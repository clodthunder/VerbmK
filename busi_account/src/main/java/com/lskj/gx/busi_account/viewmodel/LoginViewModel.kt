package com.lskj.gx.busi_account.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lskj.gx.busi_account.bean.dto.PermissionDto
import com.lskj.gx.busi_account.bean.dto.RoleDto
import com.lskj.gx.busi_account.repository.LoginDataSource
import com.lskj.gx.busi_account.repository.UserDataSource
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *   创建时间:  2020/11/26
 *   编写人: tzw
 *   功能描述: 登录页面使用数据源
 */
class LoginViewModel(
    private val loginDataS: LoginDataSource,
    private val userDataS: UserDataSource
) : ViewModel() {
    private val TAG: String = LoginDataSource::class.java.simpleName

    /**
     * 获取dataSource
     */
    object LoginViewModelFactory : ViewModelProvider.Factory {
        private val loginDs = LoginDataSource(Dispatchers.IO)
        private val userDs = UserDataSource(Dispatchers.IO)

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(loginDs, userDs) as T
        }
    }

    /**
     * 使用协程请求
     */
    fun loginWithXc(name: String, pwd: String, context: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            val loadingPopup = XPopup.Builder(context)
                .dismissOnBackPressed(false)
                .isDarkTheme(false)
                .asLoading("登录中...")
                .show() as LoadingPopupView
            //请求登录信息
            val data: Boolean = loginDataS.login(name, pwd)
            if (data) {
                //请求用户信息
                val userDto = userDataS.getUserInfo()
                userDto.let {
                    //将该用户存入本地数据库
                    val headerUrl = userDataS.getUserHeaderImage()
                    headerUrl.let {

                    }
                    val roles: ArrayList<RoleDto>? = userDataS.getUserRoles()
                    roles.let {
                        Log.d(TAG, "该用户角色列表：" + arrayOf(roles).contentDeepToString())
                    }
                    val perms: ArrayList<PermissionDto>? = userDataS.getUserPerms()
                    perms.let {
                        Log.d(TAG, "该用户权限列表：" + arrayOf(perms).contentDeepToString())
                    }
                }
                loadingPopup.postDelayed({ loadingPopup.setTitle("登录成功") }, 100)
            } else {
                loadingPopup.postDelayed({ loadingPopup.setTitle("登录失败") }, 100)
            }
            loadingPopup.delayDismiss(1600)
        }
    }
}


