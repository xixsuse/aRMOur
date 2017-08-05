package com.skepticalone.mecachecker.data.model;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.util.PaymentData;

import java.math.BigDecimal;

public interface Payable extends Item {

    @NonNull
    PaymentData getPaymentData();

    @NonNull
    BigDecimal getPayment();

}
