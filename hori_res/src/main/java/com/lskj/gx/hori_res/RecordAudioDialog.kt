package com.lskj.gx.hori_res

import android.Manifest
import android.graphics.drawable.Drawable
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.XXPermissions.startPermissionActivity
import com.lskj.gx.basic_widget.TextSeekBar
import com.lskj.gx.lib_common.dialog.BaseCenterDialogFragment
import com.lskj.gx.lib_common.utils.FileUtil
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.toast
import java.io.File
import java.util.*
import kotlin.math.log10


/**
 *   创建时间:  2021/2/7
 *   录制音频
 *   待优化项： 时间限制 、压缩方式优化 文件大小
 */
class RecordAudioDialog : BaseCenterDialogFragment() {
    private var listener: OnRecordAudioBtnClick? = null
    private var tvTitle: AppCompatTextView? = null
    private var btnCancle: AppCompatButton? = null
    private var btnConfim: AppCompatButton? = null
    private var btnStart: AppCompatButton? = null
    private var ivAudioBig: AppCompatImageView? = null
    private var mMediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    private var seekBar: TextSeekBar? = null
    private var timerTask: TimerTask? = null
    private var timer: Timer? = null

    //默认显示的title
    private val defaultTitle: String = "录制音频..."

    //实际显示的title
    private var currentTitle: String = defaultTitle;

    //最大录制时间 60 秒
    private var maxDuration: Int = 60 * 1000

    //mic 音量
    private val SAMPLE_RATE_IN_HZ: Int = 8000
    private var mAudioRecord: AudioRecord? = null

    // 当前是否正在检测mic
    private var isGetVoiceRun: Boolean = false
    private val BUFFER_SIZE: Int = AudioRecord.getMinBufferSize(
        SAMPLE_RATE_IN_HZ,
        AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT
    )

    companion object {
        private var instance: RecordAudioDialog? = null

        //标题
        const val EXTR_TITLE: String = "extra_title"

        //进入页面是否自动开始录制
        const val EXTR_AUTO_START: String = "extr_auto_start"

        //最大录制时间 默认一分钟
        const val EXTR_MAX_DURATION: String = "extr_max_duration"

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_record_audio, container, false)
        tvTitle = view.findViewById<AppCompatTextView>(R.id.tv_title)
        btnCancle = view.findViewById<AppCompatButton>(R.id.btn_cancle_record)
        btnConfim = view.findViewById<AppCompatButton>(R.id.btn_confim_record)
        btnStart = view.findViewById<AppCompatButton>(R.id.btn_start_record)
        ivAudioBig = view.findViewById<AppCompatImageView>(R.id.iv_audio_big)
        seekBar = view.findViewById<TextSeekBar>(R.id.tsb_progress)
        //设置当前录制进度
        seekBar?.progress = 0
        seekBar?.max = maxDuration

        btnCancle?.setOnClickListener {
            delTempFile()
            stopRecord()
            listener?.onCancleBtnClick()
            dismiss()
        }
        btnConfim?.setOnClickListener {
            completeRecord()
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
                            startPermissionActivity(mContxt, permissions);
                        } else {
                            toast("获取录音和日历权限失败")
                        }
                    }
                })
            listener?.onStartBtnClick()
        }
        return view
    }

    private fun completeRecord() {
        val cProgress = seekBar?.progress!!
        stopRecord()
        audioFile.let {
            if (it != null) {
                listener?.onConfimBtnClick(it, cProgress)
            }
        }
        dismiss()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            val title = bundle.getString(EXTR_TITLE)
            if (title != null && !TextUtils.isEmpty(title.toString())) {
                currentTitle = title.toString()
                tvTitle.let {
                    it?.text = currentTitle
                }
            }
            val maxDura: Int = bundle.getInt(EXTR_MAX_DURATION)
            if (maxDura != 0) {
                maxDuration = maxDura
            }
        }
    }

