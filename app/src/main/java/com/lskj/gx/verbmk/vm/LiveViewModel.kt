package com.lskj.gx.verbmk.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *   创建时间:  2020/11/16
 *   编写人: tzw
 *   功能描述: 测试使用live view model
 */
class LiveViewModel : ViewModel() {

    private val lazyValue: MutableLiveData<List<String>> by lazy {
        MutableLiveData()
    }

    fun aaa() {
        val numberList = mutableListOf<Double>()
        numberList.also { println("Populating the list") }
            .apply {
                add(2.71)
                add(3.14)
                add(1.0)
            }
            .also { println("Sorting the list") }
            .sort()
    }

    private fun loadUsers() {

    }
}

