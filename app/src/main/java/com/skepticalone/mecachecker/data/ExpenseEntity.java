package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.model.Expense;

@Entity(tableName = Contract.Expenses.TABLE_NAME)
public class ExpenseEntity extends PayableItemEntity implements Expense {
    @NonNull
    @ColumnInfo(name = Contract.Expenses.COLUMN_NAME_TITLE)
    private final String title;
    ExpenseEntity(
            @Nullable String comment,
            @NonNull PaymentData paymentData,
            @NonNull String title
    ) {
        super(comment, paymentData);
        this.title = title;
    }
    @NonNull
    @Override
    public String getTitle() {
        return title;
    }
}
