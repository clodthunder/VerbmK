package com.lskj.gx.busi_account.api

import com.lskj.gx.basi_base.net.BaseResponse
import com.lskj.gx.busi_account.bean.dto.LoginDto
import com.lskj.gx.busi_account.bean.po.LoginPo
import retrofit2.http.Body
import retrofit2.http.POST

/**
 *   创建时间:  2020/11/26
 *   编写人: tzw
 *   功能描述: 登录接口api
 */
interface ILoginApi {

    //调用登录接口-返回token
    @POST("/authz/login")
    suspend fun login(@Body userPo: LoginPo): BaseResponse<LoginDto>
}