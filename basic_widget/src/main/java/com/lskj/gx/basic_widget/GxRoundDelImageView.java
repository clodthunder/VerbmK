package com.lskj.gx.basic_widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lskj.gx.basic_entity.BaseImageEntity;
import com.lskj.gx.lib_common.base.listener.BaseOnClickListener;


/**
 * 创建时间:  2020/9/3
 * 编写人: tzw
 * 功能描述: 包含删除 点击 事件
 */
public class GxRoundDelImageView extends RelativeLayout {
    private Context mContext;
    //image 实体类
    private BaseImageEntity imgEntity;
    //真实图片
    private GxRoundImgView realImage;
    //删除图片
    private GxRoundImgView delImage;

    //真实图片的大小
    private int realHeight;
    private int realWidth;

    //删除图片的大小
    private int delHeight;
    private int delWidth;

    //点击事件
    private OnRealImageClick realListener;
    //删除事件
    private OnDelImageClick delListener;

    //自定义渲染图片方法
    private onDrawRealImageListener drawRealImageListener;

    public GxRoundImgView getRealImage() {
        return realImage;
    }

    public GxRoundImgView getDelImage() {
        return delImage;
    }

    /**
     * 设置delImage 是否隐藏
     *
     * @param visible true 显示 false 隐藏
     */
    public void setDelImgVisible(boolean visible) {
        if (delImage != null) {
            if (visible) {
                if (delImage.getVisibility() != VISIBLE) {
                    delImage.setVisibility(VISIBLE);
                }
            } else {
                if (delImage.getVisibility() != GONE) {
                    delImage.setVisibility(GONE);
                }
            }
        }
    }

    public GxRoundDelImageView(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public int dp2px(int dpValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public GxRoundDelImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public GxRoundDelImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        this.setClickable(false);
        this.setFocusable(false);

        realHeight = dp2px(60);
        realWidth = dp2px(60);
        delHeight = dp2px(15);
        delWidth = dp2px(15);

        @SuppressLint("CustomViewStyleable") TypedArray a =
                mContext.obtainStyledAttributes(attrs, R.styleable.GxRoundImgView);
        realHeight = a.getDimensionPixelSize(R.styleable.GxRoundWithDelete_real_height, realHeight);
        realWidth = a.getDimensionPixelSize(R.styleable.GxRoundWithDelete_real_width, realWidth);
        delHeight = a.getDimensionPixelSize(R.styleable.GxRoundWithDelete_del_height, delHeight);
        delWidth = a.getDimensionPixelSize(R.styleable.GxRoundWithDelete_del_width, delWidth);
        a.recycle();
        if (imgEntity != null) {
            drawImg();
        }
    }

    private void drawImg() {
        //真实图片
        realImage = new GxRoundImgView(mContext);
        realImage.setClickable(true);
        realImage.setFocusableInTouchMode(false);
        realImage.setId(R.id.round_with_delete_real_image);
        realImage.setScaleType(ImageView.ScaleType.FIT_XY);
        LayoutParams layoutParams = new LayoutParams(realWidth, realHeight);
        if (drawRealImageListener != null) {
            drawRealImageListener.onDrawRealImage(realImage);
        }
        realImage.setLayoutParams(layoutParams);
        this.addView(realImage);
        //删除图片
        delImage = new GxRoundImgView(mContext);
        delImage.setClickable(true);
        delImage.setFocusableInTouchMode(false);
        delImage.setId(R.id.round_with_delete_del_image);
        LayoutParams delParams = new LayoutParams(delWidth, delHeight);
        delImage.setScaleType(ImageView.ScaleType.FIT_XY);
        delImage.setLayoutParams(delParams);
        delImage.setImageDrawable(getResources().getDrawable(R.drawable.default_delete_icon));
        this.addView(delImage);

        LayoutParams delParentParams = (LayoutParams) delImage.getLayoutParams();
        delParentParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        delParentParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        delImage.setLayoutParams(delParentParams);
    }

    /**
     * 这里默认 del 有上角图标跟随 realImage 右上角图标
     */
    public void setRaduis(int lt, int rt, int lb, int rb) {
        if (realImage != null) {
            realImage.setRaduis(lt, rt, lb, rb);
        }
        if (delImage != null) {
            delImage.setRaduis(0, rt, 0, 0);
        }
    }

    public void setScaleRealType(ImageView.ScaleType scaleType) {
        if (realImage != null) {
            realImage.setScaleType(scaleType);
        }
    }

    public interface onDrawRealImageListener {
        void onDrawRealImage(GxRoundImgView gxImgView);
    }

    public interface OnRealImageClick {
        void onRealImgClick(GxRoundImgView realImg, BaseImageEntity imageEntity);
    }

    public interface OnDelImageClick {
        void onDelImgClick(GxRoundImgView delImg, BaseImageEntity entity);
    }

    public BaseImageEntity getImgEntity() {
        return imgEntity;
    }

    public void setImgEntity(BaseImageEntity imgEntity, onDrawRealImageListener drawReal) {
        this.imgEntity = imgEntity;
        this.drawRealImageListener = drawReal;
        drawImg();
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    public void setRealListener(OnRealImageClick realListener) {
        this.realListener = realListener;
        if (realImage != null) {
            realImage.setOnClickListener(new BaseOnClickListener() {
                @Override
                public void onMultiClick(View v) {
                    if (realListener != null) {
                        realListener.onRealImgClick(realImage, imgEntity);
                    }
                }
            });
        }
    }

    public void setDelListener(OnDelImageClick delListener) {
        this.delListener = delListener;
        if (delImage != null) {
            delImage.setOnClickListener(new BaseOnClickListener() {
                @Override
                public void onMultiClick(View v) {
                    if (delListener != null) {
                        delListener.onDelImgClick(delImage, imgEntity);
                    }
                }
            });
        }
    }
}
