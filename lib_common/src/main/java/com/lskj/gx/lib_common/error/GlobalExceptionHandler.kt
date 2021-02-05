package com.lskj.gx.lib_common.error

import android.text.TextUtils
import android.util.Log
import com.google.gson.JsonSyntaxException
import com.lskj.gx.lib_common.BuildConfig
import java.net.ConnectException

/**
 *   创建时间:  2021/1/28
 *   统一错误出来
 */
class GlobalExceptionHandler : Thread.UncaughtExceptionHandler {
    private val TAG = "gxlog"
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null

    companion object {
        val instances: GlobalExceptionHandler by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            GlobalExceptionHandler()
        }
    }

    init {
        //获取系统默认的UnCatchException
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        //设置系统默认的UnCatchException 为GlobalException
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(mineHandler: Thread, throwable: Throwable) {
        if (!handleSystemException(throwable)) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            defaultHandler?.uncaughtException(mineHandler, throwable);
        } else {
            Log.d(TAG, "------获取异常 准备自己 处理");
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                Log.e(TAG, "error : ", e);
//            }
//            //退出程序
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(1);
        }
    }

    fun handleCustomerError(error: String?) {
        handleSystemException(BusinessException(error))
    }

    /**
     * 自定义错误处理方法
     */
    fun handleSystemException(throwable: Throwable): Boolean {
        throwable.let {
            //todo 保存错误信息到本地
            var msg = if (BuildConfig.DEBUG) it.message else it.stackTraceToString()
            if (it is ConnectException) {
                Log.e(TAG, "error ConnectException:$msg");
                return true
            }
            //Json格式错误
            if (it is JsonSyntaxException) {
                Log.e(TAG, "error JsonSyntaxException:$msg");
                return true
            }
            if (it is ConnectException) {
                Log.e(TAG, "error ConnectException:$msg");
                return true
            }
            if (it is MobileBusinessException) {
                val mobile: MobileBusinessException = it
                mobile.let { mobileEx ->
                    //打印当前登录人 请求时间
                    val errorCode = mobile.code
                    val errorMsg = if (TextUtils.isEmpty(mobile.msg)) {
                        ErrorCode.getByCode(mobile.code)
                    } else {
                        mobile.msg
                    }
                    errorCode.let {
                        //code 8001 退出到登录界面
                        Log.e(TAG, "error MobileBusinessException:$errorCode, msg=$errorMsg")
                    }
                }
                return true
            }
        }
        return false
    }
}