package com.lskj.gx.verbmk.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/**
 *   创建时间:  2020/11/16
 *   编写人: tzw
 *   功能描述: glide 图片加载库配置
 */
@GlideModule
class VerbmGlideModel : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
    }
}