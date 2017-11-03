package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;

import java.math.BigDecimal;

final class TotalPaymentBinder extends ItemViewHolder.PlainBinder {

    @NonNull
    private final BigDecimal totalPayment;

    TotalPaymentBinder(@NonNull BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    @Override
    boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
        TotalPaymentBinder newBinder = (TotalPaymentBinder) other;
        return totalPayment.equals(newBinder.totalPayment);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_dollar_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.total_payment);
    }

    @NonNull
    @Override
    String getSecondLine(@NonNull Context context) {
        return context.getString(R.string.payment_format, totalPayment);
    }

}
