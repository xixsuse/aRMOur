package com.skepticalone.mecachecker.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.math.BigDecimal;

interface Payable {

    @NonNull
    BigDecimal getPayment();

    @Nullable
    String getComment();

    @Nullable
    DateTime getClaimed();

    @Nullable
    DateTime getPaid();

}
