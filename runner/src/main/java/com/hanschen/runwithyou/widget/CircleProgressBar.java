package com.hanschen.runwithyou.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.hanschen.runwithyou.R;

/**
 * @author HansChen
 */
public class CircleProgressBar extends View {

    /**
     * 进度条所占用的角度
     */
    private static final int   ARC_FULL_DEGREE   = 300;
    /**
     * 进度条个数
     */
    private static final int   COUNT             = 100;
    /**
     * 每个进度条所占用角度
     */
    private static final float ARC_EACH_PROGRESS = ARC_FULL_DEGREE * 1.0f / (COUNT - 1);
    /**
     * 弧线细线条的长度
     */
    private static final int   ARC_LINE_LENGTH   = 60;
    /**
     * 弧线细线条的宽度
     */
    private static final int   ARC_LINE_WIDTH    = 6;
    /**
     * 宽度
     */
    private int   mWidth;
    /**
     * 高度
     */
    private int   mHeight;
    /**
     * 进度条最大值
     */
    private float mMax;
    /**
     * 进度条当前进度值
     */
    private int   mProgress;
    /**
     * 绘制弧线的画笔
     */
    private Paint mProgressPaint;
    /**
     * 绘制文字的画笔
     */
    private Paint mTextPaint;
    /**
     * 绘制文字背景圆形的画笔
     */
    private Paint mTextBgPaint;
    /**
     * 默认圆弧大小
     */
    private int   mDefaultRadius;
    /**
     * 圆弧的半径
     */
    private int   mCircleRadius;
    /**
     * 圆弧圆心X坐标
     */
    private int   mCenterX;
    /**
     * 圆弧圆心Y坐标
     */
    private int   mCenterY;

    private Rect textBounds = new Rect();

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private int    contentTextSize;
    private int    contentTextColor;
    private String contentUnit = "fen";

    private String subText = "dddd";
    private int subTextSize;
    private int subTextColor;

