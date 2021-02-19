package com.lskj.gx.basic_entity

import java.io.Serializable

/**
 *   创建时间:  2020/11/18
 *   编写人: tzw
 *   id
 *   pid 关联的记录id
 *   cover 封面图片 支持url uri drawable drawableId
 *   source 真实文件
 *   sourceSuffix  真实文件后缀 png|mp3|mp4 more
 *   duration 音频、视频文件播放时长
 *   功能描述:基础图片对象封装
 */
data class BaseImageEntity(
    var id: String,
    var pid: String,
    var cover: Any,
    var source: Any,
    var sourceSuffix: String,
    var duration: Int
) :
    Serializable