package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.Duration;

final class ShiftTypeBinder extends ItemViewHolder.PlainBinder {

    @NonNull
    private final Shift.ShiftType shiftType;
    @NonNull
    private final Duration duration;

    ShiftTypeBinder(@NonNull Shift.ShiftType shiftType, @NonNull Duration duration) {
        this.shiftType = shiftType;
        this.duration = duration;
    }

    @Override
    boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
        ShiftTypeBinder newBinder = (ShiftTypeBinder) other;
        return shiftType == newBinder.shiftType && duration.equals(newBinder.duration);
    }

    @Override
    int getPrimaryIcon() {
        return shiftType.getIcon();
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(shiftType.getSingularTitle());
    }

    @Nullable
    @Override
    String getSecondLine(@NonNull Context context) {
        return DateTimeUtils.getDurationString(context, duration);
    }

}
