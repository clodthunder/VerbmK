package com.lskj.gx.busi_account.bean.dto

import com.google.gson.annotations.SerializedName

/**
 *   创建时间:  2021/1/27
 */
class PermissionDto(
    @SerializedName("permId")
    val roleId: Int?, // 2
    @SerializedName("permName")
    val roleName: String? // user
)
