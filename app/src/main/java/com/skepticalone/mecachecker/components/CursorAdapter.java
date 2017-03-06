package com.skepticalone.mecachecker.components;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

abstract class CursorAdapter<T extends Cursor> extends RecyclerView.Adapter<TwoLineViewHolder> {

    @Nullable
    T mCursor = null;

    CursorAdapter() {
        super();
    }

    final void swapCursor(@Nullable T cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public TwoLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TwoLineViewHolder(parent);
    }

    @Override
    abstract public int getItemCount();
}
