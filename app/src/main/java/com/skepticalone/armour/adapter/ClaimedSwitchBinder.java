package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.CompoundButton;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Payment;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

final class ClaimedSwitchBinder extends ItemViewHolder.SwitchBinder {

    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final Payment.Data payment;

    ClaimedSwitchBinder(@NonNull Callbacks callbacks, @NonNull Payment.Data payment) {
        this.callbacks = callbacks;
        this.payment = payment;
    }

    @Override
    boolean isChecked() {
        return payment.getClaimed() != null;
    }

    @Override
    boolean isEnabled() {
        return payment.getPaid() == null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean claimed) {
        callbacks.setClaimed(claimed);
    }

    @Override
    boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
        ClaimedSwitchBinder newBinder = (ClaimedSwitchBinder) other;
        return Comparators.equalDateTimes(payment.getClaimed(), newBinder.payment.getClaimed()) && ((payment.getPaid() == null) == (newBinder.payment.getPaid() == null));
    }

    @Override
    int getPrimaryIcon() {
        return payment.getClaimed() == null ? 0 : R.drawable.claimed_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.claimed);
    }

    @Nullable
    @Override
    String getSecondLine(@NonNull Context context) {
        return payment.getClaimed() == null ? null : DateTimeUtils.getDateTimeString(payment.getClaimed().toLocalDateTime());
    }

    interface Callbacks {
        void setClaimed(boolean claimed);
    }
}
