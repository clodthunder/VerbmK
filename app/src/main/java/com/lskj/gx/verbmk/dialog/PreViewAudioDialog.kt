package com.lskj.gx.verbmk.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.media.audiofx.Visualizer.OnDataCaptureListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.*
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.lskj.gx.basic_helper.ScreenHelper
import com.lskj.gx.basic_widget.SpectrumView
import com.lskj.gx.basic_widget.TextSeekBar
import com.lskj.gx.lib_common.config.AppContext
import com.lskj.gx.verbmk.R
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.windowManager
import java.io.File
import java.io.IOException

/**
 *   创建时间:  2021/2/7
 *   播放上传录制的音频文件
 */
class PreViewAudioDialog : DialogFragment() {
    //当前是否播放中
    private var isPlaying: Boolean = false
    private var tvTitle: AppCompatTextView? = null
    private var btnReRecord: AppCompatButton? = null
    private var btnPreView: AppCompatButton? = null
    private var btnUpload: AppCompatButton? = null
    private var btnCancle: AppCompatButton? = null
    private var listener: OnPreViewAudioBtnClick? = null

    private var spectrumView: SpectrumView? = null
    private var seekBar: TextSeekBar? = null
    private var dataCaptureListener: OnDataCaptureListener? = null
    private var handler = Handler()
    private var mRunnable: Runnable? = null
    private lateinit var mContxt: Context

    //音频播放器
    private var mMediaPlayer: MediaPlayer? = null

    //音频监听
    private var visualizer: Visualizer? = null

    companion object {
        private var instance: PreViewAudioDialog? = null
        const val EXTR_TITLE: String = "extra_title"
        const val EXTR_MAX_LONG: String = "extra_max_long"
        const val DELAY_MILLIS = 1000

        //audio 播放源
        const val EXTR_URL: String = "extra_url"

        // false 本地 true url
        const val EXTR_ISNET_SOURCE = "extr_isnet_source"

        //获取当前实例
        fun getInstance(
            bundle: Bundle
        ): PreViewAudioDialog? {
            if (instance == null) {
                synchronized(PreViewAudioDialog::class.java) {
                    if (instance == null) {
                        instance = PreViewAudioDialog()
                        instance?.arguments = bundle
                    }
                }
            }
            return instance
        }
    }

    fun setRecordAudioBtnClick(recordAudioBtnClick: OnPreViewAudioBtnClick) {
        this.listener = recordAudioBtnClick
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContxt = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window = dialog!!.window
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.CENTER)
        val view: View = LayoutInflater.from(mContxt)
            .inflate(R.layout.dialog_preview_audio, container, false)
        spectrumView = view.findViewById<SpectrumView>(R.id.spectrum_view)
        tvTitle = view.findViewById<AppCompatTextView>(R.id.tv_preview_title)
        btnReRecord = view.findViewById<AppCompatButton>(R.id.btn_re_record)
        btnPreView = view.findViewById<AppCompatButton>(R.id.btn_preview_record)
        btnUpload = view.findViewById<AppCompatButton>(R.id.btn_upload_audio)
        btnCancle = view.findViewById<AppCompatButton>(R.id.btn_cancle_pre_view)

        seekBar = view.findViewById<TextSeekBar>(R.id.tsb_progress)

