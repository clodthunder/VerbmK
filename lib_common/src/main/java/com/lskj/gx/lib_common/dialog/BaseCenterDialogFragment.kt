package com.lskj.gx.lib_common.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.DialogFragment
import com.lskj.gx.lib_common.R
import com.lskj.gx.lib_common.ScreenHelper
import org.jetbrains.anko.windowManager

/**
 *   创建时间:  2021/2/14
 *   编写人: tzw
 *   功能描述:
 */
abstract class BaseCenterDialogFragment : DialogFragment() {
    protected lateinit var mContxt: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContxt = context
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        var display: Display? = null
        var windowManager: WindowManager? = null
        var window: Window? = null
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            val dm = DisplayMetrics()
            windowManager = mContxt.windowManager
            window = dialog.window
            if (window != null) {
                display = windowManager.defaultDisplay
                display.getMetrics(dm)
                val width = display.width
                val lp = window.attributes
                lp.gravity = Gravity.CENTER
                //左右 间隔 60dp距离
                lp.width = width - ScreenHelper.instances.dp2px(60f)
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                window.attributes = lp
                window.setGravity(Gravity.CENTER)
                window.setBackgroundDrawableResource(R.drawable.bg_white_corners_10)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window = dialog!!.window
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.CENTER)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}