package com.lskj.gx.busi_account.api

import com.lskj.gx.busi_account.bean.dto.HeaderImgDto
import com.lskj.gx.busi_account.bean.dto.PermissionDto
import com.lskj.gx.busi_account.bean.dto.RoleDto
import com.lskj.gx.busi_account.bean.dto.UserDto
import com.lskj.gx.lib_common.base.net.BaseResponse
import retrofit2.http.GET

/**
 *  2021/1/14
 *   编写人: tzw
 *   功能描述: 用户相关的接口
 */
interface IUserApi {

    /**
     * 获取用户详细信息
     */
    @GET("/user/getUserInfo")
    suspend fun getUserInfo(): BaseResponse<UserDto>

    /**
     * 获取用户头像地址
     */
    @GET("/user/getUserHeaderImage")
    suspend fun getHeaderUrl(): BaseResponse<HeaderImgDto>


    /**
     * 获取用户角色
     */
    @GET("/userRole/getRolesByUserId")
    suspend fun getRoles(): BaseResponse<ArrayList<RoleDto>>

    /**
     * 获取用户权限
     */
    @GET("/permission/getPermsByUserId")
    suspend fun getPerms(): BaseResponse<ArrayList<PermissionDto>>
}