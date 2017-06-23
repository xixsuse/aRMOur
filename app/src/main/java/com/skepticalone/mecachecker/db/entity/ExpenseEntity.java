package com.skepticalone.mecachecker.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.model.Expense;

import org.joda.time.DateTime;

import java.math.BigDecimal;

@Entity(tableName = "expenses")
public class ExpenseEntity implements Expense {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String title;
    @NonNull
    private BigDecimal payment;
    @Nullable
    private String comment;
    @Nullable
    private DateTime claimed;
    @Nullable
    private DateTime paid;

    public ExpenseEntity(
            @NonNull String title,
            @NonNull BigDecimal payment,
            @Nullable String comment,
            @Nullable DateTime claimed,
            @Nullable DateTime paid
    ) {
        this.title = title;
        this.payment = payment;
        this.comment = comment;
        this.claimed = claimed;
        this.paid = claimed == null ? null : paid;
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
