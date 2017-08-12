package com.skepticalone.armour.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.model.Expense;

import java.math.BigDecimal;

@Entity(tableName = Contract.Expenses.TABLE_NAME)
public final class ExpenseEntity extends ItemEntity implements Expense {
    @NonNull
    @ColumnInfo(name = Contract.Expenses.COLUMN_NAME_TITLE)
    private final String title;
    @NonNull
    @Embedded
    private final PaymentData paymentData;
    public ExpenseEntity(
            @NonNull String title, @NonNull PaymentData paymentData, @SuppressWarnings("SameParameterValue") @Nullable String comment
    ) {
        super(comment);
        this.paymentData = paymentData;
        this.title = title;
    }
    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public PaymentData getPaymentData() {
        return paymentData;
    }

    @NonNull
    @Override
    public BigDecimal getPayment() {
        return paymentData.getPayment();
    }
}