//    /**
//     * 创建默认的音频文件
//     */
//    private fun createTempAudio() {
//        val timeStamp = SimpleDateFormat("yyyy_MM_dd_HHmmss").format(Date())
//        //本地文件路径
//        audioFile = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
//            File(mContxt.getExternalFilesDir("audio"), "$timeStamp.mp3")
//        } else {
//            File(
//                mContxt.filesDir.toString() + File.separator + "audio",
//                "$timeStamp.mp3"
//            )
//        }
//        if (audioFile?.exists()!!) {
//            audioFile?.delete()
//        }
//        //文件
//        audioFile = File.createTempFile(
//            timeStamp,
//            ".mp3",
//            audioFile?.parentFile?.absoluteFile
//        )
//    }

    /**
     * 开始录制
     */
    private fun startRecord() {
        tvTitle?.text = "正在录制音频..."
        initMediaRecord()
        mMediaRecorder?.start()
        //mic 启用
        isGetVoiceRun = true
        //mic 输入声音
        getNoiseLevel()
        // 开始录制 累加进度
        updateSeekBar()
    }

    private fun initMediaRecord() {
        audioFile = FileUtil.createImageFile(mContxt, FileUtil.TYPE_AUDIO)
        mMediaRecorder.let {
            mMediaRecorder = MediaRecorder()
            mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mMediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mMediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mMediaRecorder?.setOutputFile(audioFile?.absolutePath)
            //最大多久 默认最大录制1分钟
            mMediaRecorder?.setMaxDuration(maxDuration)
        }
        mMediaRecorder?.prepare()
    }

    //更新进度条
    private fun updateSeekBar() {
        seekBar?.progress = 0
        seekBar?.max = maxDuration
        timerTask = object : TimerTask() {
            override fun run() {
                val cProgress: Int? = seekBar?.progress
                if (cProgress != null) {
                    if (cProgress <= maxDuration) {
                        seekBar?.progress = cProgress.plus(1000)
                    } else {
                        completeRecord()
                    }
                }
            }

            override fun cancel(): Boolean {
                seekBar?.progress = 0
                seekBar?.max = maxDuration
                return super.cancel()
            }
        }
        timer = Timer()
        timer!!.schedule(timerTask, 1000, 1000)
    }

    /**
     * 获取音频音量大小
     */
    private fun getNoiseLevel() {
        mAudioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT,
            AudioFormat.ENCODING_PCM_16BIT,
            BUFFER_SIZE
        )
        Thread {
            mAudioRecord!!.startRecording()
            val buffer = ByteArray(BUFFER_SIZE)
            while (isGetVoiceRun) {
                //r是实际读取的数据长度，一般而言r会小于buffersize
                val r = mAudioRecord!!.read(buffer, 0, BUFFER_SIZE)
                var v: Long = 0
                // 将 buffer 内容取出，进行平方和运算
                for (value in buffer) {
                    v += value * value.toLong()
                }
                // 平方和除以数据总长度，得到音量大小。
                val mean = v / r.toDouble()
                val volume: Double = 10 * log10(mean)

                var audioVoice: Drawable? = null
                when {
                    volume <= 0 -> {
                        audioVoice = mContxt.getDrawable(R.mipmap.audio_voice_zearo)
                    }
                    volume <= 30 -> {
                        audioVoice = mContxt.getDrawable(R.mipmap.audio_voice_30)
                    }
                    volume <= 50 -> {
                        audioVoice = mContxt.getDrawable(R.mipmap.audio_voice_50)
                    }
                    volume <= 70 -> {
                        audioVoice = mContxt.getDrawable(R.mipmap.audio_voice_70)
                    }
                    else -> {
                        audioVoice = mContxt.getDrawable(R.mipmap.audio_voice_full)
                    }
                }
                runOnUiThread {
                    ivAudioBig?.setImageDrawable(audioVoice)
                }
                synchronized(RecordAudioDialog::javaClass) {
                    try {
                        Thread.sleep(100) // 一秒十次
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
            mAudioRecord!!.stop()
            mAudioRecord!!.release()
            mAudioRecord = null
        }.start()
    }

    /**
     * 停止录制
     */
    private fun stopRecord() {
        tvTitle?.text = defaultTitle
        //mic 禁用启用
        isGetVoiceRun = false
        //定时器
        timer?.let {
            it.cancel()
        }
        timer = null
        timerTask?.cancel()
        //重置进度
        seekBar?.progress = 0
        seekBar?.max = maxDuration
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
        fun onConfimBtnClick(audioFile: File, realMax: Int)
        fun onStartBtnClick()
        fun onSpectrumUpdate()
    }

    fun setRecordAudioBtnClick(recordAudioBtnClick: OnRecordAudioBtnClick) {
        this.listener = recordAudioBtnClick;
    }
}

