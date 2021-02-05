package com.lskj.gx.verbmk

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.lskj.gx.lib_common.base.activity.BaseActivity
import com.lskj.gx.lib_common.config.AroutConfig
import com.lskj.gx.verbmk.databinding.ActivityAudioBinding

/**
 *   创建时间:  2021/2/5
 *   音频录制与播放
 */
@Route(path = AroutConfig.A_APP_AUDIO)
class AudioActivity : BaseActivity() {
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
        bding.btnStartAudio.setOnClickListener {

        }
        bding.btnStopAudio.setOnClickListener {

        }
    }
}