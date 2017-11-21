package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.CompoundButton;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Payment;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

final class PaidSwitchBinder extends ItemViewHolder.SwitchBinder {

    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final Payment.Data payment;

    PaidSwitchBinder(@NonNull Callbacks callbacks, @NonNull Payment.Data payment) {
        this.callbacks = callbacks;
        this.payment = payment;
    }

    @Override
    boolean isChecked() {
        return payment.getPaid() != null;
    }

    @Override
    boolean isEnabled() {
        return payment.getClaimed() != null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean paid) {
        callbacks.setPaid(paid);
    }

    @Override
    boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
        PaidSwitchBinder newBinder = (PaidSwitchBinder) other;
        return Comparators.equalDateTimes(payment.getPaid(), newBinder.payment.getPaid()) && ((payment.getClaimed() == null) == (newBinder.payment.getClaimed() == null));
    }

    @Override
    int getPrimaryIcon() {
        return payment.getPaid() == null ? 0 : R.drawable.ic_paid_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.paid);
    }

    @Nullable
    @Override
    String getSecondLine(@NonNull Context context) {
        return payment.getPaid() == null ? null : DateTimeUtils.getDateTimeString(payment.getPaid().toLocalDateTime());
    }

    interface Callbacks {
        void setPaid(boolean paid);
    }
}
