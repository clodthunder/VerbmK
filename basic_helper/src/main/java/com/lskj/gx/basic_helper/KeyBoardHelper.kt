package com.lskj.gx.basic_helper

import android.R
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.lskj.gx.basic_helper.intfaces.OnKeyBoardChangeListener


/**
 *   创建时间:  2020/11/20
 *   编写人: tzw
 *   功能描述: 软键盘 开启 关闭监听
 */
class KeyBoardHelper {
    private lateinit var keyBoardListener: OnKeyBoardChangeListener
    private var hasOPened: Boolean = false

    companion object {
        val instances: KeyBoardHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            KeyBoardHelper()
        }
    }

    /**
     * 设置软件盘开启关闭监听
     * 实现这个接口 OnKeyBoardChangeListener重写方法处理逻辑
     * 设置监听 KeyBoardHelper.instances.setKeyBoardHelper(this, this)
     */
    fun setKeyBoardHelper(act: Activity, keyListener: OnKeyBoardChangeListener) {
        this.keyBoardListener = keyListener
        val actRootView: View =
            (act.findViewById(R.id.content) as ViewGroup).getChildAt(0)
        actRootView.viewTreeObserver.addOnGlobalLayoutListener {
            val DefaultKeyboardDP: Int = 100
            val EstimatedKeyboardDP = DefaultKeyboardDP + 48
            val r = Rect()
            val estimatedKeyboardHeight = TypedValue
                .applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    EstimatedKeyboardDP.toFloat(),
                    actRootView.resources.displayMetrics
                )
                .toInt()
            actRootView.getWindowVisibleDisplayFrame(r)
            val heightDiff: Int =
                actRootView.rootView.height - (r.bottom - r.top)
            val isShown = heightDiff >= estimatedKeyboardHeight
            if (hasOPened != isShown) {
                hasOPened = isShown
                keyBoardListener.onKeyBoardShow(isShown)
            }
        }
    }

    /**
     * 隐藏或关闭软键盘
     * 如果当前是显示的状态 则 会关闭
     * 如果当前是隐藏的状态 则 会显示
     */
    fun showOrHiddenKeyBoard(act: Activity?) {
        act.let {
            val inputManager: InputMethodManager =
                act?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * 强制显示软键盘
     */
    fun showKeyBoradForced(act: Activity?) {
        act.let {
            val inputManager: InputMethodManager =
                act?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(act.window.decorView, InputMethodManager.SHOW_FORCED)
        }
    }

    /**
     * 强制关闭软键盘
     */
    fun hiddenKeyBoradForced(act: Activity?) {
        act.let {
            val inputManager: InputMethodManager =
                act?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                act.currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    fun isKeyBoradShowing(act: Activity?): Boolean {
        act.let {
            //获取当屏幕内容的高度
            val screenHeight: Int = act?.window?.decorView!!.height
            //获取View可见区域的bottom
            val rect = Rect()
            //DecorView即为activity的顶级view
            act.window.decorView.getWindowVisibleDisplayFrame(rect)
            //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
            //选取screenHeight*2/3进行判断
            //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
            //选取screenHeight*2/3进行判断
            return screenHeight * 2 / 3 > rect.bottom + getSoftButtonsBarHeight(act)
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun getSoftButtonsBarHeight(act: Activity?): Int {
        act.let {
            val metrics: DisplayMetrics = DisplayMetrics()
            //这个方法获取可能不是真实屏幕的高度
            act?.windowManager?.defaultDisplay?.getMetrics(metrics)
            val usableHeight: Int = metrics.heightPixels
            //获取当前屏幕的真实高度
            act?.windowManager?.defaultDisplay?.getRealMetrics(metrics);
            val realHeight: Int = metrics.heightPixels
            if (realHeight > usableHeight) {
                return realHeight - usableHeight;
            } else {
                return 0;
            }
        }
    }
}
