package com.lskj.gx.basic_helper

import android.app.Activity
import android.util.Log

/**
 *   创建时间:  2020/11/13
 *   编写人: tzw
 *   功能描述: 管理 activity
 */
class ActivityStackHelper {
    private val TAG: String = this.javaClass.simpleName

    //用于存储activity 这里不使用栈
    private lateinit var acList: MutableList<Activity>

    companion object {
        val instance: ActivityStackHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityStackHelper()
        }
    }

    fun init() {
        acList = mutableListOf()
        printAcList()
    }

    private fun printAcList() {
        val acNames: List<String> = if (acList.isNullOrEmpty().not()) {
            acList.map { it.packageName + it.javaClass.simpleName }
        } else {
            mutableListOf()
        }
        var sbStr = StringBuilder()
        if (acNames.isNotEmpty()) {
            sbStr.append("${TAG}-acList当前包含【\n")
            acNames.forEach { cStr ->
                sbStr.append(cStr + "\n")
            }
            sbStr.append("】\n");
        } else {
            sbStr.append("没有数据 \n");
        }
        Log.d(TAG, "$TAG 当前acList-> ${sbStr}")
    }

    fun addActivity(ac: Activity) {
        acList.add(ac);
        printAcList()
    }

    fun addActivities(vararg acs: Activity) {
        acList.addAll(acs)
        printAcList()
    }

    fun addActvities(acList: MutableList<Activity>) {
        acList.addAll(acList)
        printAcList()
    }

    fun removeAll() {
        acList.clear()
        printAcList()
    }

    /**
     * 移除某个activity
     */
    fun removeActivity(ac: Activity): Boolean {
        if (ac in acList) {
            val result = acList.remove(ac)
            printAcList()
            return result
        }
        return false
    }

}