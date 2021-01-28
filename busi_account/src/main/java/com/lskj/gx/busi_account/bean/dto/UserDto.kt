package com.lskj.gx.busi_account.bean.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 *   创建时间:  2021/1/14
 *   编写人: tzw
 *   功能描述: 用户信息传输对象
 */
data class UserDto(
    @SerializedName("account")
    val account: String?, // WZJWEqxmtiVvtUIAVQJZ18TgWuBjx07M
    @SerializedName("age")
    val age: Any?, // null
    @SerializedName("email")
    val email: Any?, // null
    @SerializedName("enable")
    val enable: Boolean?, // true
    @SerializedName("id")
    val id: Int?, // 8
    @SerializedName("lastActiveDate")
    val lastActiveDate: String?, // 2021-01-14 13:44:39
    @SerializedName("modifyDate")
    val modifyDate: Any?, // null
    @SerializedName("name")
    val name: String?, // temp7
    @SerializedName("password")
    val password: String?, // E10ADC3949BA59ABBE56E057F20F883E
    @SerializedName("phone")
    val phone: Any?, // null
    @SerializedName("registerDate")
    val registerDate: String?, // 2021-01-14 09:35:49
    @SerializedName("salt")
    val salt: Any?, // null
    @SerializedName("state")
    val state: Boolean? // true
) : Serializable

