package com.hanschen.runwithyou.widget;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

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
    private int mWidth;
    /**
     * 高度
     */
    private int mHeight;
    /**
     * 进度条最大值
     */
    private float mMax = 100;
    /**
     * 上次设置的进度值
     */
    private int    mOldProgress;
    /**
     * 当前设置的进度值
     */
    private int    mProgress;
    /**
     * 当前绘制的进度值，动画过程中会改变
     */
    private int    mCurrentDrawProgress;
    /**
     * 绘制弧线的画笔
     */
    private Paint  mProgressPaint;
    /**
     * 绘制文字的画笔
     */
    private Paint  mTextPaint;
    /**
     * 绘制文字背景圆形的画笔
     */
    private Paint  mTextBgPaint;
    /**
     * 默认圆弧大小
     */
    private int    mDefaultRadius;
    /**
     * 圆弧的半径
     */
    private int    mCircleRadius;
    /**
     * 圆弧圆心X坐标
     */
    private int    mCenterX;
    /**
     * 圆弧圆心Y坐标
     */
    private int    mCenterY;
    /**
     * 中间字体大小
     */
    private int    mContentTextSize;
    /**
     * 中间字体颜色
     */
    private int    mContentTextColor;
    /**
     * 单位
     */
    private String mContentUnit;
    /**
     * 子内容
     */
    private String mSubText;
    /**
     * 子内容字体大小
     */
    private int    mSubTextSize;
    /**
     * 子内容字体颜色
     */
    private int    mSubTextColor;
    /**
     * 中间圆颜色
     */
    private int    mCircleBackground;
    /**
     * 进度条开始颜色
     */
    private int    mProgressStartColor;
    /**
     * 进度条结束颜色
     */
    private int    mProgressEndColor;
    /**
     * 进度条Inactive颜色
     */
    private int    mProgressInactiveColor;

    private Rect             mTextBounds       = new Rect();
    private TimeInterpolator mTimeInterpolator = new DecelerateInterpolator();

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        //获取属性
        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0);
        mContentTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressBar_contentTextSize, dp2px(context, 56));
        mContentTextColor = a.getColor(R.styleable.CircleProgressBar_contentTextColor, Color.WHITE);
        mContentUnit = a.getString(R.styleable.CircleProgressBar_contentUnit);
        mSubText = a.getString(R.styleable.CircleProgressBar_subText);
        mSubTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressBar_subTextSize, dp2px(context, 18));
        mSubTextColor = a.getColor(R.styleable.CircleProgressBar_subTextColor, Color.parseColor("#B3FFFFFF"));
        mCircleBackground = a.getColor(R.styleable.CircleProgressBar_circleBackground, Color.parseColor("#3F51B5"));
        mProgressStartColor = a.getColor(R.styleable.CircleProgressBar_progressStartColor, Color.CYAN);
        mProgressEndColor = a.getColor(R.styleable.CircleProgressBar_progressEndColor, Color.CYAN);
        mProgressInactiveColor = a.getColor(R.styleable.CircleProgressBar_progressInactiveColor, Color.parseColor("#88aaaaaa"));
        a.recycle();

        //设置默认大小
        mDefaultRadius = dp2px(context, 120);

        //初始化画笔
        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mTextPaint = new Paint();
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

    /**
     * 根据控件大小和内边距计算圆心和半径
     */
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
        float sweep = ARC_FULL_DEGREE * (mCurrentDrawProgress / mMax); //进度划过的角度

        //绘制进度条
        mProgressPaint.setColor(calcColor(mCurrentDrawProgress / mMax, mProgressStartColor, mProgressEndColor));
        mProgressPaint.setStrokeWidth(ARC_LINE_WIDTH);
        float drawDegree = 1.6f;
        while (drawDegree <= ARC_FULL_DEGREE) {
            double a = (start + drawDegree) / 180 * Math.PI;
            float lineStartX = mCenterX - mCircleRadius * (float) Math.sin(a);
            float lineStartY = mCenterY + mCircleRadius * (float) Math.cos(a);
            float lineStopX = lineStartX + ARC_LINE_LENGTH * (float) Math.sin(a);
            float lineStopY = lineStartY - ARC_LINE_LENGTH * (float) Math.cos(a);

            if (drawDegree > sweep) {
                mProgressPaint.setColor(mProgressInactiveColor);
                mProgressPaint.setStrokeWidth(ARC_LINE_WIDTH >> 1);
            }
            canvas.drawLine(lineStartX, lineStartY, lineStopX, lineStopY, mProgressPaint);

            drawDegree += ARC_EACH_PROGRESS;
        }

        //绘制文字背景圆形
        mTextBgPaint.setStyle(Paint.Style.FILL);//设置填充
        mTextBgPaint.setColor(mCircleBackground);
        canvas.drawCircle(mCenterX, mCenterY, (mCircleRadius - ARC_LINE_LENGTH - 20), mTextBgPaint);
        mTextBgPaint.setStyle(Paint.Style.STROKE);//设置空心
        mTextBgPaint.setStrokeWidth(2);
        mTextBgPaint.setColor(Color.parseColor("#aaaaaaaa"));
        canvas.drawCircle(mCenterX, mCenterY, (mCircleRadius - ARC_LINE_LENGTH - 20), mTextBgPaint);

        //第一行文字
        mTextPaint.setTextSize(mContentTextSize);
        mTextPaint.setColor(mContentTextColor);
        String text = String.valueOf((int) (100 * mCurrentDrawProgress / mMax));
        float textLen = mTextPaint.measureText(text);
        //计算文字高度
        mTextPaint.getTextBounds(text, 0, 1, mTextBounds);
        float h1 = mTextBounds.height();
        canvas.drawText(text, mCenterX - textLen / 2, mCenterY - mCircleRadius / 10 + h1 / 2, mTextPaint);

        //单位
        if (!TextUtils.isEmpty(mContentUnit)) {
            mTextPaint.setTextSize(40);
            mTextPaint.getTextBounds(mContentUnit, 0, 1, mTextBounds);
            float h11 = mTextBounds.height();
            canvas.drawText(mContentUnit, mCenterX + textLen / 2 + 5, mCenterY - mCircleRadius / 10 + h1 / 2 - (h1 - h11), mTextPaint);

        }

        //mSubText
        if (!TextUtils.isEmpty(mSubText)) {
            mTextPaint.setTextSize(mSubTextSize);
            mTextPaint.setColor(mSubTextColor);
            textLen = mTextPaint.measureText(mSubText);
            canvas.drawText(mSubText, mCenterX - textLen / 2, mCenterY + mCircleRadius / 2.5f, mTextPaint);
        }
    }

    public void setMax(int max) {
        this.mMax = max;
        invalidate();
    }

    //设置新的进度值，带动画
    public void setProgress(final int progress) {
        mOldProgress = mProgress;
        mProgress = progress;
        startAnimation();
    }

    //设置新的进度值，没有动画
    public void setProgressWithoutAnim(final int progress) {
        mOldProgress = mProgress;
        mProgress = progress;
        mCurrentDrawProgress = mProgress;
        postInvalidate();
    }

    private void startAnimation() {
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setDuration(1000);
        anim.setInterpolator(mTimeInterpolator);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                int diff = (int) ((mProgress - mOldProgress) * currentValue);
                mCurrentDrawProgress = mOldProgress + diff;
                postInvalidate();
            }
        });
        anim.start();
    }

    /**
     * 计算开始颜色和结束颜色的中间值
     *
     * @param startValue 开始颜色
     * @param endValue   结束颜色
     * @return 新的颜色值
     */
    @ColorInt
    public int calcColor(float fraction, @ColorInt int startValue, @ColorInt int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }


}
