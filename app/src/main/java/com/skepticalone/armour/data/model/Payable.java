package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.entity.PaymentData;

import java.math.BigDecimal;

public interface Payable extends Item {

    @NonNull
    PaymentData getPaymentData();

    @NonNull
    BigDecimal getPayment();

}
