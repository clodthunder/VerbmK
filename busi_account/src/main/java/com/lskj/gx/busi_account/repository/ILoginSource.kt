package com.lskj.gx.busi_account.repository

/**
 *   创建时间:  2020/11/26
 *   编写人: tzw
 *   功能描述: 登录 相关的方法
 */
interface ILoginSource {

    /**
     * 网络请求获取用户信息
     */
    suspend fun login(name: String, pwd: String): Boolean

}