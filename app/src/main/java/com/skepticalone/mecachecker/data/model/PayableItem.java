package com.skepticalone.mecachecker.data.model;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.util.PaymentData;

public interface PayableItem extends Item {

    @NonNull
    PaymentData getPaymentData();

}
