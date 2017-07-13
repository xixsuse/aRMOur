package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.model.CrossCover;

import org.joda.time.LocalDate;

@Entity(tableName = Contract.CrossCoverShifts.TABLE_NAME, indices = {@Index(value = {Contract.CrossCoverShifts.COLUMN_NAME_DATE}, unique = true)})
public class CrossCoverEntity implements CrossCover {
    @NonNull
    @ColumnInfo(name = Contract.CrossCoverShifts.COLUMN_NAME_DATE)
    private final LocalDate date;
    @NonNull
    @Embedded
    private final PaymentData paymentData;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BaseColumns._ID)
    private long id = 0L;
    CrossCoverEntity(
            @NonNull LocalDate date,
            @NonNull PaymentData paymentData
    ) {
        this.date = date;
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
    public LocalDate getDate() {
        return date;
    }
    @NonNull
    @Override
    public PaymentData getPaymentData() {
        return paymentData;
    }
}
