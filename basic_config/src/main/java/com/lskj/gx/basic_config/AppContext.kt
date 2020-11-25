package com.lskj.gx.basic_config

import android.app.Application
import android.content.Context

/**
 *   创建时间:  2020/11/11
 *   编写人: tzw
 *   功能描述:
 */
class AppContext {
    private lateinit var context: Context
    private lateinit var app: Application

    companion object {
        val instance: AppContext by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppContext()
        }
    }

    fun init(c: Application) {
        context = c.applicationContext
        app = c
    }

    fun getContext(): Context {
        if (!this::context.isInitialized) {
            throw RuntimeException("AppContext context 尚未初始化--!");
        }
        return context
    }
}