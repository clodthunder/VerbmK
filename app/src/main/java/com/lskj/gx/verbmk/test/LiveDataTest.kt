package com.lskj.gx.verbmk.test

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.lskj.gx.verbmk.R
import com.lskj.gx.verbmk.databinding.ActivityLiveDataTestBinding
import com.lskj.gx.verbmk.viewmodel.UserViewModel

/**
 *   创建时间:  2020/11/16
 *   编写人: tzw
 *   功能描述: 测试livedata
 */
//@Route(path = LIVE_DATA_TEST)
class LiveDataTest : AppCompatActivity() {
    private lateinit var dbding: ActivityLiveDataTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbding = DataBindingUtil.setContentView(this, R.layout.activity_live_data_test)

        val model: UserViewModel by viewModels()
        model.getUsers().observe(this, Observer { it ->
            val uDatas: List<String> = it.map {
                it.name + "," + it.age
            }
            val sbStr: StringBuilder = StringBuilder()
            for (uData in uDatas) {
                sbStr.append(uData + "\n")
            }
            dbding.tvStrNotif.setText("users:\n$sbStr")
        })
        dbding.btnAdd.setOnClickListener {
            model.addUser()
        }
        dbding.btnRemove.setOnClickListener {
            model.removeUser()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}