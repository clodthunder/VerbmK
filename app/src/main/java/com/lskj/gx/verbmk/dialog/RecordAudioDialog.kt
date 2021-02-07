package com.lskj.gx.verbmk.dialog

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.XXPermissions.startPermissionActivity
import com.lskj.gx.basic_helper.ScreenHelper
import com.lskj.gx.basic_widget.SpectrumView
import com.lskj.gx.basic_widget.TextSeekBar
import com.lskj.gx.verbmk.R
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.windowManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 *   创建时间:  2021/2/7
 *   录制音频
 *     spectrumView?.post {
 *        spectrumView?.setWaveData(fft)
 *     }
 */
class RecordAudioDialog : DialogFragment() {
    private var listener: OnRecordAudioBtnClick? = null
    private var tvTitle: AppCompatTextView? = null
    private var btnCancle: AppCompatButton? = null
    private var btnConfim: AppCompatButton? = null
    private var btnStart: AppCompatButton? = null
    private var mMediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    private var spectrumView: SpectrumView? = null
    private var seekBar: TextSeekBar? = null
    private var timerTask: TimerTask? = null
    private var timer: Timer? = null
    private lateinit var mContext: Context

    companion object {
        private var instance: RecordAudioDialog? = null

        //标题
        const val EXTR_TITLE: String = "extra_title"

        //进入页面是否自动开始录制
        const val EXTR_AUTO_START: String = "extr_auto_start"

        //获取当前实例
        fun getInstance(
            bundle: Bundle
        ): RecordAudioDialog? {
            if (instance == null) {
                synchronized(RecordAudioDialog::class.java) {
                    if (instance == null) {
                        instance = RecordAudioDialog()
                        instance?.arguments = bundle
                    }
                }
            }
            return instance
        }
    }

    fun updateArgs(isAutoRecord: Boolean) {
        val args = arguments
        args?.let {
            args.putBoolean(EXTR_AUTO_START, isAutoRecord)
        }
        stopRecord()
    }

    fun setRecordAudioBtnClick(recordAudioBtnClick: OnRecordAudioBtnClick) {
        this.listener = recordAudioBtnClick;
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
        val view: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_record_audio, container, false)
        tvTitle = view.findViewById<AppCompatTextView>(R.id.tv_title)
        btnCancle = view.findViewById<AppCompatButton>(R.id.btn_cancle_record)
        btnConfim = view.findViewById<AppCompatButton>(R.id.btn_confim_record)
        btnStart = view.findViewById<AppCompatButton>(R.id.btn_start_record)

        spectrumView = view.findViewById<SpectrumView>(R.id.spectrum_view)
        seekBar = view.findViewById<TextSeekBar>(R.id.tsb_progress)
        //设置当前录制进度
        seekBar?.progress = 0
        seekBar?.max = 60

        btnCancle?.setOnClickListener {
            delTempFile()
            stopRecord()
            listener?.onCancleBtnClick()
            dismiss()
        }
        btnConfim?.setOnClickListener {
            stopRecord()
            audioFile.let {
                if (it != null) {
                    listener?.onConfimBtnClick(it)
                }
            }
            dismiss()
        }
        btnStart?.setOnClickListener {
            XXPermissions.with(this)
                .permission(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                .permission(Manifest.permission.RECORD_AUDIO)
                .permission(Manifest.permission.MODIFY_AUDIO_SETTINGS)
                .request(object : OnPermissionCallback {
                    override fun onGranted(
                        permissions: MutableList<String>?,
                        all: Boolean
                    ) {
                        if (all) {
                            startRecord()
                        }
                    }

                    override fun onDenied(
                        permissions: MutableList<String>?,
                        never: Boolean
                    ) {
                        super.onDenied(permissions, never)
                        if (never) {
                            toast("获取录音和日历权限失败")
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            startPermissionActivity(mContext, permissions);
                        } else {
                            toast("获取录音和日历权限失败")
                        }
                    }
                })
            listener?.onStartBtnClick()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
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
            windowManager = mContext.windowManager
            window = dialog.window
            if (window != null) {
                mContext.windowManager.defaultDisplay
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
        }
    }

    /**
     * 创建默认的音频文件
     */
    private fun createTempAudio() {
        val timeStamp = SimpleDateFormat("yyyy_MM_dd_HHmmss").format(Date())
        //本地文件路径
        audioFile = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            File(mContext.getExternalFilesDir("audio"), "$timeStamp.mp3")
        } else {
            File(
                mContext.filesDir.toString() + File.separator + "audio",
                "$timeStamp.mp3"
            )
        }
        if (audioFile?.exists()!!) {
            audioFile?.delete()
        }
        //文件
        audioFile = File.createTempFile(
            timeStamp,
            ".mp3",
            audioFile?.parentFile?.absoluteFile
        )
    }

    /**
     * 开始录制
     */
    private fun startRecord() {
        initMediaRecord()
        mMediaRecorder?.start()
        // 开始录制 累加进度
        updateSeekBar()
    }

    private fun initMediaRecord() {
        createTempAudio()
        mMediaRecorder.let {
            mMediaRecorder = MediaRecorder()
            mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mMediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mMediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mMediaRecorder?.setOutputFile(audioFile?.absolutePath)
        }
        mMediaRecorder?.prepare()
    }

    //更新进度条
    private fun updateSeekBar() {
        seekBar?.progress = 0
        seekBar?.max = 60
        timerTask = object : TimerTask() {
            override fun run() {
                val cProgress: Int? = seekBar?.progress
                if (cProgress != null) {
                    if (cProgress <= 60) {
                        seekBar?.progress = cProgress.plus(1)
                    } else {
                        System.out.println("录制超过1分钟自动结束")
                        stopRecord()
                    }
                }
            }

            override fun cancel(): Boolean {
                seekBar?.progress = 0
                seekBar?.max = 60
                return super.cancel()
            }
        }
        timer = Timer()
        timer!!.schedule(timerTask, 1000, 1000)
    }

    /**
     * 停止录制
     */
    private fun stopRecord() {
        timer?.let {
            it.cancel()
        }
        timer = null
        timerTask?.cancel()
        //重置进度
        seekBar?.progress = 0
        seekBar?.max = 60
        //重置record
        mMediaRecorder?.let {
            //处理状态异常
            mMediaRecorder?.stop()
            mMediaRecorder?.release()
            mMediaRecorder = null
        }
    }

    private fun delTempFile() {
        if (audioFile != null && audioFile!!.exists()) {
            audioFile?.delete()
        }
    }

    override fun onDetach() {
        stopRecord()
        super.onDetach()
    }

    interface OnRecordAudioBtnClick {
        fun onCancleBtnClick()
        fun onConfimBtnClick(audioFile: File)
        fun onStartBtnClick()
        fun onSpectrumUpdate()
    }
}

