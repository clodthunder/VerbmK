package com.lskj.gx.busi_account.error

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.lskj.gx.busi_account.BuildConfig
import okhttp3.Request
import java.net.ConnectException
import kotlin.system.exitProcess

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
        if (!handleException(throwable)) {
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

    /**
     * 自定义错误处理方法
     */
    fun handleException(throwable: Throwable): Boolean {
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
            if (it is MobileBusinessException) {
                val mobile: MobileBusinessException = it
                mobile.let { mobileEx ->
                    //打印当前登录人 请求时间

                    //打印请求url 和请求参数
                    val request: Request? = mobileEx.request
                    request.let { reqst ->
                        val method = request?.method
                        Log.e(TAG, "------headers start-------\n")
                        reqst?.headers?.forEach { pair ->
                            Log.e(TAG, "--key:" + pair.first + "--value:" + pair.second)
                        }
                        Log.e(TAG, "------headers end-------\n")
                    }
                    val errorCode = mobile.errorCode
                    errorCode.let {
                        if (errorCode != null) {
                            // code 8001 退出到登录界面
                            if (errorCode.ordinal == 8001) {
                                Log.e(TAG, "error MobileBusinessException:" + errorCode.getMsg())

                            }

                        } else {
                            Log.e(TAG, "error ConnectException:$msg");
                        }
                        if (request == null) {
                            return true
                        }
                    }
                }
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            exitProcess(1);
        }
        return false
    }
}