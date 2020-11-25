package com.lskj.gx.basic_entity

import java.io.Serializable

/**
 *   创建时间:  2020/11/16
 *   编写人: tzw
 *   功能描述: 基础用户类
 */
data class BaseUserEntity(
    var userId: String,
    var userName: String,
    var account: String,
    var pwd: String,
    var cert: String
) : Serializable