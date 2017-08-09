package com.skepticalone.armour.data.util;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.db.Contract;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public final class PaymentData {

    @NonNull
    @ColumnInfo(name = Contract.COLUMN_NAME_PAYMENT)
    private final BigDecimal payment;

    @Nullable
    @ColumnInfo(name = Contract.COLUMN_NAME_CLAIMED)
    private final DateTime claimed;

    @Nullable
    @ColumnInfo(name = Contract.COLUMN_NAME_PAID)
    private final DateTime paid;
//
//    @Nullable
//    @ColumnInfo(name = Contract.COLUMN_NAME_COMMENT)
//    private final String comment;

    @SuppressWarnings("WeakerAccess")
    public PaymentData(
            @NonNull BigDecimal payment,
            @SuppressWarnings("SameParameterValue") @Nullable DateTime claimed,
            @SuppressWarnings("SameParameterValue") @Nullable DateTime paid
//            @Nullable String comment
    ) {
        this.payment = payment;
        this.claimed = claimed;
        this.paid = claimed == null ? null : paid;
//        this.comment = comment;
    }

    @NonNull
    public static PaymentData fromPayment(int payment) {
        return new PaymentData(MoneyConverter.centsToMoney(payment), null, null);
    }

    @NonNull
    public BigDecimal getPayment() {
        return payment;
    }

    @Nullable
    public DateTime getClaimed() {
        return claimed;
    }

    @Nullable
    public DateTime getPaid() {
        return paid;
    }

    @DrawableRes
    public int getIcon() {
        return paid == null ? claimed == null ? R.drawable.unclaimed_black_24dp : R.drawable.claimed_black_24dp : R.drawable.paid_black_24dp;
    }

}