    private int progressColor;
    private int circleBackground;
    private int progressStartColor;
    private int progressEndColor;

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0);
        contentTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressBar_contentTextSize, dp2px(context, 56));
        contentTextColor = a.getColor(R.styleable.CircleProgressBar_contentTextColor, Color.WHITE);
        contentUnit = a.getString(R.styleable.CircleProgressBar_contentUnit);
        subText = a.getString(R.styleable.CircleProgressBar_subText);
        subTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressBar_subTextSize, dp2px(context, 18));
        subTextColor = a.getColor(R.styleable.CircleProgressBar_subTextColor, Color.parseColor("#B3FFFFFF"));
        circleBackground = a.getColor(R.styleable.CircleProgressBar_circleBackground, Color.parseColor("#3F51B5"));
        progressStartColor = a.getColor(R.styleable.CircleProgressBar_progressStartColor, Color.CYAN);
        progressEndColor = a.getColor(R.styleable.CircleProgressBar_progressEndColor, Color.CYAN);
        a.recycle();

        mDefaultRadius = dp2px(context, 120);

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);

        mTextBgPaint = new Paint();
        mTextBgPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = 2 * mDefaultRadius + getPaddingLeft() + getPaddingRight();
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = 2 * mDefaultRadius + getPaddingTop() + getPaddingBottom();
        }

        calcRadiusAndCenter();
        setMeasuredDimension(mWidth, mHeight);
    }

    private void calcRadiusAndCenter() {
        int x = mWidth - getPaddingLeft() - getPaddingRight();
        int y = mHeight - getPaddingTop() - getPaddingRight();
        mCircleRadius = Math.min(x, y) >> 1;
        mCenterX = getPaddingLeft() + mCircleRadius;
        mCenterY = getPaddingTop() + mCircleRadius;
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float start = (360 - ARC_FULL_DEGREE) >> 1; //进度条起始角度
        float sweep1 = ARC_FULL_DEGREE * (mProgress / mMax); //进度划过的角度

        //绘制进度条
        mProgressPaint.setColor(calcColor(mProgress / mMax, progressStartColor, progressEndColor));
        mProgressPaint.setStrokeWidth(ARC_LINE_WIDTH);
        float drawDegree = 1.6f;
        while (drawDegree <= ARC_FULL_DEGREE) {
            double a = (start + drawDegree) / 180 * Math.PI;
            float lineStartX = mCenterX - mCircleRadius * (float) Math.sin(a);
            float lineStartY = mCenterY + mCircleRadius * (float) Math.cos(a);
            float lineStopX = lineStartX + ARC_LINE_LENGTH * (float) Math.sin(a);
            float lineStopY = lineStartY - ARC_LINE_LENGTH * (float) Math.cos(a);

            if (drawDegree > sweep1) {
                //绘制进度条背景
                mProgressPaint.setColor(Color.parseColor("#88aaaaaa"));
                mProgressPaint.setStrokeWidth(ARC_LINE_WIDTH >> 1);
            }
            canvas.drawLine(lineStartX, lineStartY, lineStopX, lineStopY, mProgressPaint);

            drawDegree += ARC_EACH_PROGRESS;
        }

        //绘制文字背景圆形
        mTextBgPaint.setStyle(Paint.Style.FILL);//设置填充
        mTextBgPaint.setColor(circleBackground);
        canvas.drawCircle(mCenterX, mCenterY, (mCircleRadius - ARC_LINE_LENGTH - 20), mTextBgPaint);

        mTextBgPaint.setStyle(Paint.Style.STROKE);//设置空心
        mTextBgPaint.setStrokeWidth(2);
        mTextBgPaint.setColor(Color.parseColor("#aaaaaaaa"));
        canvas.drawCircle(mCenterX, mCenterY, (mCircleRadius - ARC_LINE_LENGTH - 20), mTextBgPaint);

        //上一行文字
        mTextPaint.setTextSize(contentTextSize);
        mTextPaint.setColor(contentTextColor);
        String text = (int) (100 * mProgress / mMax) + "";
        float textLen = mTextPaint.measureText(text);
        //计算文字高度
        mTextPaint.getTextBounds("8", 0, 1, textBounds);
        float h1 = textBounds.height();
        canvas.drawText(text, mCenterX - textLen / 2, mCenterY - mCircleRadius / 10 + h1 / 2, mTextPaint);

        //单位
        if (!TextUtils.isEmpty(contentUnit)) {
            mTextPaint.setTextSize(40);
            mTextPaint.getTextBounds(contentUnit, 0, 1, textBounds);
            float h11 = textBounds.height();
            canvas.drawText(contentUnit, mCenterX + textLen / 2 + 5, mCenterY - mCircleRadius / 10 + h1 / 2 - (h1 - h11), mTextPaint);

        }

        //subText
        if (!TextUtils.isEmpty(subText)) {
            mTextPaint.setTextSize(subTextSize);
            mTextPaint.setColor(subTextColor);
            textLen = mTextPaint.measureText(subText);
            canvas.drawText(subText, mCenterX - textLen / 2, mCenterY + mCircleRadius / 2.5f, mTextPaint);
        }
    }

    public void setMax(int max) {
        this.mMax = max;
        invalidate();
    }

    //动画切换进度值(异步)
    public void setProgress(final float progress) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                float oldProgress = mProgress;
                for (int i = 1; i <= 100; i++) {
                    mProgress = (int) (oldProgress + (progress - oldProgress) * (1.0f * i / 100));
                    postInvalidate();
                    SystemClock.sleep(20);
                }
            }
        }).start();
    }

    //直接设置进度值（同步）
    public void setProgressSync(int progress) {
        this.mProgress = progress;
        invalidate();
    }

    public int calcColor(float fraction, int startValue, int endValue) {
        int start_a, start_r, start_g, start_b;
        int end_a, end_r, end_g, end_b;
        int new_a, new_r, new_g, new_b;

        //start
        start_a = (startValue) >> 24 & 0xff;
        start_r = (startValue) >> 16 & 0xff;
        start_g = (startValue) >> 8 & 0xff;
        start_b = startValue & 0xff;

        //end
        end_a = (endValue) >> 24 & 0xff;
        end_r = (endValue) >> 16 & 0xff;
        end_g = (endValue) >> 8 & 0xff;
        end_b = endValue & 0xff;

        //new
        new_a = (int) (start_a + fraction * (end_a - start_a));
        new_r = (int) (start_r + fraction * (end_r - start_r));
        new_g = (int) (start_g + fraction * (end_g - start_g));
        new_b = (int) (start_b + fraction * (end_b - start_b));


        return new_a << 24 | new_r << 16 | new_g | new_b;
    }

    //从原始#AARRGGBB颜色值中指定位置截取，并转为int.
    private int getIntValue(String hexValue, int start, int end) {
        return Integer.parseInt(hexValue.substring(start, end), 16);
    }

    private String getHexString(int value) {
        String a = Integer.toHexString(value);
        if (a.length() == 1) {
            a = "0" + a;
        }

        return a;
    }
}
