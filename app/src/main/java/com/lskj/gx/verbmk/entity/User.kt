package com.lskj.gx.verbmk.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

/**
 *   创建时间:  2020/11/13
 *   编写人: tzw
 *   功能描述: 用户类用于测试数据绑定库的使用方式
 */
class User : BaseObservable() {
    @get:Bindable
    var userPicUrl: String = ""
        set(url) {
            field = url
            notifyPropertyChanged(BR.userPicUrl)
        }

    @get:Bindable
//    @set:Bindable
    var username: String = ""
        set(un) {
            field = un
            notifyPropertyChanged(BR.username)
        }

    @get:Bindable
//    @set:Bindable
    var userPwd: String = ""
        set(up) {
            field = up
            notifyPropertyChanged(BR.userPwd)
        }

    @get:Bindable
//    @set:Bindable
    var isEnable: Boolean = false
        set(bo) {
            field = bo
            notifyPropertyChanged(BR.enable)
        }


    override fun toString(): String {
        return "$username,$userPwd,$isEnable"
    }
}