package com.skepticalone.mecachecker.data.model;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

public interface AdditionalShift extends Shift, Payable {
    @NonNull
    BigDecimal getTotalPayment();
}
