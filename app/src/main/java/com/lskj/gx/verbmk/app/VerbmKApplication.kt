package com.lskj.gx.verbmk.app

import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.lskj.gx.lib_common.ActivityStackHelper
import com.lskj.gx.lib_common.config.AppContext
import com.lskj.gx.verbmk.BuildConfig

/**
 *   创建时间:  2020/11/11
 *   编写人: tzw
 *   功能描述: 项目初始化
 */
class VerbmKApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        //初始化Application
        AppContext.instance.init(this);
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        //ARouter初始化
        ARouter.init(this)
        //初始化ActvityStackHelper
        ActivityStackHelper.instance.init()
    }

}