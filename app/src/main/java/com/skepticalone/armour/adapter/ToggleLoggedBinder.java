package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.CompoundButton;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.DateTimeUtils;

final class ToggleLoggedBinder extends ItemViewHolder.SwitchBinder {

    @NonNull
    private final Callbacks callbacks;

    @Nullable
    private final Shift.Data loggedShiftData;

    ToggleLoggedBinder(@NonNull Callbacks callbacks, @Nullable Shift.Data loggedShiftData) {
        this.callbacks = callbacks;
        this.loggedShiftData = loggedShiftData;
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_clipboard_black_24dp;
    }

    @Override
    boolean isChecked() {
        return loggedShiftData != null;
    }

    @Override
    boolean isEnabled() {
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean logged) {
        callbacks.setLogged(logged);
    }

    @Override
    boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
        ToggleLoggedBinder newBinder = (ToggleLoggedBinder) other;
        return loggedShiftData == null ? newBinder.loggedShiftData == null : (newBinder.loggedShiftData != null && loggedShiftData.getDuration().equals(newBinder.loggedShiftData.getDuration()));
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.logged);
    }

    @Nullable
    @Override
    String getSecondLine(@NonNull Context context) {
        return loggedShiftData == null ? null : DateTimeUtils.getDurationString(context, loggedShiftData.getDuration());
    }

    interface Callbacks {
        void setLogged(boolean logged);
    }
}
