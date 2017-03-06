package com.skepticalone.mecachecker.components;

import android.database.Cursor;

abstract class StableCursorAdapter<T extends Cursor> extends CursorAdapter<T> {

    StableCursorAdapter() {
        super();
        setHasStableIds(true);
    }

    @Override
    abstract public long getItemId(int position);

    @Override
    public final int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

}
