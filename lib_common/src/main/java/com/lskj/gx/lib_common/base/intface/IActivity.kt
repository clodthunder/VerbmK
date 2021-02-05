package com.lskj.gx.lib_common.base.intface
import android.os.Bundle
import androidx.annotation.Nullable

/**
 *   创建时间:  2020/11/16
 *   编写人: tzw
 *   功能描述: base activity 封装组件方法
 */
interface IActivity {
    /**
     * 绑定layout
     */
    fun firstBdLayout()

    /**
     * 获取传入页面的数据
     */
    fun afterBdInitParams()

    /**
     * 初始化toolbar
     */
    fun afterPamsInitTbar()

    /**
     * 准备界面需要显示的数据 1
     */
    fun afterTbarPreData(savedInstanceState: Bundle?)

    /**
     * 初始化view
     */
    fun afterPreData(@Nullable savedInstanceState: Bundle?)
}