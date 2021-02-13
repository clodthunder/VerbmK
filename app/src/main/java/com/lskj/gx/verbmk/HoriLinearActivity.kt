package com.lskj.gx.verbmk

import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.lskj.gx.basic_entity.BaseImageEntity
import com.lskj.gx.lib_common.base.activity.BaseActivity
import com.lskj.gx.lib_common.config.AroutConfig
import com.lskj.gx.verbmk.databinding.ActivityHoriLinearBinding
import org.jetbrains.anko.toast
import java.lang.String
import java.util.*

/**
 *   创建时间:  2021/2/13
 *   横向显示 添加|删除 图片、音频、视频、附件
 */
@Route(path = AroutConfig.A_APP_HORI_LINEAR)
class HoriLinearActivity : BaseActivity() {
    private lateinit var bding: ActivityHoriLinearBinding
    private var images: ArrayList<BaseImageEntity> = arrayListOf()
    override fun firstBdLayout() {
        bding = DataBindingUtil.setContentView(this, R.layout.activity_hori_linear)
    }

    override fun afterBdInitParams() {

    }

    override fun afterPamsInitTbar() {

    }

    override fun afterTbarPreData(savedInstanceState: Bundle?) {

    }

    override fun afterPreData(savedInstanceState: Bundle?) {
        bding.hllTest.setBackGround(Color.BLUE)

        //实例化4张图片 用于测试
        val temp = BaseImageEntity(
            UUID.randomUUID().toString(),
            "pid1",
            String.valueOf(R.drawable.temp),
            "jpg"
        )
        val temp2 = BaseImageEntity(
            UUID.randomUUID().toString(),
            "pid2",
            String.valueOf(R.drawable.temp2),
            "jpg"
        )
        val temp3 = BaseImageEntity(
            UUID.randomUUID().toString(),
            "pid3",
            String.valueOf(R.drawable.temp3),
            "jpeg"
        )
        val temp4 = BaseImageEntity(
            UUID.randomUUID().toString(),
            "pid4",
            String.valueOf(R.drawable.temp4),
            "jpeg"
        )
        val temp5 = BaseImageEntity(
            UUID.randomUUID().toString(),
            "pid5",
            String.valueOf(R.drawable.temp),
            "jpg"
        )
        val temp6 = BaseImageEntity(
            UUID.randomUUID().toString(),
            "pid6",
            String.valueOf(R.drawable.temp4),
            "jpeg"
        )
        //实例化4张图片 用于测试
        images.add(temp)
        images.add(temp2)
        images.add(temp3)
        images.add(temp4)
        images.add(temp5)
        images.add(temp6)

        //要自己实现 点击、删除、新增
        bding.hllTest.setImages(images)
        //只有需要自己显示才用调用这个 否则不需要
        bding.hllTest.setOnHorIvDelClickListener { position, entity, iv ->
            toast("删除 $position-> ${entity.toString()}")
            //自己调用删除
            bding.hllTest.removeEntity(entity)
        }
        bding.hllTest.setOnHorIvAddClickListener { position, entity, iv ->
            toast("添加 事件")
        }
        bding.hllTest.setOnHorIvClickListener { position, entity, iv ->
            toast("点击 详情 $position->${entity.toString()}")
        }
        bding.btnAdd.setOnClickListener {
            val temp7 = BaseImageEntity(
                UUID.randomUUID().toString(),
                "add-pid6",
                String.valueOf(R.drawable.temp4),
                "jpeg"
            )
            bding.hllTest.addEntity(temp7);
        }
        bding.btnEdit.setOnClickListener {
            bding.hllTest.isEditModel = true
        }
        bding.btnUnEdit.setOnClickListener {
            bding.hllTest.isEditModel = false
        }
        bding.btnReset.setOnClickListener {
            bding.hllTest.setImages(images)
        }
    }
}