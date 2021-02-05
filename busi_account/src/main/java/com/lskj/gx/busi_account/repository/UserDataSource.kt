package com.lskj.gx.busi_account.repository

import android.util.Log
import com.lskj.gx.busi_account.api.IUserApi
import com.lskj.gx.busi_account.bean.dto.HeaderImgDto
import com.lskj.gx.busi_account.bean.dto.PermissionDto
import com.lskj.gx.busi_account.bean.dto.RoleDto
import com.lskj.gx.busi_account.bean.dto.UserDto
import com.lskj.gx.lib_common.base.net.BaseResponse
import com.lskj.gx.lib_common.config.ApiConstant
import com.lskj.gx.lib_common.error.ErrorCode
import com.lskj.gx.lib_common.error.GlobalExceptionHandler
import com.lskj.gx.lib_common.utils.SpUtil
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *   创建时间:  2021/1/14
 *   编写人: tzw
 *   功能描述: 用户相关
 */
class UserDataSource(private val ioDispatcher: CoroutineDispatcher) : IUserSource {
    private val TAG: String = LoginDataSource::class.java.simpleName

    /**
     * 获取用户信息
     */
    override suspend fun getUserInfo(): UserDto? {
        Log.e(TAG, "调用 getUserInfo")
        val okClient: OkHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(Interceptor {
                val request = it.request().newBuilder()
                    .addHeader("token", SpUtil.SharedPreferencesUtils.TOKEN)
                    .build()
                it.proceed(request)
            })
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
        val userApi: IUserApi = retrofit.create(IUserApi::class.java)
        try {
            val baseRes: BaseResponse<UserDto> = userApi.getUserInfo()
            if (baseRes.code == 200) {
                return baseRes.data
            } else {
                GlobalExceptionHandler.instances
                    .handleCustomerError(baseRes.code?.let { ErrorCode.getByCode(it) })
            }
            Log.e(TAG, "调用 getUserInfo 完成")
            return null
        } catch (e: Exception) {
            GlobalExceptionHandler.instances.handleSystemException(e)
            return null
        }
    }

    /**
     * 获取用户头像信息
     */
    override suspend fun getUserHeaderImage(): HeaderImgDto? {
        Log.e(TAG, "调用 getUserHeaderImage")
        val okClient: OkHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(Interceptor {
                val request = it.request().newBuilder()
                    .addHeader("token", SpUtil.SharedPreferencesUtils.TOKEN)
                    .build()
                it.proceed(request)
            })
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
        val userApi: IUserApi = retrofit.create(IUserApi::class.java)
        return try {
            val baseRes: BaseResponse<HeaderImgDto> = userApi.getHeaderUrl()
            if (baseRes.code == 200) {
                Log.e(TAG, "调用 getUserHeaderImage 完成")
                baseRes.data
            } else {
                Log.e(TAG, "调用 getUserHeaderImage 完成:null")
                null
            }
        } catch (e: Exception) {
            GlobalExceptionHandler.instances.handleSystemException(e)
            return null
        }
    }

    /**
     * 查询用户角色列表
     */
    override suspend fun getUserRoles(): ArrayList<RoleDto>? {
        Log.e(TAG, "调用 getUserRoles")
        val okClient: OkHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(Interceptor {
                val request = it.request().newBuilder()
                    .addHeader("token", SpUtil.SharedPreferencesUtils.TOKEN)
                    .build()
                it.proceed(request)
            })
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
        val iUserApi = retrofit.create(IUserApi::class.java)
        return try {
            val baseRes: BaseResponse<ArrayList<RoleDto>> = iUserApi.getRoles()
            if (baseRes.code == 200) {
                baseRes.data
            } else {
                null
            }
        } catch (e: Exception) {
            GlobalExceptionHandler.instances.handleSystemException(e)
            return null
        }
    }

    override suspend fun getUserPerms(): ArrayList<PermissionDto>? {
        Log.e(TAG, "调用 getUserPerms")
        val okClient: OkHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(Interceptor {
                val request = it.request().newBuilder()
                    .addHeader("token", SpUtil.SharedPreferencesUtils.TOKEN)
                    .build()
                it.proceed(request)
            })
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
        val iUserApi = retrofit.create(IUserApi::class.java)
        return try {
            val baseRes: BaseResponse<ArrayList<PermissionDto>> = iUserApi.getPerms()
            if (baseRes.code == 200) {
                baseRes.data
            } else {
                null
            }
        } catch (e: Exception) {
            GlobalExceptionHandler.instances.handleSystemException(e)
            return null
        }
    }
}