package com.lskj.gx.busi_account

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.lskj.gx.lib_common.R
import com.lskj.gx.lib_common.utils.ScreenUtil


/**
 *   创建时间:  2021/1/5
 *   编写人: tzw
 *   功能描述:
 */
class SingleLoadingUtil {
    companion object {
        private var loading: AlertDialog? = null
        private var view: View? = null
        private var imageView: ImageView? = null

        val instance: SingleLoadingUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SingleLoadingUtil()
        }
    }

    @SuppressLint("RestrictedApi")
    fun showLoading(activity: Activity) {
        if (loading == null || view == null) {
            view = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_loading, null)
            imageView = view!!.findViewById(R.id.loadingImageView)
            val animationDrawable = imageView!!.background as AnimationDrawable
            animationDrawable.start()
            loading = AlertDialog.Builder(activity)
                .setView(view)
                .setCancelable(true)
                .create()
            loading!!.setCanceledOnTouchOutside(false)
            loading!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            loading!!.window!!.setBackgroundDrawableResource(R.drawable.dialog_white_rect_10)
        }
        if (!loading!!.isShowing) {
            loading!!.show()
            loading!!.window!!.setLayout(ScreenUtil.dp2px(100f), ScreenUtil.dp2px(100f))
        }
    }

    fun dissLoading() {
        if (loading != null && loading!!.isShowing) {
            loading!!.dismiss()
        }
    }
}

private fun Resources.getDrawable() {
    TODO("Not yet implemented")
}
