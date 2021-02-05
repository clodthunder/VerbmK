package com.lskj.gx.basic_widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

import java.math.BigDecimal;

/**
 * 创建时间:  2021/2/3
 * 仪表盘自定义view
 */
public class MeterView extends View {
    private static final int DEFAULT_COLOR_LOWER = Color.parseColor("#1d953f");
    private static final int DEFAULT_COLOR_MIDDLE = Color.parseColor("#228fbd");
    private static final int DEFAULT_COLOR_HIGH = Color.RED;
    private static final int DEAFAULT_COLOR_TITLE = Color.BLACK;
    private static final int DEFAULT_TEXT_SIZE_DIAL = 11;
    private static final int DEFAULT_STROKE_WIDTH = 8;
    private static final int DEFAULT_RADIUS_DIAL = 128;
    private static final int DEAFAULT_TITLE_SIZE = 16;
    private static final int DEFAULT_VALUE_SIZE = 28;
    private static final int DEFAULT_ANIM_PLAY_TIME = 2000;

    private int colorDialLower;
    private int colorDialMiddle;
    private int colorDialHigh;
    private int textSizeDial;
    private int strokeWidthDial;
    private String titleDial;
    private int titleDialSize;
    private int titleDialColor;
    private int valueTextSize;
    private int animPlayTime;

    private int radiusDial;
    private int mRealRadius;

    private Paint arcPaint;
    private RectF mRect;
    private Paint pointerPaint;
    private Paint.FontMetrics fontMetrics;
    private Paint titlePaint;
    private Path pointerPath;

    //    仪表盘最大值
    private float max = 10;
    //    仪表盘最小值
    private float min = -10;
    //  20%
    private float peri20 = -2;
    //  80%
    private float peri80 = 8;
    //    仪表盘指针当前指向
    private float currentValue;
    //    之前显示的值
    private float beforValue = 0;
    //    长指针间隔多少个短指针
    private int middlePairSize = 5;

    private float pair = 270f / (max - min) / middlePairSize;
    //    画布
    private Canvas canvas;

    public MeterView(Context context) {
        this(context, null);
    }

