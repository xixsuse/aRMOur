package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.model.Expense;

@Entity(tableName = Contract.Expenses.TABLE_NAME)
public class ExpenseEntity implements Expense {
    @NonNull
    @ColumnInfo(name = Contract.Expenses.COLUMN_NAME_TITLE)
    private final String title;
    @NonNull
    @Embedded
    private final PaymentData paymentData;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BaseColumns._ID)
    private long id = 0L;
    ExpenseEntity(
            @NonNull String title,
            @NonNull PaymentData paymentData
    ) {
        this.title = title;
        this.paymentData = paymentData;
    }
    @Override
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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
}
