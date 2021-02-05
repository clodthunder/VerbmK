package com.lskj.gx.verbmk

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.lskj.gx.lib_common.base.activity.BaseActivity
import com.lskj.gx.lib_common.config.AroutConfig
import com.lskj.gx.verbmk.databinding.ActivityYbpBinding

/**
 *   创建时间:  2021/2/3
 *   仪表盘
 *   0- 10 测试没有问题 min>=0 max >=0
 *   -10 - 10
 *
 */
@Route(path = AroutConfig.A_APP_YBP)
class YbpActivity : BaseActivity() {
    //视图绑定
    private lateinit var bding: ActivityYbpBinding
    override fun firstBdLayout() {
        bding = DataBindingUtil.setContentView(this, R.layout.activity_ybp)
    }

    override fun afterBdInitParams() {

    }

    override fun afterPamsInitTbar() {

    }

    override fun afterTbarPreData(savedInstanceState: Bundle?) {

    }

    override fun afterPreData(savedInstanceState: Bundle?) {
        var maxFloat = 10f
        var minFloat = -10f
        var current = 0f
        //设置指针最终位置，附带动画效果
        bding.metervTest.setCurrentValue(current, maxFloat, minFloat)
        //
        bding.btnZb.setOnClickListener {
            //current
            if (bding.gcetNum.text != null) {
                val etCurrent = bding.gcetNum.text.toString()
                if (!TextUtils.isEmpty(etCurrent)) {
                    current = when {
                        etCurrent.toFloat() < minFloat -> {
                            minFloat
                        }
                        etCurrent.toFloat() > maxFloat -> {
                            maxFloat
                        }
                        else -> {
                            etCurrent.toFloat()
                        }
                    }
                }
            }
            //max
            if (bding.gcetMax.text == null) {
                Toast.makeText(this, "et max 不能null", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val etMax = bding.gcetMax.text
            if (TextUtils.isEmpty(etMax.toString())) {
                Toast.makeText(this, "输入 max", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            maxFloat = etMax.toString().toFloat()
            //min
            if (bding.gcetMin.text == null) {
                Toast.makeText(this, "et min 不能null", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val etMin = bding.gcetMin.text
            if (TextUtils.isEmpty(etMin.toString())) {
                Toast.makeText(this, "输入 min", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            minFloat = etMin.toString().toFloat()
            bding.metervTest.setCurrentValue(current, maxFloat, minFloat)
        }
    }
}