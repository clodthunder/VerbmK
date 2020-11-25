package com.lskj.gx.basic_helper

import android.content.Context
import android.util.Log
import com.lskj.gx.basic_config.AppContext
import java.lang.reflect.Field

/**
 *   创建时间:  2020/11/25
 *   编写人: tzw
 *   功能描述:屏幕分辨率相关方法
 */
class ScreenHelper {
    companion object {
        val instances: ScreenHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ScreenHelper()
        }
    }

    /**
     * dp2px
     */
    fun dp2px(dp: Float): Int {
        val scale: Float =
            AppContext.instance.getContext()
                .resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt();
    }

    fun px2dp(px: Float): Int {
        val scale: Float =
            AppContext.instance.getContext()
                .resources.displayMetrics.scaledDensity
        return (px / scale + 0.5f).toInt();
    }

    fun getStatusBarHeight(context: Context): Int {
        var c: Class<*>? = null
        var obj: Any? = null
        var field: Field? = null
        var x = 0
        var statusBarHeight = 0
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c.newInstance()
            field = c.getField("status_bar_height")
            x = field[obj].toString().toInt()
            statusBarHeight = context.resources.getDimensionPixelSize(x)
            Log.v("@@@@@@", "the status bar height is : $statusBarHeight")
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return statusBarHeight
    }
}