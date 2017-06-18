package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.math.BigDecimal;

@Entity
public class Expense {
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

    public Expense(
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public BigDecimal getPayment() {
        return payment;
    }

    @Nullable
    public String getComment() {
        return comment;
    }

    @Nullable
    public DateTime getClaimed() {
        return claimed;
    }

    @Nullable
    public DateTime getPaid() {
        return paid;
    }
}
