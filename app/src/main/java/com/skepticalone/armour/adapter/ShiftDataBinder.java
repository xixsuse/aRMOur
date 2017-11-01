package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.DateTimeUtils;

final class ShiftDataBinder extends ItemViewHolder.PlainBinder {

    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final Shift.Data shift;
    private final boolean start, logged;

    ShiftDataBinder(@NonNull Callbacks callbacks, @NonNull Shift.Data shift, boolean start, boolean logged) {
        this.callbacks = callbacks;
        this.shift = shift;
        this.start = start;
        this.logged = logged;
    }

    @Override
    boolean areItemsTheSame(@NonNull ItemViewHolder.Binder other) {
        if (!super.areItemsTheSame(other)) return false;
        ShiftDataBinder newBinder = (ShiftDataBinder) other;
        return start == newBinder.start && logged == newBinder.logged;
    }

    @Override
    boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
        ShiftDataBinder newBinder = (ShiftDataBinder) other;
        if (start) {
            return shift.getStart().toLocalTime().equals(newBinder.shift.getStart().toLocalTime());
        } else {
            return shift.getStart().toLocalDate().isEqual(newBinder.shift.getStart().toLocalDate()) && shift.getEnd().toLocalTime().equals(newBinder.shift.getEnd().toLocalTime());
        }
    }

    @Override
    int getPrimaryIcon() {
        return !logged ? (start ? R.drawable.ic_play_black_24dp : R.drawable.ic_stop_black_24dp) : (start ? R.drawable.ic_clipboard_play_black_24dp : R.drawable.ic_clipboard_stop_black_24dp);
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(!logged ? (start ? R.string.start : R.string.end) : (start ? R.string.logged_start : R.string.logged_end));
    }

    @Nullable
    @Override
    String getSecondLine(@NonNull Context context) {
        return start ? DateTimeUtils.getTimeString(shift.getStart().toLocalTime()) : DateTimeUtils.getEndTimeString(shift.getEnd().toLocalDateTime(), shift.getStart().toLocalDate());
    }

    @Override
    public void onClick(View v) {
        callbacks.changeTime(start, logged);
    }

    interface Callbacks {
        void changeTime(boolean start, boolean logged);
    }

}
