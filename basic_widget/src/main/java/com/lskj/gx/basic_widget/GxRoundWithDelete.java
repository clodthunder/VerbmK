package com.lskj.gx.basic_widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lskj.gx.basic_entity.BaseUserEntity;
import com.lskj.gx.lib_common.base.listener.BaseOnClickListener;


/**
 * 创建时间:  2020/9/3
 * 编写人: tzw
 * 功能描述:
 * gxRoundWithDelete.setRaduis(20, 20, 10, 10);
 * //设置imageView entity
 * gxRoundWithDelete.setImgEntity(
 * new BaseUserEntity(UUID.randomUUID().toString(), null, String.valueOf(R.drawable.temp)));
 * gxRoundWithDelete.setGxRivdListener(new GxRoundWithDelete.OnRealImageClick() {
 *
 * @Override public void onRealImgClick(GxRoundImgView realImg, BaseUserEntity entity) {
 * //设置
 * GxGlide.loadFitCenter(ImageDelete.this, R.drawable.test_ui_temp2, gxRoundWithDelete.getRealImage());
 * Toast.makeText(ImageDelete.this, entity == null ? "请设置 entity" : entity.toString(), Toast.LENGTH_SHORT).show();
 * }
 * @Override public void onDelImgClick(GxRoundImgView delImg) {
 * Toast.makeText(ImageDelete.this, "delete 触发", Toast.LENGTH_SHORT).show();
 * }
 * });
 * GxGlide.load(this, R.drawable.test_ui_temp2, gxRoundWithDelete.getRealImage());
 */
public class GxRoundWithDelete extends RelativeLayout {
    private Context mContext;
    //image 实体类
    private BaseUserEntity imgEntity;

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

    private OnRealImageClick realListener;

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

    public GxRoundWithDelete(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public int dp2px(int dpValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public GxRoundWithDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public GxRoundWithDelete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        this.setClickable(false);
        this.setFocusable(false);

        realHeight = dp2px(80);
        realWidth = dp2px(80);
        delHeight = dp2px(30);
        delWidth = dp2px(30);

        @SuppressLint("CustomViewStyleable") TypedArray a =
                mContext.obtainStyledAttributes(attrs, R.styleable.GxRoundImgView);
        realHeight = a.getDimensionPixelSize(R.styleable.GxRoundWithDelete_real_height, realHeight);
        realWidth = a.getDimensionPixelSize(R.styleable.GxRoundWithDelete_real_width, realWidth);
        delHeight = a.getDimensionPixelSize(R.styleable.GxRoundWithDelete_del_height, delHeight);
        delWidth = a.getDimensionPixelSize(R.styleable.GxRoundWithDelete_del_width, delWidth);
        a.recycle();
        //真实图片
        realImage = new GxRoundImgView(mContext);
        realImage.setClickable(true);
        realImage.setFocusableInTouchMode(false);
        realImage.setId(R.id.round_with_delete_real_image);
        realImage.setScaleType(ImageView.ScaleType.FIT_XY);
        LayoutParams layoutParams = new LayoutParams(realWidth, realHeight);
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
        delImage.setImageDrawable(getResources().getDrawable(R.drawable.temp));

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

    public void setGxRivdListener(OnRealImageClick realListener) {
        this.realListener = realListener;
        if (realImage != null) {
            realImage.setOnClickListener(new BaseOnClickListener() {
                @Override
                public void onMultiClick(View v) {
                    realListener.onRealImgClick(realImage, imgEntity);
                }
            });
        }
        if (delImage != null) {
            delImage.setOnClickListener(new BaseOnClickListener() {
                @Override
                public void onMultiClick(View v) {
                    realListener.onDelImgClick(delImage);
                }
            });
        }
    }

    public void setScaleRealType(ImageView.ScaleType scaleType) {
        if (realImage != null) {
            realImage.setScaleType(scaleType);
        }
    }

    public interface OnRealImageClick {
        void onRealImgClick(GxRoundImgView realImg, BaseUserEntity imageEntity);

        void onDelImgClick(GxRoundImgView delImg);
    }

    public BaseUserEntity getImgEntity() {
        return imgEntity;
    }

    public void setImgEntity(BaseUserEntity imgEntity) {
        this.imgEntity = imgEntity;
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }
}
