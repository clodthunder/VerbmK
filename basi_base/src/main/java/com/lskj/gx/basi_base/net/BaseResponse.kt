package com.lskj.gx.basi_base.net

import java.io.Serializable

/**
 *   创建时间:  2020/11/16
 *   编写人: tzw
 *   功能描述: 底层封装 response
 */
class BaseResponse<R : Serializable?> {
    var code = 0
    var message: String? = null
    var data: R? = null
        private set

    fun setData(data: R) {
        this.data = data
    }
}