package com.skepticalone.mecachecker.composition;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.components.ListItemViewHolder;

public abstract class AbstractData {

    public abstract void readFromPositionedCursor(@NonNull Cursor cursor);

    public abstract boolean bindToHolder(Context context, ListItemViewHolder holder, int position);

}