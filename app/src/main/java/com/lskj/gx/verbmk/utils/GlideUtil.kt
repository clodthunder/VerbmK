package com.lskj.gx.verbmk.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lskj.gx.basic_config.AppContext

/**
 *   创建时间:  2020/11/13
 *   编写人: tzw
 *   功能描述:
 */
class GlideUtil {


    companion object {
        val TAG: String = "GlideUtil"
        val instance: GlideUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GlideUtil()
        }

        @JvmStatic
        @BindingAdapter(value = ["gxImageUrl", "placeholder", "error"], requireAll = false)
        fun loadImageView(iv: ImageView, url: String, holder: Drawable, error: Drawable) {
            val ac: Context? = AppContext.instance.getContext();
            ac?.let {
                Glide.with(it).load(url)
                    .placeholder(holder)
                    .error(error).addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e(TAG, "onLoadFailed::url->$model")
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e(TAG, "onResourceReady")
                            return false
                        }
                    })
                    .into(iv)
            }
        }
    }
}