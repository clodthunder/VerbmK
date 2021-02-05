package com.lskj.gx.lib_common.error

/**
 *   创建时间:  2021/1/28
 *   自定义手机异常：方便手机端统一处理错误异常
 */
class MobileBusinessException(code: Int, msg: String?) :
    RuntimeException() {
    var code: Int = 0
    var msg: String? = null


    init {
        this.code = code
        this.msg = msg
    }

    override fun toString(): String {
        return "MobileBusinessException(code=$code,msg=$msg)"
    }
}