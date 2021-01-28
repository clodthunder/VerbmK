package com.lskj.gx.busi_account.repository

import com.lskj.gx.busi_account.bean.dto.HeaderImgDto
import com.lskj.gx.busi_account.bean.dto.PermissionDto
import com.lskj.gx.busi_account.bean.dto.RoleDto
import com.lskj.gx.busi_account.bean.dto.UserDto

/**
 *   创建时间:  2021/1/14
 *   编写人: tzw
 *   功能描述: 用户相关方法
 */
interface IUserSource {
    /**
     * 获取用户
     */
    suspend fun getUserInfo(): UserDto?

    /**
     * 获取用户头像地址
     */
    suspend fun getUserHeaderImage(): HeaderImgDto?

    /**
     * 获取用户角色列表
     */
    suspend fun getUserRoles(): ArrayList<RoleDto>?


    suspend fun getUserPerms(): ArrayList<PermissionDto>?

}