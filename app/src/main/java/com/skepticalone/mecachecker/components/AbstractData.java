package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

abstract class AbstractData {
    static boolean isSwitchType(int position, @NonNull AbstractData... data) throws IllegalStateException {
        for (AbstractData datum : data) {
            try {
                return datum.isSwitchType(position);
            } catch (IllegalStateException e) {
                // continue
            }
        }
        throw new IllegalStateException();
    }

    abstract void readFromPositionedCursor(@NonNull Cursor cursor);

    boolean bindToHolder(Context context, PlainListItemViewHolder holder, int position) {
        return false;
    }

    boolean bindToHolder(Context context, SwitchListItemViewHolder holder, int position) {
        return false;
    }

    abstract boolean isSwitchType(int position) throws IllegalStateException;
}
