package com.lskj.gx.verbmk

import android.app.Activity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.lskj.gx.hori_res.PreViewAudioDialog
import com.lskj.gx.hori_res.RecordAudioDialog
import com.lskj.gx.lib_common.base.activity.BaseActivity
import com.lskj.gx.lib_common.config.AroutConfig
import com.lskj.gx.verbmk.databinding.ActivityAudioBinding
import org.jetbrains.anko.toast
import java.io.File

/**
 *   创建时间:  2021/2/5
 *   音频录制与播放
 */
@Route(path = AroutConfig.A_APP_AUDIO)
class AudioActivity : BaseActivity() {
    private var mContext: Activity? = null
    private var recordDialog: RecordAudioDialog? = null
    private var preViewAudioDialog: PreViewAudioDialog? = null

    //视图绑定
    private lateinit var bding: ActivityAudioBinding

    override fun firstBdLayout() {
        bding = DataBindingUtil.setContentView(this, R.layout.activity_audio)
    }

    override fun afterBdInitParams() {

    }

    override fun afterPamsInitTbar() {
    }

    override fun afterTbarPreData(savedInstanceState: Bundle?) {
    }

    override fun afterPreData(savedInstanceState: Bundle?) {
        mContext = this
        bding.btnStartAudio.setOnClickListener {
            startAudioRecord()
        }
        bding.btnMicLevel.setOnClickListener {
            toast("配合音频录制")
        }
    }

    /**
     * 启动音频录制
     */
    private fun startAudioRecord() {
        val args = Bundle()
        args.putBoolean(RecordAudioDialog.EXTR_AUTO_START, true)
        args.putString(RecordAudioDialog.EXTR_TITLE, "录制音频...")

        recordDialog?.let {
            recordDialog?.updateArgs(false)
        } ?: let {
            recordDialog = RecordAudioDialog.getInstance(args)
        }
        recordDialog?.show(this.supportFragmentManager, "record_dialog")
        recordDialog?.setRecordAudioBtnClick(object : RecordAudioDialog.OnRecordAudioBtnClick {
            override fun onCancleBtnClick() {
                toast("cancle click")
            }

            override fun onConfimBtnClick(audioFile: File, realMax: Int) {
                startPreViewAudio(audioFile, realMax)
            }

            override fun onSpectrumUpdate() {
                toast("onSpectrumUpdate")
            }

            override fun onStartBtnClick() {
                toast("开始录制")
            }
        })
    }

    /**
     * 结束音频录制
     */
    private fun stopAudioRecord() {
        recordDialog?.dismiss()
    }

    private fun startPreViewAudio(audioFile: File, realMax: Int) {
        val args = Bundle()
        args.putString(PreViewAudioDialog.EXTR_TITLE, "音频预览...")
        args.putInt(PreViewAudioDialog.EXTR_MAX_LONG, realMax)
        if (audioFile.exists()) {
            args.putBoolean(PreViewAudioDialog.EXTR_ISNET_SOURCE, false)
            args.putString(PreViewAudioDialog.EXTR_URL, audioFile.absolutePath)
        } else {
            args.putString(
                PreViewAudioDialog.EXTR_URL,
                "http://144.34.189.222:9090/audios/5rWL6K-V.mp3"
            )
            args.putBoolean(PreViewAudioDialog.EXTR_ISNET_SOURCE, true)
        }
        preViewAudioDialog?.let {
            preViewAudioDialog?.updateArguments(audioFile.absolutePath, realMax, false)
        } ?: let {
            preViewAudioDialog = PreViewAudioDialog.getInstance(args)
        }
        //初始化
        preViewAudioDialog?.setRecordAudioBtnClick(object :
            PreViewAudioDialog.OnPreViewAudioBtnClick {
            override fun onPreViewBtnClick() {
                toast("preview audio")
            }

            override fun onReRecordClick() {
                startAudioRecord()
            }

            override fun onUploadBtnClick() {
                toast("upload audio")
            }

            override fun onCancleBtnClick() {
                toast("cancle audio")
            }
        })
        preViewAudioDialog?.show(this.supportFragmentManager, "preview_audio")
    }

    private fun stopPreViewAudio() {
        preViewAudioDialog?.dismiss()
    }
}