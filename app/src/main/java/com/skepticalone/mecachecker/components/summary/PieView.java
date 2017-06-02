package com.skepticalone.mecachecker.components.summary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.skepticalone.mecachecker.R;

public class PieView extends View {
    private static final String TAG = "PieView";
    @ColorInt
    private final int colorOne, colorTwo, colorThree;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF mRectF = new RectF();
    private Rect mRect = new Rect();
    private long one, two, three;

    public PieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        colorOne = ContextCompat.getColor(context, R.color.one);
        colorTwo = ContextCompat.getColor(context, R.color.two);
        colorThree = ContextCompat.getColor(context, R.color.three);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PieView,
                0, 0);
        try {
            one = a.getInteger(R.styleable.PieView_one, 0);
            two = a.getInteger(R.styleable.PieView_two, 0);
            three = a.getInteger(R.styleable.PieView_three, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (one != 0 || two != 0 || three != 0) {
            mRectF.set(0, 0, canvas.getWidth(), canvas.getHeight());
            float factor = 360f / (one + two + three);
            float startAngle = -90f;
            float sweepAngle;
            if (one != 0) {
                mPaint.setColor(colorOne);
                sweepAngle = one * factor;
                canvas.drawArc(mRectF, startAngle, sweepAngle, true, mPaint);
                startAngle += sweepAngle;
            }
            if (two != 0) {
                mPaint.setColor(colorTwo);
                sweepAngle = two * factor;
                canvas.drawArc(mRectF, startAngle, sweepAngle, true, mPaint);
                startAngle += sweepAngle;
            }
            if (three != 0) {
                mPaint.setColor(colorThree);
                sweepAngle = three * factor;
                canvas.drawArc(mRectF, startAngle, sweepAngle, true, mPaint);
            }
        }
    }

    public void set(long one, long two, long three) {
        this.one = one;
        this.two = two;
        this.three = three;
        invalidate();
    }

    public void set(long one, long two) {
        set(one, two, 0);
    }
}
