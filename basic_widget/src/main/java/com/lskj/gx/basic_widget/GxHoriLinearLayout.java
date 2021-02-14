package com.lskj.gx.basic_widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;

import com.bumptech.glide.Glide;
import com.lskj.gx.basic_entity.BaseImageEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 创建时间:  2020/9/3
 * 编写人: tzw
 * 功能描述: 封装横向 imageView list
 * 2种模式：1 编辑模式 2 查看模式
 * 支持自定义渲染图片
 * 支持自定义事件处理
 * 支持动态更新某条记录
 */
public class GxHoriLinearLayout extends HorizontalScrollView {
    private Context mContext;
    private LinearLayout llContainer;
    //显示image  entity array

    //这里需要保证enities 和 gxImageViews下标一致
    private ArrayList<BaseImageEntity> entities;
    private ArrayList<GxRoundDelImageView> gxImgViews;

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
    //判断是否显示编辑模式
    private boolean isEditModel = true;
    //添加按钮
    private BaseImageEntity addBtn;
    private GxRoundDelImageView addBtnView;
    //当前是否显示addBtn
    private boolean hasAddBtn = false;
    private final String ADD_FLAG = "add_btn";

    private OnHorIvClickListener onHorIvClickListener;
    private OnHorIvDelClickListener onHorIvDelClickListener;
    private OnHorIvAddClickListener onHorIvAddClickListener;

    //自定义图片渲染
    @SuppressLint("UseCompatLoadingForDrawables")
    private GxRoundDelImageView.onDrawRealImageListener onDrawRealImageListener =
            (gxImgView, entity) -> {
                //特殊处理addBtn
                if (ADD_FLAG.equals(entity.getPid())) {
                    Glide.with(mContext).load(entity.getUrl()).into(gxImgView);
//                    gxImgView.setImageDrawable(getResources().getDrawable(Integer.parseInt(entity.getUrl())));
                } else {
                    Glide.with(mContext).load(entity.getUrl()).into(gxImgView);
//                    //正常view  处理显示本地图片 和处理网络图片
//                    gxImgView.setImageDrawable(getResources().getDrawable(Integer.parseInt(entity.getUrl())));
                }
            };

    public GxHoriLinearLayout(Context context) {
        super(context);
        init(context, null);
    }

    public GxHoriLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GxHoriLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        realHeight = dp2px(60);
        realWidth = dp2px(60);
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

