package com.lskj.gx.lib_common.base.listener

import android.view.View

/**
 *   创建时间:  2020/11/16
 *   编写人: tzw
 *   功能描述: 自定义点击事件防止多次点击
 */
abstract class BaseOnClickListener : View.OnClickListener {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private val MIN_CLICK_DELAY_TIME = 1500
    private var lastClickTime: Long = 0

    override fun onClick(v: View?) {
        val curClickTime = System.currentTimeMillis()
        if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime
            onMultiClick(v)
        }
    }

    /**
     * 对外暴露的点击方法
     */
    abstract fun onMultiClick(v: View?)
}