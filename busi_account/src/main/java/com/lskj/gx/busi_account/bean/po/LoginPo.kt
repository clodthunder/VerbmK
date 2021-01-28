package com.lskj.gx.busi_account.bean.po

/**
 *   创建时间:  2020/11/26
 *   编写人: tzw
 *   功能描述: 登录接口请求参数 json 格式
 */
data class LoginPo(
    val account: String,
    val password: String
)