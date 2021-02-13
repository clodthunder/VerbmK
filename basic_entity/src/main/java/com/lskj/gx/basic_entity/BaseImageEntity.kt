package com.lskj.gx.basic_entity

import java.io.Serializable

/**
 *   创建时间:  2020/11/18
 *   编写人: tzw
 *   id
 *   pid 关联的记录id
 *   url
 *   suffix 后缀用于判断图片类型
 *   功能描述:基础图片对象封装
 */
data class BaseImageEntity(var id: String, var pid: String, var url: String, var suffix: String) :
    Serializable