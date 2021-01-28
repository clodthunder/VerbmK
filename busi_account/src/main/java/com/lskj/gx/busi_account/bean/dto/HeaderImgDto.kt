package com.lskj.gx.busi_account.bean.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 *   创建时间:  2021/1/14
 *   编写人: tzw
 *   功能描述: 用户头像
 */
data class HeaderImgDto(
    @SerializedName("imageUrl")
    val imageUrl: String?
) : Serializable