    public MeterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.MeterView);
        colorDialLower = attributes.getColor(R.styleable.MeterView_color_dial_lower, DEFAULT_COLOR_LOWER);
        colorDialMiddle = attributes.getColor(R.styleable.MeterView_color_dial_middle, DEFAULT_COLOR_MIDDLE);
        colorDialHigh = attributes.getColor(R.styleable.MeterView_color_dial_high, DEFAULT_COLOR_HIGH);
        textSizeDial = (int) attributes.getDimension(R.styleable.MeterView_text_size_dial, sp2px(DEFAULT_TEXT_SIZE_DIAL));
        strokeWidthDial = (int) attributes.getDimension(R.styleable.MeterView_stroke_width_dial, dp2px(DEFAULT_STROKE_WIDTH));
        radiusDial = (int) attributes.getDimension(R.styleable.MeterView_radius_circle_dial, dp2px(DEFAULT_RADIUS_DIAL));
        titleDial = attributes.getString(R.styleable.MeterView_text_title_dial);
        titleDialSize = (int) attributes.getDimension(R.styleable.MeterView_text_title_size, dp2px(DEAFAULT_TITLE_SIZE));
        titleDialColor = attributes.getColor(R.styleable.MeterView_text_title_color, DEAFAULT_COLOR_TITLE);
        valueTextSize = (int) attributes.getDimension(R.styleable.MeterView_text_size_value, dp2px(DEFAULT_VALUE_SIZE));
        animPlayTime = attributes.getInt(R.styleable.MeterView_animator_play_time, DEFAULT_ANIM_PLAY_TIME);
    }

    private void initPaint() {
        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(strokeWidthDial);

        pointerPaint = new Paint();
        pointerPaint.setAntiAlias(true);
        pointerPaint.setTextSize(textSizeDial);
        pointerPaint.setTextAlign(Paint.Align.CENTER);
        fontMetrics = pointerPaint.getFontMetrics();

        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setFakeBoldText(true);

        pointerPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int mWidth, mHeight;
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = getPaddingLeft() + radiusDial * 2 + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                mWidth = Math.min(mWidth, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = getPaddingTop() + radiusDial * 2 + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(mHeight, heightSize);
            }
        }

        setMeasuredDimension(mWidth, mHeight);

        radiusDial = Math.min((getMeasuredWidth() - getPaddingLeft() - getPaddingRight()),
                (getMeasuredHeight() - getPaddingTop() - getPaddingBottom())) / 2;
        mRealRadius = radiusDial - strokeWidthDial / 2;
        mRect = new RectF(-mRealRadius, -mRealRadius, mRealRadius, mRealRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        drawDync();
    }

    private void drawDync() {
        if (canvas == null) {
            return;
        }
        drawArc(canvas);
        drawPointerLine(canvas);
        drawTitleDial(canvas);
        drawPointer(canvas);
    }

    private void drawArc(Canvas canvas) {
        canvas.translate(getPaddingLeft() + radiusDial, getPaddingTop() + radiusDial);
        arcPaint.setColor(colorDialLower);
        canvas.drawArc(mRect, 135, 54, false, arcPaint);
        arcPaint.setColor(colorDialMiddle);
        canvas.drawArc(mRect, 189, 162, false, arcPaint);
        arcPaint.setColor(colorDialHigh);
        canvas.drawArc(mRect, 351, 54, false, arcPaint);
    }

    private void drawPointerLine(Canvas canvas) {
        canvas.rotate(135);
        float cValue = min;
        for (float i = min * middlePairSize; i <= max * middlePairSize; i++) {     //一共需要绘制101个表针
            if (i < 0) {
                if (Math.abs(i) > (Math.abs(peri20)) * middlePairSize) {
                    if (Math.abs(i) == Math.abs(max) && max == 0) {
                        pointerPaint.setColor(colorDialHigh);
                    } else {
                        pointerPaint.setColor(colorDialLower);
                    }
                } else if (Math.abs(i) <= Math.abs(peri20) * middlePairSize) {//蓝色
                    if (Math.abs(i) == Math.abs(max) && max == 0) {
                        pointerPaint.setColor(colorDialHigh);
                    } else {
                        if (max > 0) {
                            pointerPaint.setColor(colorDialMiddle);
                        } else {
                            //多加一个判断 max 小于0 对比 peri80
                            if (Math.abs(i) < Math.abs(peri80) * middlePairSize) {
                                pointerPaint.setColor(colorDialHigh);
                            } else {
                                pointerPaint.setColor(colorDialMiddle);
                            }
                        }
                    }
                } else {//红色
                    pointerPaint.setColor(colorDialHigh);
                }
            } else {
                if (i < peri20 * middlePairSize) {
                    pointerPaint.setColor(colorDialLower);
                } else if (i < peri80 * middlePairSize) {//蓝色
                    pointerPaint.setColor(colorDialMiddle);
                } else {//红色
                    pointerPaint.setColor(colorDialHigh);
                }
            }
            //绘制最小、最大、整数
            if (i % middlePairSize == 0 || i == max || i == min) {     //长表针
                pointerPaint.setStrokeWidth(6);
                canvas.drawLine(radiusDial, 0, radiusDial - strokeWidthDial - dp2px(15), 0, pointerPaint);
                drawPointerText(canvas, (int) cValue);
                cValue = cValue + 1;
            } else { //短表针
                pointerPaint.setStrokeWidth(3);
                canvas.drawLine(radiusDial, 0, radiusDial - strokeWidthDial - dp2px(5), 0, pointerPaint);
            }
            canvas.rotate(pair);
        }
    }

    private void drawPointerText(Canvas canvas, int i) {
        canvas.save();
        int currentCenterX = (int) (radiusDial - strokeWidthDial - dp2px(21) - pointerPaint.measureText(String.valueOf(i)) / 2);
        canvas.translate(currentCenterX, 0);
        canvas.rotate(360 - 135 - pair * i);        //坐标系总旋转角度为360度

        int textBaseLine = (int) (0 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
        canvas.drawText(String.valueOf(i), 0, textBaseLine, pointerPaint);
        canvas.restore();
    }

    //绘制当前检测数值
    private void drawTitleDial(Canvas canvas) {
        titlePaint.setColor(titleDialColor);
        titlePaint.setTextSize(titleDialSize);
        canvas.rotate(-(45 + pair));       //恢复坐标系为起始中心位置
        canvas.drawText(titleDial, 0, -radiusDial / 3, titlePaint);
        if (currentValue < 0) {
            if (Math.abs(currentValue) > Math.abs(peri20)) {
                pointerPaint.setColor(colorDialLower);
            } else if (Math.abs(currentValue) > (Math.abs(min) - Math.abs(peri80))) {//蓝色
                pointerPaint.setColor(colorDialMiddle);
            } else {//红色
                pointerPaint.setColor(colorDialHigh);
            }
        } else {
            //20% 阈值
            if (currentValue <= peri20) {
                titlePaint.setColor(colorDialLower);
                //80% 阈值
            } else if (currentValue <= peri80) {
                titlePaint.setColor(colorDialMiddle);
            } else {
                titlePaint.setColor(colorDialHigh);
            }
        }
        titlePaint.setTextSize(valueTextSize);
        //仪表盘只展示小数点后2位
        BigDecimal b = new BigDecimal(currentValue);
        float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        canvas.drawText(f1 + "", 0, radiusDial * 2 / 3, titlePaint);
    }

    //绘制当前指针
    private void drawPointer(Canvas canvas) {
        //指针度数 = 起始度数 + 当前数*(pair*middle)
        int currentDegree = 135;
        if (min < 0) {
            if (currentValue == 0) {
                currentDegree = 270;
            } else if (currentValue < 0) {
                currentDegree = (int) ((Math.abs(min) - Math.abs(currentValue)) * pair * middlePairSize + 135);
            } else {
                currentDegree = (int) ((currentValue - min) * pair * middlePairSize + 135);
            }
        } else {
            currentDegree = (int) ((currentValue - min) * pair * middlePairSize + 135);
        }
        canvas.rotate(currentDegree);
        pointerPath.moveTo(radiusDial - strokeWidthDial - dp2px(12), 0);
        pointerPath.lineTo(0, -dp2px(5));
        pointerPath.lineTo(-12, 0);
        pointerPath.lineTo(0, dp2px(5));
        pointerPath.close();
        canvas.drawPath(pointerPath, titlePaint);
    }

    /**
     * 设置当前指针指向
     *
     * @param @curValue 当前值是多少
     */
    public void setCurrentValue(float curValue, float cmax, float cmin) {
        this.max = cmax;
        this.min = cmin;
        this.pair = 270f / (max - min) / middlePairSize;
        if (this.min < 0 && this.max <= 0) {
            this.peri20 = (float) ((max - min) * 0.2 + min);
        } else if (min >= 0 && max >= 0) {
            this.peri20 = (float) ((max - min) * 0.2) + min;
        } else if (min < 0 && max > 0) {
            this.peri20 = (float) ((max - min) * 0.2 + min);
        }
        if (this.min < 0 && this.max <= 0) {
            this.peri80 = (float) ((max - min) * 0.8 + min);
        } else if (min >= 0 && max >= 0) {
            this.peri80 = (float) ((max - min) * 0.8) + min;
        } else if (min < 0 && max > 0) {
            this.peri80 = (float) ((max - min) * 0.8) - Math.abs(min);
        }
        drawDync();
        setCurrentValue(curValue);
    }

    public void setCurrentValue(float curValue) {
        ValueAnimator animator = ValueAnimator.ofFloat(beforValue, curValue);
        animator.addUpdateListener(animation -> {
            currentValue = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(animPlayTime);
        animator.start();
        beforValue = curValue;
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }
}
