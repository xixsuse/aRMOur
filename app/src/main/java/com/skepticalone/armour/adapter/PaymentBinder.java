package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

import com.skepticalone.armour.R;

import java.math.BigDecimal;

final class PaymentBinder extends ItemViewHolder.PlainBinder {
    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final BigDecimal payment;
    @DrawableRes
    private final int paymentIcon;
    @StringRes
    private final int paymentTitle;

    PaymentBinder(@NonNull Callbacks callbacks, @NonNull BigDecimal payment, @DrawableRes int paymentIcon, @StringRes int paymentTitle) {
        this.callbacks = callbacks;
        this.payment = payment;
        this.paymentIcon = paymentIcon;
        this.paymentTitle = paymentTitle;
    }

    @Override
    boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
        PaymentBinder newBinder = (PaymentBinder) other;
        return payment.equals(newBinder.payment);
    }

    @Override
    int getPrimaryIcon() {
        return paymentIcon;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(paymentTitle);
    }

    @Override
    public void onClick(View v) {
        callbacks.changePayment();
    }

    @NonNull
    @Override
    String getSecondLine(@NonNull Context context) {
        return context.getString(R.string.payment_format, payment);
    }

    interface Callbacks {
        void changePayment();
    }

}
