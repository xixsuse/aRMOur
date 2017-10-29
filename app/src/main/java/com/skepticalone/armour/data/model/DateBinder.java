package com.skepticalone.armour.data.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemViewHolder;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.LocalDate;

public final class DateBinder extends ItemViewHolder.PlainBinder {

    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final LocalDate date;

    public DateBinder(@NonNull Callbacks callbacks, @NonNull LocalDate date) {
        this.callbacks = callbacks;
        this.date = date;
    }

    @Override
    public boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
        DateBinder newBinder = (DateBinder) other;
        return date.isEqual(newBinder.date);
    }

    @Override
    public int getPrimaryIcon() {
        return R.drawable.ic_calendar_black_24dp;
    }

    @NonNull
    @Override
    public String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.date);
    }

    @Nullable
    @Override
    public String getSecondLine(@NonNull Context context) {
        return DateTimeUtils.getFullDateString(date);
    }

    @Override
    public boolean showSecondaryIcon() {
        return false;
    }

    @Override
    public int getSecondaryIcon() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onClick(View v) {
        callbacks.changeDate();
    }

    public interface Callbacks {
        void changeDate();
    }

}
