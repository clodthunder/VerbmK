package com.lskj.gx.busi_account

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ktx.immersionBar
import com.lskj.gx.basi_base.activity.BaseActivity
import com.lskj.gx.basic_config.AroutConfig
import com.lskj.gx.basic_helper.KeyBoardHelper
import com.lskj.gx.basic_helper.ScreenHelper
import com.lskj.gx.basic_helper.intfaces.OnKeyBoardChangeListener
import com.lskj.gx.busi_account.databinding.ActActivityLoginBinding

/**
 *   创建时间:  2020/11/18
 *   编写人: tzw
 *   功能描述:
 */
@Route(path = AroutConfig.A_ACCOUNT_LOGIN)
class LoginActivity : BaseActivity(), OnKeyBoardChangeListener {
    private lateinit var dbding: ActActivityLoginBinding

    override fun firstBdLayout() {
        dbding = DataBindingUtil.setContentView(this, R.layout.act_activity_login)
        immersionBar {
            statusBarColor(R.color.ui_tool_bar_color)
            navigationBarColor(R.color.ui_tool_bar_color)
            fitsSystemWindows(true)
            statusBarDarkFont(true)
            navigationBarDarkIcon(true)
        }
    }

    override fun afterBdInitParams() {
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun afterPamsInitTbar() {
        dbding.uiTvTitle.text = getString(R.string.act_login_str)
        dbding.uiIvBack.setOnClickListener {
            finish()
        }
    }

    override fun afterTbarPreData(savedInstanceState: Bundle?) {
    }

    override fun afterPreData(savedInstanceState: Bundle?) {
        //设置软键盘监听
        KeyBoardHelper.instances.setKeyBoardHelper(this, this)
    }

    override fun onKeyBoardShow(isShow: Boolean) {
        dbding.svLogin.post {
            if (isShow) {
                dbding.svLogin.smoothScrollTo(
                    0,
                    ScreenHelper.instances.px2dp(
                        (dbding.btnLogin.bottom - dbding.svLogin.top).toFloat()
                                - ScreenHelper.instances.getStatusBarHeight(this)
                    )
                )
            } else {
                dbding.svLogin.smoothScrollTo(0, 0)
            }
        }
        Toast.makeText(this, "$isShow", Toast.LENGTH_LONG).show()
    }
}