        entities = new ArrayList<>();
        gxImgViews = new ArrayList<>();
        addBtn = new BaseImageEntity(UUID.randomUUID().toString(), ADD_FLAG, R.drawable.add_photo, "png");
        //添加按钮
        if (isEditModel) {
            addbtnView();
        }
    }

    private ImageView createSpace() {
        ImageView view = new ImageView(mContext);
        view.setClickable(false);
        view.setFocusableInTouchMode(false);
        view.setLayoutParams(new LayoutParams(dp2px(5), getMeasuredHeight()));
        Drawable bg = llContainer.getBackground();
        if (bg == null) {
            bg = getResources().getDrawable(R.drawable.white_space);
        }
        view.setImageDrawable(bg);
        return view;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private GxRoundDelImageView createImgView(int i, BaseImageEntity entity) {
        GxRoundDelImageView temp = new GxRoundDelImageView(mContext);
        LinearLayout.LayoutParams tempParams = new LinearLayout.LayoutParams(realWidth, realHeight);
        temp.setScaleRealType(ImageView.ScaleType.FIT_XY);
        temp.setImgEntity(entity, onDrawRealImageListener);

        temp.setRaduis(10, 10, 10, 10);
        temp.setLayoutParams(tempParams);
        temp.setClickable(false);
        temp.setFocusableInTouchMode(false);
        temp.setDelImgVisible(isEditModel);
        //点击事件
        temp.setRealListener((realImg, imageEntity) -> {
            if (ADD_FLAG.equals(imageEntity.getPid())) {
                System.out.println("默认添加事件触发 = " + imageEntity.toString());
                //默认添加事件
                defaultAddEvent();
            } else {
                System.out.println("默认点击事件触发 = " + imageEntity.toString());
                //默认点击查看详情
                defaultClickEvent();
            }
        });
        //删除事件
        temp.setDelListener((delImg, imageEntity) -> {
            //默认删除事件
            System.out.println("默认默认删除事件触发 = " + imageEntity.toString());
            defaultDelEvent();
        });
        return temp;
    }

    //默认删除事件处理
    private void defaultDelEvent() {

    }

    //默认点击事件处理
    private void defaultClickEvent() {

    }

    //默认添加事件处理
    private void defaultAddEvent() {

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


    public interface OnHorIvDelClickListener {
        //暴露给外面实现自主 删除事件
        void OnHorIvDelClick(int position, BaseImageEntity entity, GxRoundDelImageView iv);
    }

    public interface OnHorIvAddClickListener {
        //暴露给外面实现自主 处理添加事件
        void onHorIvAddClick(int position, BaseImageEntity entity, GxRoundDelImageView iv);
    }

    public interface OnHorIvClickListener {

        //暴露给外面实现自主 处理点击事件
        void OnHorIvClick(int position, BaseImageEntity entity, GxRoundDelImageView iv);
    }


    private void drawImages(ArrayList<BaseImageEntity> cImages) {
        for (int i = 0; i < cImages.size(); i++) {
            int index = 0;
            int childs = llContainer.getChildCount();
            if (childs > 0) {
                index = childs;
            }
            //说明有addBtn
            if (isEditModel) {
                index = childs - 3;
            }
            //处理start end space
            if ((showStarSpace && i == 0) || (showEndSpace && i == cImages.size() - 1)) {
                llContainer.addView(createSpace(), index);
                index = index + 1;
            }
            if (showMiddleSpace && i != 0 && i != cImages.size() - 1) {
                llContainer.addView(createSpace(), index);
                index = index + 1;
            }
            BaseImageEntity cEntity = cImages.get(i);
            GxRoundDelImageView temp = createImgView(i, cEntity);
            //删除事件
            if (onHorIvDelClickListener != null) {
                temp.setDelListener((delImg, entity) -> onHorIvDelClickListener.OnHorIvDelClick(getRealIndex(temp), cEntity, temp));
            }
            //点击事件
            if (onHorIvClickListener != null)
                temp.setRealListener((realImg, imageEntity) -> {
                    if (!ADD_FLAG.equals(cEntity.getId())) {
                        if (onHorIvClickListener != null) {
                            onHorIvClickListener.OnHorIvClick(getRealIndex(temp), cEntity, temp);
                        }
                    }
                });
            //这里处理 分割问题
            llContainer.addView(temp, index);
            //添加视图
            gxImgViews.add(temp);
        }
    }

    /**
     * 重新设置数据源
     *
     * @param images
     */
    public void setImages(@NotNull ArrayList<BaseImageEntity> images) {
        if (entities != null) {
            entities.clear();
        }
        if (gxImgViews != null) {
            gxImgViews.clear();
        }
        llContainer.removeAllViews();
        hasAddBtn = false;
        if (isEditModel) {
            addbtnView();
        }
        entities.addAll(0, images);
        drawImages(images);
    }

    public boolean isEditModel() {
        return isEditModel;
    }

    //是否是编辑模式
    public void setEditModel(boolean editModel) {
        isEditModel = editModel;
        if (isEditModel) {
            showDelAndAdd();
        } else {
            removeDelAndAdd();
        }
    }

    /**
     * 添加Image
     */
    public ArrayList<BaseImageEntity> addEntity(BaseImageEntity addEntity) {
        if (isEditModel) {
            entities.add(addEntity);
            ArrayList<BaseImageEntity> temparr = new ArrayList<>();
            temparr.add(addEntity);
            drawImages(temparr);
        }
        return entities;
    }

    /**
     * 更新entity
     */
    private ArrayList<BaseImageEntity> updateEntity(ArrayList<BaseImageEntity> cEntities) {
        if (cEntities == null || cEntities.size() == 0) {
            return entities;
        }
        for (int i = 0; i < cEntities.size(); i++) {
            BaseImageEntity cEntity = cEntities.get(i);
            if (cEntity == null) {
                continue;
            }
            //更新实体
            for (int j = 0; j < entities.size(); j++) {
                BaseImageEntity jEntity = entities.get(j);
                if (jEntity == null) {
                    continue;
                }
                if (cEntity.getId().equals(jEntity.getId())) {
                    entities.remove(jEntity);
                    entities.add(j, cEntity);
                    break;
                }
            }
            //更新imageView
            for (int j = 0; j < gxImgViews.size(); j++) {
                GxRoundDelImageView cImageView = gxImgViews.get(j);
                BaseImageEntity jEntity = cImageView.getImgEntity();
                if (jEntity == null) {
                    continue;
                }
                if (cEntity.getId().equals(jEntity.getId())) {
                    gxImgViews.remove(cImageView);
                    cImageView.setImgEntity(jEntity, onDrawRealImageListener);
                    //删除事件
                    if (onHorIvDelClickListener != null) {
                        cImageView.setDelListener((delImg, entity) ->
                                onHorIvDelClickListener.OnHorIvDelClick(getRealIndex(cImageView), cEntity, cImageView));
                    }
                    //点击事件
                    if (onHorIvClickListener != null)
                        cImageView.setRealListener((realImg, imageEntity) -> {
                            if (!ADD_FLAG.equals(cEntity.getId())) {
                                if (onHorIvClickListener != null) {
                                    onHorIvClickListener.OnHorIvClick(getRealIndex(cImageView), cEntity, cImageView);
                                }
                            }
                        });
                    gxImgViews.add(j, cImageView);
                    break;
                }
            }
        }
        return entities;
    }

    /**
     * 更新entity
     */
    public ArrayList<BaseImageEntity> updateEntity(BaseImageEntity entity) {
        ArrayList<BaseImageEntity> temp = new ArrayList<>();
        temp.add(entity);
        return updateEntity(temp);
    }

    /**
     * 删除Image
     *
     * @return
     */
    public ArrayList<BaseImageEntity> removeEntity(BaseImageEntity entity) {
        if (isEditModel) {
            int childcount = llContainer.getChildCount();
            for (int i = 0; i < childcount; i++) {
                View view = llContainer.getChildAt(i);
                if (view instanceof GxRoundDelImageView) {
                    GxRoundDelImageView temp = (GxRoundDelImageView) view;
                    //排除addbtn
                    if (ADD_FLAG.equals(entity.getPid())) {
                        continue;
                    }
                    if (entity.getId().equals(temp.getImgEntity().getId())) {
                        //移除img
                        llContainer.removeViewAt(i);
                        //移除空格
                        llContainer.removeViewAt(i - 1);
                        entities.remove(entity);
                        gxImgViews.remove(temp);
                        break;
                    }
                }
            }
        }
        return entities;
    }

    //不是编辑模式移除删除按钮和添加按钮
    private void removeDelAndAdd() {
        //移除addBtn
        if (hasAddBtn) {
            int index = llContainer.getChildCount() - 1;
            //移除最后一个space
            llContainer.removeViewAt(index);
            //移除addBtn
            llContainer.removeView(addBtnView);
            //移除addBtn 前的space
            if (entities.size() == 0) {
                llContainer.removeViewAt(index - 2);
            }
            hasAddBtn = false;
        }
        //禁用del
        for (int i = 0; i < gxImgViews.size(); i++) {
            GxRoundDelImageView view = gxImgViews.get(i);
            view.setDelImgVisible(false);
        }
    }

    //是添加模式增加删除按钮和添加按钮
    private void showDelAndAdd() {
        //启用del
        for (int i = 0; i < gxImgViews.size(); i++) {
            GxRoundDelImageView view = gxImgViews.get(i);
            view.setDelImgVisible(true);
        }
        addbtnView();
        post(() -> {
            //移动scrollview
            fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        });
    }

    private void addbtnView() {
        if (!hasAddBtn) {
            //这里处理 分割问题
            int index = 0;
            int childs = llContainer.getChildCount() - 1;
            if (childs > 0) {
                index = childs;
            }
            //添加最后一个space
            if (entities.size() == 0) {
                //加一个空格
                llContainer.addView(createSpace(), index);
            }
            //加一个addBtn
            addBtnView = createImgView(index, addBtn);
            addBtnView.setDelImgVisible(false);
            llContainer.addView(addBtnView, ++index);
            //加一个空格
            llContainer.addView(createSpace(), ++index);
            hasAddBtn = true;
        }
    }

    //自定义 处理点击事件
    public void setOnHorIvClickListener(OnHorIvClickListener click) {
        this.onHorIvClickListener = click;
        for (int i = 0; i < gxImgViews.size(); i++) {
            GxRoundDelImageView temp = gxImgViews.get(i);
            temp.setRealListener((realImg, imageEntity) -> {
                if (!ADD_FLAG.equals(imageEntity.getPid())) {
                    onHorIvClickListener.OnHorIvClick(getRealIndex(temp), temp.getImgEntity(), temp);
                }
            });
        }
    }

    private int getRealIndex(GxRoundDelImageView temp) {
        for (int j = 0; j < entities.size(); j++) {
            BaseImageEntity jen = entities.get(j);
            if (jen.getId().equals(temp.getImgEntity().getId())) {
                return j;
            }
        }
        return -1;
    }

    //自定义 处理删除事件
    public void setOnHorIvDelClickListener(OnHorIvDelClickListener onHorIvDelClickListener) {
        this.onHorIvDelClickListener = onHorIvDelClickListener;
        for (int i = 0; i < gxImgViews.size(); i++) {
            GxRoundDelImageView temp = gxImgViews.get(i);
            temp.setDelListener((realImg, imageEntity) -> {
                if (!ADD_FLAG.equals(imageEntity.getPid())) {
                    onHorIvDelClickListener.OnHorIvDelClick(getRealIndex(temp), temp.getImgEntity(), temp);
                }
            });
        }
    }

    //自定义 添加事件
    public void setOnHorIvAddClickListener(OnHorIvAddClickListener addClick) {
        this.onHorIvAddClickListener = addClick;
        addBtnView.setRealListener((realImg, imageEntity) -> onHorIvAddClickListener.onHorIvAddClick(-1, null, addBtnView));

    }
}
