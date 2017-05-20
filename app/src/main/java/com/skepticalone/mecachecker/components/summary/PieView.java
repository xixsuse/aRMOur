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
    private final int unclaimedColor, claimedColor, paidColor;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect mRect = new Rect();
    private RectF mRectF = new RectF();
    private float unclaimed, claimed, paid;

    public PieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        unclaimedColor = ContextCompat.getColor(context, R.color.unclaimed);
        claimedColor = ContextCompat.getColor(context, R.color.claimed);
        paidColor = ContextCompat.getColor(context, R.color.paid);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PieView,
                0, 0);
        try {
            unclaimed = a.getFloat(R.styleable.PieView_unclaimed, 0);
            claimed = a.getFloat(R.styleable.PieView_claimed, 0);
            paid = a.getFloat(R.styleable.PieView_paid, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (unclaimed != 0 || claimed != 0 || paid != 0) {
            mRectF.set(0, 0, canvas.getWidth(), canvas.getHeight());
            float factor = 360 / (unclaimed + claimed + paid);
            float startAngle = -90;
            float sweepAngle;
            if (unclaimed != 0) {
                mPaint.setColor(unclaimedColor);
                sweepAngle = unclaimed * factor;
                canvas.drawArc(mRectF, startAngle, sweepAngle, true, mPaint);
                startAngle += sweepAngle;
            }
            if (claimed != 0) {
                mPaint.setColor(claimedColor);
                sweepAngle = claimed * factor;
                canvas.drawArc(mRectF, startAngle, sweepAngle, true, mPaint);
                startAngle += sweepAngle;
            }
            if (paid != 0) {
                mPaint.setColor(paidColor);
                sweepAngle = paid * factor;
                canvas.drawArc(mRectF, startAngle, sweepAngle, true, mPaint);
            }
        }
    }

    public void set(float unclaimed, float claimed, float paid) {
        this.unclaimed = unclaimed;
        this.claimed = claimed;
        this.paid = paid;
        invalidate();
    }
}
