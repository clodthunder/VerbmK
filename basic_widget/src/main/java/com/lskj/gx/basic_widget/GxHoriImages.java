package com.lskj.gx.basic_widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.ColorInt;

import com.lskj.gx.basic_entity.BaseImageEntity;
import com.lskj.gx.lib_common.base.listener.BaseOnClickListener;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 创建时间:  2020/9/3
 * 编写人: tzw
 * 功能描述: 封装横向 imageView list
 * 支持显示  隐藏 del imageView
 * 支持显示、隐藏 添加按钮
 * 2种模式：1 编辑模式 2 查看模式
 */
public class GxHoriImages extends HorizontalScrollView {
    private Context mContext;
    private LinearLayout llContainer;
    //显示image  entity array
    private ArrayList<BaseImageEntity> images;

    //item realImage height
    private int realHeight;
    //item realImage width
    private int realWidth;
    //是否显示start space
    private boolean showStarSpace = true;
    //是否显示end space
    private boolean showEndSpace = true;
    //是否显示中间分割线
    private boolean showMiddleSpace = true;

    private OnHorIvClickListener mHorIvClickListener;

    public void setHorIvClickListener(OnHorIvClickListener horIvClickListener) {
        mHorIvClickListener = horIvClickListener;
    }

    public GxHoriImages(Context context) {
        super(context);
        init(context, null);
    }

    public GxHoriImages(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GxHoriImages(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        realHeight = dp2px(80);
        realWidth = dp2px(80);
        images = new ArrayList<>();
        //实例化4张图片 用于测试

        BaseImageEntity temp = new BaseImageEntity(UUID.randomUUID().toString(), null, String.valueOf(R.drawable.temp));
        BaseImageEntity temp2 = new BaseImageEntity(UUID.randomUUID().toString(), null, String.valueOf(R.drawable.temp2));
        BaseImageEntity temp3 = new BaseImageEntity(UUID.randomUUID().toString(), null, String.valueOf(R.drawable.temp3));
        BaseImageEntity temp4 = new BaseImageEntity(UUID.randomUUID().toString(), null, String.valueOf(R.drawable.temp4));
        BaseImageEntity temp5 = new BaseImageEntity(UUID.randomUUID().toString(), null, String.valueOf(R.drawable.temp));

        images.add(temp);
        images.add(temp2);
        images.add(temp3);
        images.add(temp4);
        images.add(temp5);

        //默认填充
        setFillViewport(true);
        llContainer = new LinearLayout(mContext);
        llContainer.setOrientation(LinearLayout.HORIZONTAL);
        llContainer.setBackgroundColor(Color.RED);
        LinearLayout.LayoutParams llParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llContainer.setLayoutParams(llParams);
        llContainer.setClickable(false);
        llContainer.setFocusableInTouchMode(false);
        addView(llContainer);
        setBackGround(Color.WHITE);

        setHorIvClickListener(new OnHorIvClickListener() {
            @Override
            public void OnHorIvClick(int position, BaseImageEntity entity, GxRoundImgView iv) {
                Toast.makeText(context, position + "--" + entity != null ? entity.toString() : "entity is null",
                        Toast.LENGTH_SHORT).show();
            }
        });

        for (int i = 0; i < images.size(); i++) {
            //处理start end space
            if ((showStarSpace && i == 0) || (showEndSpace && i == images.size() - 1)) {
                llContainer.addView(createSpace());
            }
            if (showMiddleSpace && i != 0 && i != images.size() - 1) {
                llContainer.addView(createSpace());
            }
            //这里处理 分割问题
            llContainer.addView(createImgView(i, images.get(i)));
        }
    }

    private ImageView createSpace() {
        ImageView view = new ImageView(mContext);
        view.setClickable(false);
        view.setFocusableInTouchMode(false);
        view.setLayoutParams(new LayoutParams(dp2px(10), getMeasuredHeight()));
        view.setImageDrawable(getResources().getDrawable(R.drawable.white_space));
        return view;
    }

    private GxRoundImgView createImgView(int i, BaseImageEntity entity) {
        GxRoundImgView temp = new GxRoundImgView(mContext);
        temp.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams tempParams = new LinearLayout.LayoutParams(realWidth, realHeight);
        temp.setImageDrawable(getResources().getDrawable(R.drawable.temp));
        temp.setLayoutParams(tempParams);
        temp.setClickable(true);
        temp.setFocusableInTouchMode(false);
        temp.setImageDrawable(getResources().getDrawable(Integer.parseInt(entity.getUrl())));
        temp.setOnClickListener(new BaseOnClickListener() {
            @Override
            public void onMultiClick(View v) {
                new HorIvItemClick(i, temp, entity);
            }
        });
        return temp;
    }

    /**
     * 这里需要保持hor 和 ll 背景色一致 ll 可能没有横向填充导致效果难看
     */
    public void setBackGround(@ColorInt int color) {
        setBackgroundColor(color);
        if (llContainer != null) {
            llContainer.setBackgroundColor(color);
        }
    }

    public int dp2px(int dpValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    public class HorIvItemClick implements OnClickListener {
        int position;
        GxRoundImgView iv;
        BaseImageEntity entity;

        public HorIvItemClick(int position, GxRoundImgView iv, BaseImageEntity entity) {
            this.position = position;
            this.iv = iv;
            this.entity = entity;
        }

        @Override
        public void onClick(View v) {
            if (mHorIvClickListener != null) {
                mHorIvClickListener.OnHorIvClick(position, entity, iv);
            }
        }
    }

    public interface OnHorIvClickListener {
        public void OnHorIvClick(int position, BaseImageEntity entity, GxRoundImgView iv);
    }
}
