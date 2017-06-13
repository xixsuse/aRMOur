package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

abstract class AbstractData {
    static ViewHolderType getViewHolderType(int position, AbstractData... abstractDataObjects) {
        ViewHolderType type = null;
        for (AbstractData data : abstractDataObjects) {
            type = data.getViewHolderType(position);
            if (type != null) break;
        }
        return type;
    }

    @Nullable
    abstract ViewHolderType getViewHolderType(int position);

    abstract void readFromPositionedCursor(@NonNull Cursor cursor);

    boolean bindToHolder(Context context, PlainListItemViewHolder holder, int position) {
        return false;
    }

    boolean bindToHolder(Context context, SwitchListItemViewHolder holder, int position) {
        return false;
    }
}