package com.skepticalone.armour.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.Contract;

@Entity(tableName = Contract.Expenses.TABLE_NAME)
public final class ExpenseEntity extends Item {
    @NonNull
    @ColumnInfo(name = Contract.Expenses.COLUMN_NAME_TITLE)
    private final String title;
    @NonNull
    @Embedded
    private final PaymentData paymentData;

    @SuppressWarnings("SameParameterValue")
    public ExpenseEntity(
            long id,
            @Nullable String comment,
            @NonNull String title,
            @NonNull PaymentData paymentData
    ) {
        super(id, comment);
        this.title = title;
        this.paymentData = paymentData;
    }

    public static ExpenseEntity from(@NonNull String title) {
        return new ExpenseEntity(NO_ID, null, title, PaymentData.from(0));
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public PaymentData getPaymentData() {
        return paymentData;
    }

}