        btnReRecord?.setOnClickListener {
            isPlaying = false
            dismiss()
            listener?.onReRecordClick()
        }
        //取消
        btnCancle?.setOnClickListener {
            isPlaying = false
            dismiss()
            listener?.onCancleBtnClick()
        }
        //试听
        btnPreView?.setOnClickListener {
            startPreView(!isPlaying)
            listener?.onPreViewBtnClick()
        }
        btnUpload?.setOnClickListener {
            if (isPlaying) {
                pausePreView()
            }
            dismiss()
            listener?.onUploadBtnClick()
        }
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (null != mMediaPlayer) {
                    mMediaPlayer!!.seekTo(seekBar!!.progress)
                }
            }
        })
        //mediaPlay 设置音频源
        mRunnable = Runnable {
            if (mMediaPlayer!!.currentPosition > mMediaPlayer!!.duration) {
                mRunnable?.let {
                    handler.removeCallbacks(it)
                }
                return@Runnable
            }
            //设置当前进度
            seekBar?.progress = mMediaPlayer!!.currentPosition
            // 延迟一秒触发音频录制
            mRunnable?.let {
                handler.postDelayed(it, 1000)
            }
        }

        //设置音频监听
        dataCaptureListener = object : OnDataCaptureListener {
            override fun onWaveFormDataCapture(p0: Visualizer?, p1: ByteArray?, p2: Int) {
                //声波
            }

            override fun onFftDataCapture(p0: Visualizer?, fft: ByteArray?, p2: Int) {
                //声频
                spectrumView?.post {
                    spectrumView?.setWaveData(fft)
                }
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        var display: Display? = null
        var windowManager: WindowManager? = null
        var window: Window? = null
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            val dm = DisplayMetrics()
            windowManager = mContxt.windowManager
            window = dialog.window
            if (window != null) {
                display = windowManager.defaultDisplay
                display.getMetrics(dm)
                val width = display.width
                val lp = window.attributes
                lp.gravity = Gravity.CENTER
                //左右 间隔 60dp距离
                lp.width = width - ScreenHelper.instances.dp2px(60f)
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                window.attributes = lp
                window.setGravity(Gravity.CENTER)
                window.setBackgroundDrawableResource(R.drawable.bg_white_corners_10)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            val title = bundle.get(EXTR_TITLE)
            if (title != null && !TextUtils.isEmpty(title.toString())) {
                tvTitle.let {
                    it?.text = title.toString()
                }
            }
            //初始化 最大进度
            val realMax = bundle.getInt(EXTR_MAX_LONG)
            seekBar?.max = realMax
        }
    }

    /**
     * 播放与暂停操作
     * @param isPlayer 操作true 播放 false 暂停
     */
    private fun startPreView(isPlayer: Boolean) {
        if (isPlayer) {
            isPlaying = true
            mMediaPlayer?.let {//不为空执行
                mMediaPlayer!!.start()
                mMediaPlayer!!.seekTo(mMediaPlayer!!.currentPosition)
                //触发播放操作
                mRunnable?.let {
                    handler.postDelayed(it, DELAY_MILLIS.toLong())
                }
            } ?: let {
                initMediaPlayer()
            }
            btnPreView?.text = "暂停"
        } else {
            pausePreView()
        }
    }

    private fun pausePreView() {
        isPlaying = false
        if (null != mMediaPlayer) {
            mMediaPlayer!!.pause()
            //停止播放
            mRunnable?.let { handler.removeCallbacks(it) }
        }
    }

    private fun stopPreView() {
        //当前进度归0
        seekBar?.progress = 0
        //控件
        btnPreView?.text = "试听"
        isPlaying = false
        mMediaPlayer?.let {//mMediaPlayer 不为null
            // 释放所有对象
            visualizer?.let {
                it.release()
            }
            mMediaPlayer?.stop()
            mMediaPlayer?.release()
            mMediaPlayer = null
        }
        mRunnable?.let { handler.removeCallbacks(it) }
    }

    /**
     * 初始化media player
     */
    private fun initMediaPlayer() {
        val args = arguments
        var url: String? = null
        var uri: Uri? = null
        //默认加载 net类型
        var sourceType: Boolean = true
        if (args != null) {
            sourceType = args.getBoolean(EXTR_ISNET_SOURCE)
            url = args.get(EXTR_URL) as String?
            if (!sourceType) {//net
                val audioFile = File(url)
                uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    /*7.0以上要通过FileProvider将File转化为Uri*/
                    FileProvider.getUriForFile(
                        mContxt,
                        AppContext.instance.getContext().packageName + ".fileprovider",
                        audioFile
                    )
                } else {
                    Uri.fromFile(audioFile)
                }
            }
        }
        if (TextUtils.isEmpty(url)) {
            toast("播放源异常")
            return
        }
        try {
            mMediaPlayer = MediaPlayer()
            mMediaPlayer?.reset()
            if (sourceType) {
                mMediaPlayer!!.setDataSource(url)
            } else {
                uri?.let { mMediaPlayer!!.setDataSource(mContxt, it) }
            }
            mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mMediaPlayer?.prepareAsync()
            mMediaPlayer?.setOnPreparedListener {
                mMediaPlayer?.start()
                seekBar?.max = mMediaPlayer!!.duration
                //触发播放 音频
                mRunnable?.let { it1 -> handler.postDelayed(it1, DELAY_MILLIS.toLong()) }
                initVisualizer()
            }
            //播放完毕
            mMediaPlayer!!.setOnCompletionListener {
                stopPreView()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun initVisualizer() {
        try {
            if (visualizer != null) {
                visualizer?.release()
            }
            visualizer = Visualizer(mMediaPlayer!!.audioSessionId)
            val captureSize = Visualizer.getCaptureSizeRange()[1]
            val captureRate = Visualizer.getMaxCaptureRate() * 2 / 4
            visualizer?.captureSize = captureSize
            visualizer?.setDataCaptureListener(dataCaptureListener, captureRate, true, true)
            visualizer?.scalingMode = Visualizer.SCALING_MODE_NORMALIZED
            visualizer?.enabled = true
        } catch (e: Exception) {
            e.message
        }
    }

    //更新数据
    fun updateArguments(url: String, realMax: Int, isNet: Boolean) {
        val args = arguments
        args?.putString(EXTR_URL, url)
        args?.putBoolean(EXTR_ISNET_SOURCE, isNet)
        args?.putInt(EXTR_MAX_LONG, realMax)
        arguments = args

        seekBar?.max = realMax
        isPlaying = false
        //先停止
        stopPreView()
    }

    interface OnPreViewAudioBtnClick {
        fun onCancleBtnClick()
        fun onPreViewBtnClick()
        fun onReRecordClick()
        fun onUploadBtnClick()
    }

    override fun onDetach() {
        stopPreView()
        super.onDetach()
    }
}