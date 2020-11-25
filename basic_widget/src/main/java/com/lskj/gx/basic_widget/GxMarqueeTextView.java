package com.lskj.gx.basic_widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 创建时间:  2020/8/25
 * 编写人: tzw
 * 功能描述: 无线循环 MarqueeTextView
 */
public class GxMarqueeTextView extends AppCompatTextView {

    public GxMarqueeTextView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setGravity(Gravity.CENTER_VERTICAL);
        //防止view 各种原因停止
        setSingleLine(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setSelected(true);
        //无线循环
        setMarqueeRepeatLimit(-1);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
    }

    public GxMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public GxMarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
