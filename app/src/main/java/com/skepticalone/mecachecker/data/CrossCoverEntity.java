package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.model.CrossCover;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

@Entity(tableName = Contract.CrossCoverShifts.TABLE_NAME, indices = {@Index(value = {Contract.CrossCoverShifts.COLUMN_NAME_DATE}, unique = true)})
public class CrossCoverEntity implements CrossCover {

    @NonNull
    @ColumnInfo(name = Contract.CrossCoverShifts.COLUMN_NAME_DATE)
    private final LocalDate date;
    @NonNull
    @ColumnInfo(name = Contract.CrossCoverShifts.COLUMN_NAME_PAYMENT)
    private final BigDecimal payment;
    @Nullable
    @ColumnInfo(name = Contract.CrossCoverShifts.COLUMN_NAME_COMMENT)
    private final String comment;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BaseColumns._ID)
    private long id = 0L;
    @Nullable
    @ColumnInfo(name = Contract.CrossCoverShifts.COLUMN_NAME_CLAIMED)
    private DateTime claimed = null;
    @Nullable
    @ColumnInfo(name = Contract.CrossCoverShifts.COLUMN_NAME_PAID)
    private DateTime paid = null;

    CrossCoverEntity(
            @NonNull LocalDate date,
            @NonNull BigDecimal payment,
            @Nullable String comment) {
        this.date = date;
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
    public LocalDate getDate() {
        return date;
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
