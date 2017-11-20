package com.skepticalone.armour.help;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

final class HelpItemDivider extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    @NonNull
    private final Drawable mDivider;
    @NonNull
    private final Callbacks callbacks;
    private final Rect mBounds = new Rect();

    HelpItemDivider(@NonNull Context context, @NonNull Callbacks callbacks) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        //noinspection ConstantConditions
        mDivider = a.getDrawable(0);
        a.recycle();
        this.callbacks = callbacks;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, callbacks.shouldHaveDividerBelow(parent.getChildAdapterPosition(view)) ? mDivider.getIntrinsicHeight() : 0);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        canvas.save();
        final int left, right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (callbacks.shouldHaveDividerBelow(parent.getChildAdapterPosition(child))) {
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
                final int top = bottom - mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
        }
        canvas.restore();
    }

    interface Callbacks {
        boolean shouldHaveDividerBelow(int position);
    }

}
