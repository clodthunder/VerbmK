package com.lskj.gx.busi_account.error

import androidx.annotation.Nullable
import okhttp3.Request

/**
 *   创建时间:  2021/1/28
 *   自定义手机异常：方便手机端统一处理错误异常
 */
class MobileBusinessException(@Nullable errorCode: ErrorCode,@Nullable request: Request) :
    RuntimeException() {
    var errorCode: ErrorCode? = null
    var request: Request? = null

    init {
        this.errorCode = errorCode
        this.request =request
    }
}