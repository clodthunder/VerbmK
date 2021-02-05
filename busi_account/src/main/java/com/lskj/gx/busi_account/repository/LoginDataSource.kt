package com.lskj.gx.busi_account.repository

import android.util.Log
import com.lskj.gx.busi_account.api.ILoginApi
import com.lskj.gx.busi_account.bean.dto.LoginDto
import com.lskj.gx.busi_account.bean.po.LoginPo
import com.lskj.gx.lib_common.base.net.BaseResponse
import com.lskj.gx.lib_common.config.ApiConstant
import com.lskj.gx.lib_common.error.ErrorCode
import com.lskj.gx.lib_common.error.GlobalExceptionHandler
import com.lskj.gx.lib_common.utils.MD5Util
import com.lskj.gx.lib_common.utils.SpUtil
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *   创建时间:  2020/11/26
 *   编写人: tzw
 *   功能描述: 登录、注册、刷新token
 */
class LoginDataSource(private val ioDispatcher: CoroutineDispatcher) : ILoginSource {
    private val TAG: String = LoginDataSource::class.java.simpleName

    /**
     * 网络登录方法
     */
    override suspend fun login(name: String, pwd: String): Boolean {
        Log.e(TAG, "调用 login")
        val okClient: OkHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ApiConstant.getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okClient)
            .build()
        val loginApi: ILoginApi = retrofit.create(ILoginApi::class.java)
        try {
            val pwdmd5 = MD5Util.md5(pwd).toString()
            val userPo = LoginPo(name, pwdmd5)
            val baseRes: BaseResponse<LoginDto> = loginApi.login(userPo)
            val result = if (baseRes.code == 200) {
                //请求成功 保存本地token
                SpUtil.SharedPreferencesUtils.TOKEN = baseRes.data!!.token
                true
            } else {
                val msg = baseRes.code?.let {
                    ErrorCode.getByCode(
                        it
                    )
                }
                GlobalExceptionHandler.instances.handleCustomerError(msg)
                false
            }
            Log.e(TAG, "调用 login 完成")
            return result
        } catch (e: Exception) {
            GlobalExceptionHandler.instances.handleSystemException(e)
            return false
        }
    }
}