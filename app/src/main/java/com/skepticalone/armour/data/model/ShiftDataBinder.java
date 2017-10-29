package com.skepticalone.armour.data.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemViewHolder;
import com.skepticalone.armour.util.DateTimeUtils;

public final class ShiftDataBinder extends ItemViewHolder.PlainBinder {

    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final Shift.Data shift;
    private final boolean start, logged;

    public ShiftDataBinder(@NonNull Callbacks callbacks, @NonNull Shift.Data shift, boolean start, boolean logged) {
        this.callbacks = callbacks;
        this.shift = shift;
        this.start = start;
        this.logged = logged;
    }

    @Override
    public boolean areItemsTheSame(@NonNull ItemViewHolder.Binder other) {
        if (!super.areItemsTheSame(other)) return false;
        ShiftDataBinder newBinder = (ShiftDataBinder) other;
        return start == newBinder.start && logged == newBinder.logged && shift.isEqual(newBinder.shift);
    }

    @Override
    public boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
        return shift.isEqual(((ShiftDataBinder) other).shift);
    }

    @Override
    public int getPrimaryIcon() {
        return !logged ? (start ? R.drawable.ic_play_black_24dp : R.drawable.ic_stop_black_24dp) : (start ? R.drawable.ic_clipboard_play_black_24dp : R.drawable.ic_clipboard_stop_black_24dp);
    }

    @NonNull
    @Override
    public String getFirstLine(@NonNull Context context) {
        return context.getString(!logged ? (start ? R.string.start : R.string.end) : (start ? R.string.logged_start : R.string.logged_end));
    }

    @Nullable
    @Override
    public String getSecondLine(@NonNull Context context) {
        return start ? DateTimeUtils.getTimeString(shift.getStart().toLocalTime()) : DateTimeUtils.getEndTimeString(shift.getEnd().toLocalDateTime(), shift.getStart().toLocalDate());
    }

    @Override
    public void onClick(View v) {
        callbacks.changeTime(start, logged);
    }

    public interface Callbacks {
        void changeTime(boolean start, boolean logged);
    }

}
