package com.skepticalone.mecachecker.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.model.Expense;

import org.joda.time.DateTime;

import java.math.BigDecimal;

import static com.skepticalone.mecachecker.db.Contract.Expenses.COLUMN_NAME_CLAIMED;
import static com.skepticalone.mecachecker.db.Contract.Expenses.COLUMN_NAME_COMMENT;
import static com.skepticalone.mecachecker.db.Contract.Expenses.COLUMN_NAME_PAID;
import static com.skepticalone.mecachecker.db.Contract.Expenses.COLUMN_NAME_PAYMENT;
import static com.skepticalone.mecachecker.db.Contract.Expenses.COLUMN_NAME_TITLE;
import static com.skepticalone.mecachecker.db.Contract.Expenses.TABLE_NAME;
import static com.skepticalone.mecachecker.db.Contract.Expenses._ID;

@Entity(tableName = TABLE_NAME)
public class ExpenseEntity implements Expense {

    @NonNull
    @ColumnInfo(name = COLUMN_NAME_TITLE)
    private final String title;
    @NonNull
    @ColumnInfo(name = COLUMN_NAME_PAYMENT)
    private final BigDecimal payment;
    @Nullable
    @ColumnInfo(name = COLUMN_NAME_COMMENT)
    private final String comment;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _ID)
    private long id = 0L;
    @Nullable
    @ColumnInfo(name = COLUMN_NAME_CLAIMED)
    private DateTime claimed = null;
    @Nullable
    @ColumnInfo(name = COLUMN_NAME_PAID)
    private DateTime paid = null;

    public ExpenseEntity(
            @NonNull String title,
            @NonNull BigDecimal payment,
            @Nullable String comment) {
        this.title = title;
        this.payment = payment;
        this.comment = comment;
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
    public BigDecimal getPayment() {
        return payment;
    }

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    @Nullable
    @Override
    public DateTime getClaimed() {
        return claimed;
    }

    public void setClaimed(@Nullable DateTime claimed) {
        this.claimed = claimed;
    }

    @Nullable
    @Override
    public DateTime getPaid() {
        return paid;
    }

    public void setPaid(@Nullable DateTime paid) {
        this.paid = paid;
    }
}
