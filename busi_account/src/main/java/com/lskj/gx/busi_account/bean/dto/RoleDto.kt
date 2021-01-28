package com.lskj.gx.busi_account.bean.dto

import com.google.gson.annotations.SerializedName


/**
 *   创建时间:  2021/1/14
 *   编写人: tzw
 *   功能描述: 用户角色
 */
data class RoleDto(
    @SerializedName("roleId")
    val roleId: Int?, // 2
    @SerializedName("roleName")
    val roleName: String? // user
)