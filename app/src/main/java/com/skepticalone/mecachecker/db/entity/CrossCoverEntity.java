package com.skepticalone.mecachecker.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.model.CrossCover;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

import static com.skepticalone.mecachecker.db.Contract.CrossCoverShifts.COLUMN_NAME_CLAIMED;
import static com.skepticalone.mecachecker.db.Contract.CrossCoverShifts.COLUMN_NAME_COMMENT;
import static com.skepticalone.mecachecker.db.Contract.CrossCoverShifts.COLUMN_NAME_DATE;
import static com.skepticalone.mecachecker.db.Contract.CrossCoverShifts.COLUMN_NAME_PAID;
import static com.skepticalone.mecachecker.db.Contract.CrossCoverShifts.COLUMN_NAME_PAYMENT;
import static com.skepticalone.mecachecker.db.Contract.CrossCoverShifts.TABLE_NAME;
import static com.skepticalone.mecachecker.db.Contract.CrossCoverShifts._ID;

@Entity(tableName = TABLE_NAME, indices = {@Index(value = {COLUMN_NAME_DATE}, unique = true)})
public class CrossCoverEntity implements CrossCover {

    @NonNull
    @ColumnInfo(name = COLUMN_NAME_DATE)
    private final LocalDate date;
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

    public CrossCoverEntity(
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
