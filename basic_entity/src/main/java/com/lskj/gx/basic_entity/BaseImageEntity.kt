package com.lskj.gx.basic_entity

import java.io.Serializable

/**
 *   创建时间:  2020/11/18
 *   编写人: tzw
 *   功能描述:基础图片对象封装
 */
data class BaseImageEntity(var id: String, var pid: String, var url: String) : Serializable