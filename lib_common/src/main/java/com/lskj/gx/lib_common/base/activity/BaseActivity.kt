package com.lskj.gx.lib_common.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lskj.gx.lib_common.base.intface.IActivity

/**
 *   创建时间:  2020/11/16
 *   编写人: tzw
 *   功能描述: base activity 提取activity 公共方法
 */
abstract class BaseActivity : AppCompatActivity(), IActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firstBdLayout()
        afterBdInitParams()
        afterPamsInitTbar()
        afterTbarPreData(savedInstanceState)
        afterPreData(savedInstanceState)
    }

    abstract override fun firstBdLayout()
    abstract override fun afterBdInitParams()
    abstract override fun afterPamsInitTbar()
    abstract override fun afterTbarPreData(savedInstanceState: Bundle?)
    abstract override fun afterPreData(savedInstanceState: Bundle?)
}