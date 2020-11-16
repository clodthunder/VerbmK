package com.lskj.gx.verbmk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lskj.gx.verbmk.entity.UserLiveData
import java.util.*

/**
 *   创建时间:  2020/11/16
 *   编写人: tzw
 *   功能描述: 处理user live data 数据
 */
class UserViewModel : ViewModel() {
    private val userVms: MutableLiveData<List<UserLiveData>> = MutableLiveData<List<UserLiveData>>()
    fun getUsers(): LiveData<List<UserLiveData>> {
        userVms.value = loadUser()
        return userVms;
    }

    fun addUser() {
        val multi: MutableList<UserLiveData> = userVms.value as MutableList<UserLiveData>
        multi.add(UserLiveData("new_" + UUID.randomUUID(), 124))
        userVms.value = multi
    }

    fun removeUser() {
        val multi: MutableList<UserLiveData> = userVms.value as MutableList<UserLiveData>
        val index: Int = multi.size - 1
        if (index < 0) {
            return
        }
        multi.removeAt(index)
        userVms.value = multi
    }

    //获取用户方法
    private fun loadUser(): MutableList<UserLiveData> {
        val userList: MutableList<UserLiveData> = mutableListOf()
        userList.add(UserLiveData("tzw", 12))
        userList.add(UserLiveData("wzy", 14))
        return userList;
    }
}