package com.lskj.gx.busi_account.popup

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import com.lskj.gx.busi_account.R
import com.lxj.xpopup.animator.PopupAnimator
import com.lxj.xpopup.core.CenterPopupView

/**
 *   创建时间:  2021/1/18
 */
@SuppressLint("InflateParams")
class LoadingPopup(context: Context) : CenterPopupView(context) {

    //自定义弹窗布局
    override
    fun getImplLayoutId(): Int {
        return R.layout.dialog_loading
    }

    override fun onCreate() {
        super.onCreate()
        val img: ImageView = popupContentView.findViewById(R.id.loadingImageView);
        val animationDrawable = img.background as AnimationDrawable
        animationDrawable.start()
    }

    override fun getMaxWidth(): Int {
        return super.getMaxWidth()
    }

    override fun getMaxHeight(): Int {
        return super.getMaxHeight()

    }

    override fun getPopupAnimator(): PopupAnimator {
        return super.getPopupAnimator()
    }

}