package com.lskj.gx.lib_common.utils

import android.content.Context
import com.lskj.gx.lib_common.config.AppContext

/**
 *   创建时间:  2021/1/5
 *   编写人: tzw
 *   功能描述:
 */
class ScreenUtil {
    companion object {
        /**
         * dp 2 px
         */
        fun dp2px(dpValue: Float): Int {
            val scale: Float = AppContext.instance.getContext().resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        fun px2dp(context: Context?, pxValue: Float): Int {
            val scale: Float = AppContext.instance.getContext().resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }

        /**
         * 将px值转换为sp值，保证文字大小不变
         * DisplayMetrics类中属性scaledDensity
         */
        fun px2sp(context: Context?, pxValue: Float): Int {
            val fontScale: Float =
                AppContext.instance.getContext().resources.displayMetrics.scaledDensity
            return (pxValue / fontScale + 0.5f).toInt()
        }

        /**
         * 将sp值转换为px值，保证文字大小不变
         * DisplayMetrics类中属性scaledDensity
         * TextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,MyApplication.getAppContext().getResources().getDimension(value))
         */
        fun sp2px(context: Context?, spValue: Float): Int {
            val fontScale: Float =
                AppContext.instance.getContext().resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }
    }
}