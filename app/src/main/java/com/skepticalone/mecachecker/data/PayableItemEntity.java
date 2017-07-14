package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.Embedded;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.model.PayableItem;

abstract class PayableItemEntity extends ItemEntity implements PayableItem {
    @NonNull
    @Embedded
    private final PaymentData paymentData;

    PayableItemEntity(@Nullable String comment, @NonNull PaymentData paymentData) {
        super(comment);
        this.paymentData = paymentData;
    }

    @NonNull
    @Override
    public final PaymentData getPaymentData() {
        return paymentData;
    }
}
