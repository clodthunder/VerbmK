package com.lskj.gx.lib_common.config

import android.text.TextUtils
import com.lskj.gx.lib_common.utils.SpUtil

/**
 *   创建时间:  2021/1/5
 *   编写人: tzw
 *   功能描述:
 */
class ApiConstant {

    companion object {
        //默认的接口请求url
        const val DEFAULT_BASE_URL: String = "http://144.34.189.222"
//        const val DEFAULT_BASE_URL: String = "http://192.168.1.98"
//        const val DEFAULT_DEBUGE_URL: String = "http://192.168.1.98"

        //默认的接口请求port
        const val DEFAULT_BASE_PORT: Int = 8080

        val instance: ApiConstant by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiConstant()
        }

        /**
         * 获取base Url
         */
        fun getBaseUrl(): String {
            var baseUrl: String = SpUtil.SharedPreferencesUtils.DEFAULT_BASE_URL
            var port: Int = SpUtil.SharedPreferencesUtils.DEFAULT_BASE_PORT
            if (TextUtils.isEmpty(baseUrl)) {
                baseUrl = DEFAULT_BASE_URL
            }
            if (port == 0) {
                port = DEFAULT_BASE_PORT
            }
            return "$baseUrl:$port"
        }

        /**
         * 更新base Url
         */
        fun setBaseUrlPrex(preStr: String, port: Int): Boolean {
            if (TextUtils.isEmpty(preStr) || port <= 0) {
                return false
            }
            SpUtil.SharedPreferencesUtils.DEFAULT_BASE_URL = preStr
            SpUtil.SharedPreferencesUtils.DEFAULT_BASE_PORT = port
            return true
        }
    }
}