package com.lskj.gx.busi_account.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.viewModelScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ktx.immersionBar
import com.lskj.gx.basi_base.activity.BaseActivity
import com.lskj.gx.basic_helper.KeyBoardHelper
import com.lskj.gx.basic_helper.ScreenHelper
import com.lskj.gx.basic_helper.intfaces.OnKeyBoardChangeListener
import com.lskj.gx.busi_account.R
import com.lskj.gx.busi_account.SingleLoadingUtil
import com.lskj.gx.busi_account.databinding.ActActivityLoginBinding
import com.lskj.gx.busi_account.viewmodel.LoginViewModel
import com.lskj.gx.lib_common.config.AroutConfig
import kotlinx.coroutines.cancel

/**
 *   创建时间:  2020/11/18
 */
@Route(path = AroutConfig.A_ACCOUNT_LOGIN)
class LoginActivity : BaseActivity(), OnKeyBoardChangeListener {
    private lateinit var dbding: ActActivityLoginBinding
    private val loginModel: LoginViewModel
            by viewModels { LoginViewModel.LoginViewModelFactory }

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
            if (KeyBoardHelper.instances.isKeyBoradShowing(this)) {
                KeyBoardHelper.instances.hiddenKeyBoradForced(this)
            } else {
                finish()
            }
        }
    }

    override fun afterTbarPreData(savedInstanceState: Bundle?) {
    }

    override fun afterPreData(savedInstanceState: Bundle?) {
        //设置软键盘监听
        KeyBoardHelper.instances.setKeyBoardHelper(this, this)

        //触发登录
        dbding.btnLogin.setOnClickListener {
            KeyBoardHelper.instances.hiddenKeyBoradForced(this)
            val nameStr = dbding.etUserName.text.toString()
            if (TextUtils.isEmpty(nameStr)) {
                dbding.etUserName.requestFocus()
                dbding.etUserName.error = "用户名不能为空！"
                return@setOnClickListener
            }
            var pwdStr = dbding.etUserPwd.text.toString()
            if (TextUtils.isEmpty(pwdStr)) {
                dbding.etUserPwd.requestFocus()
                dbding.etUserPwd.error = "密码不能为空！"
                return@setOnClickListener
            }
            loginModel.loginWithXc(nameStr, pwdStr,this)
        }
    }

    override fun onKeyBoardShow(isShow: Boolean) {
        if (isShow) {
            if (dbding.vbSpace.visibility != View.VISIBLE) {
                dbding.vbSpace.visibility = View.VISIBLE
            }
        } else {
            if (dbding.vbSpace.visibility != View.GONE) {
                dbding.vbSpace.visibility = View.GONE
            }
        }
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
    }

    override fun onBackPressed() {
        //关闭软件盘
        if (KeyBoardHelper.instances.isKeyBoradShowing(this)) {
            KeyBoardHelper.instances.hiddenKeyBoradForced(this)
            return
        }
        //取消任务
        loginModel.viewModelScope.cancel()
        //关闭进度
        SingleLoadingUtil.instance.dissLoading()
        super.onBackPressed()
    }

    override fun onDestroy() {
        //取消任务
        loginModel.viewModelScope.cancel()
        //关闭进度
        SingleLoadingUtil.instance.dissLoading()
        super.onDestroy()
    }
}