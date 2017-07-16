package com.skepticalone.mecachecker.model;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.PaymentData;

public interface PayableItem extends Item {

    @NonNull
    PaymentData getPaymentData();

}
