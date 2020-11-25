package com.lskj.gx.basic_widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 创建时间:  2020/9/2
 * 编写人: tzw
 * 功能描述: 矩形imageView 支持自定义四边角度
 *
 *
 */
public class GxRoundImgView extends androidx.appcompat.widget.AppCompatImageView {
  private Context mContext;
  //绘制图形的画笔
  private Paint bitMapPaint = null;
  /**
   * The Left top radius.
   */
  private float ltRadius, /**
   * The Right top radius.
   */
  rtRadius, /**
   * The Left bottom radius.
   */
  lbRadius, /**
   * The Right bottom radius.
   */
  rbRadius;

  ////删除图标的宽度
  //private int delWith = ScreenUtil.dp2px(40);
  ////删除图标的高度
  //private int delHeight = ScreenUtil.dp2px(40);
  //;
  ////删除图标的四角值
  //private float delLtRadius, /**
  // * The Right top radius.
  // */
  //delRtRadius, /**
  // * The Left bottom radius.
  // */
  //delLbRadius, /**
  // * The Right bottom radius.
  // */
  //delRbRadius;
  ////删除图标
  ////private int delDrawable;

  public GxRoundImgView(@NonNull Context context) {
    super(context);
    init(context, null);
  }

  public GxRoundImgView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public GxRoundImgView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context ctx, AttributeSet attrs) {
    this.mContext = ctx;
    @SuppressLint("CustomViewStyleable") TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.GxRoundImgView);
    //图片 四个角的圆角
    ltRadius = a.getDimension(R.styleable.GxRoundImgView_lt_radius, ltRadius);
    rtRadius = a.getDimension(R.styleable.GxRoundImgView_rt_radius, rtRadius);
    lbRadius = a.getDimension(R.styleable.GxRoundImgView_lb_radius, lbRadius);
    rbRadius = a.getDimension(R.styleable.GxRoundImgView_rb_radius, rbRadius);
    ////
    //delWith = a.getDimensionPixelSize(R.styleable.GxRoundImgView_del_width, delWith);
    //delHeight = a.getDimensionPixelSize(R.styleable.GxRoundImgView_del_height, delHeight);

    //删除图片的四个角的圆角
    //delLtRadius = a.getDimension(R.styleable.GxRoundImgView_delLt_radius, delLtRadius);
    //delRtRadius = a.getDimension(R.styleable.GxRoundImgView_delRt_radius, delRtRadius);
    //delLbRadius = a.getDimension(R.styleable.GxRoundImgView_delLb_radius, delLbRadius);
    //delRbRadius = a.getDimension(R.styleable.GxRoundImgView_delRb_radius, delRbRadius);
    //
    ////这里处理默认删除图片的四个圆角 默认删除图片显示在右上方所以 只需要指定右上方的圆角即可 其他角度更改
    //if (delRtRadius == 0) {
    //  delRtRadius = rtRadius;
    //  delLbRadius = 0;
    //  delLtRadius = 0;
    //  delRbRadius = 0;
    //}
    //delDrawable = a.getResourceId(R.styleable.GxRoundImgView_del_drawable, R.drawable.default_delete_icon);

    a.recycle();
    bitMapPaint = new Paint();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  private void resetSize(float size) {
    ltRadius = Math.min(ltRadius, size);
    rtRadius = Math.min(rtRadius, size);
    lbRadius = Math.min(lbRadius, size);
    rbRadius = Math.min(rbRadius, size);
  }

  @SuppressLint("DrawAllocation") @Override
  protected void onDraw(Canvas canvas) {
    if (getDrawable() != null) {
      resetSize(Math.min(getWidth(), getHeight()) / 2f);
      @SuppressLint("DrawAllocation") Bitmap bm = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
      @SuppressLint("DrawAllocation") Canvas mCanvas = new Canvas(bm);
      super.onDraw(mCanvas);

      bitMapPaint.reset();
      bitMapPaint.setAntiAlias(true);
      bitMapPaint.setDither(true);
      bitMapPaint.setStyle(Paint.Style.FILL);
      bitMapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

      Path path = new Path();
      RectF rectF = new RectF(0, 0, getWidth(), getHeight());
      path.addRoundRect(rectF, new float[] {
          ltRadius, ltRadius, rtRadius, rtRadius, rbRadius, rbRadius, lbRadius, lbRadius,
      }, Path.Direction.CW);
      path.setFillType(Path.FillType.INVERSE_EVEN_ODD);
      mCanvas.drawPath(path, bitMapPaint);
      bitMapPaint.setXfermode(null);
      canvas.drawBitmap(bm, 0, 0, bitMapPaint);
      bm.recycle();

      ////删除图标矩形
      //Path delPath = new Path();
      //RectF delRectF = new RectF(getWidth() - delWith, 0, getWidth(), getHeight() - delHeight);
      //delPath.addRoundRect(delRectF, new float[] {
      //    delLtRadius, delLtRadius, delRtRadius, delRtRadius, delLbRadius, delLbRadius, delRbRadius, delRbRadius
      //}, Path.Direction.CW);
      //delPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
      //
      //Bitmap delBitMap = Bitmap.createBitmap(delWith, delHeight, Bitmap.Config.ARGB_8888);
      //mCanvas.drawBitmap(delBitMap, 0, 0, bitMapPaint);
      //delBitMap.recycle();
    } else {
      super.onDraw(canvas);
    }
  }

  /**
   * 设置默认圆角
   *
   * @param lt 左上
   * @param rt 右上
   * @param lb 左下
   * @param rb 右下
   */
  public void setRaduis(int lt, int rt, int lb, int rb) {
    ltRadius = dp2px(lt);
    rtRadius = dp2px(rt);
    lbRadius = dp2px(lb);
    rbRadius = dp2px(rb);
    postInvalidate();
  }

  public int dp2px(int dpValue) {
    float scale = mContext.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }
}
