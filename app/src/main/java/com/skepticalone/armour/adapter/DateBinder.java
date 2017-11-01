package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.LocalDate;

final class DateBinder extends ItemViewHolder.PlainBinder {

    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final LocalDate date;

    DateBinder(@NonNull Callbacks callbacks, @NonNull LocalDate date) {
        this.callbacks = callbacks;
        this.date = date;
    }

    @Override
    boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
        DateBinder newBinder = (DateBinder) other;
        return date.isEqual(newBinder.date);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_calendar_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.date);
    }

    @Nullable
    @Override
    String getSecondLine(@NonNull Context context) {
        return DateTimeUtils.getFullDateString(date);
    }

    @Override
    public void onClick(View v) {
        callbacks.changeDate();
    }

    interface Callbacks {
        void changeDate();
    }

}
