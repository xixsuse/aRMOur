package com.skepticalone.armour.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.util.MoneyConverter;

import org.threeten.bp.Instant;

import java.math.BigDecimal;

public final class PaymentData {

    @NonNull
    @ColumnInfo(name = Contract.COLUMN_NAME_PAYMENT)
    private final BigDecimal payment;

    @Nullable
    @ColumnInfo(name = Contract.COLUMN_NAME_CLAIMED)
    private final Instant claimed;

    @Nullable
    @ColumnInfo(name = Contract.COLUMN_NAME_PAID)
    private final Instant paid;

    @SuppressWarnings({"SameParameterValue", "WeakerAccess"})
    public PaymentData(
            @NonNull BigDecimal payment,
            @Nullable Instant claimed,
            @Nullable Instant paid
    ) {
        this.payment = payment;
        this.claimed = claimed;
        this.paid = claimed == null ? null : paid;
    }

    static PaymentData from(int cents) {
        return new PaymentData(MoneyConverter.centsToMoney(cents), null, null);
    }

    @NonNull
    public BigDecimal getPayment() {
        return payment;
    }

    @Nullable
    public Instant getClaimed() {
        return claimed;
    }

    @Nullable
    public Instant getPaid() {
        return paid;
    }

}
