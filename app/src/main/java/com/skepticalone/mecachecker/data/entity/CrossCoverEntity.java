package com.skepticalone.mecachecker.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.db.Contract;
import com.skepticalone.mecachecker.data.util.PaymentData;

import org.joda.time.LocalDate;

@Entity(tableName = Contract.CrossCoverShifts.TABLE_NAME, indices = {@Index(name = Contract.CrossCoverShifts.INDEX, value = {Contract.CrossCoverShifts.COLUMN_NAME_DATE}, unique = true)})
public final class CrossCoverEntity extends ItemEntity implements com.skepticalone.mecachecker.model.CrossCover {
    @NonNull
    @ColumnInfo(name = Contract.CrossCoverShifts.COLUMN_NAME_DATE)
    private final LocalDate date;
    @NonNull
    @Embedded
    private final PaymentData paymentData;
    public CrossCoverEntity(
            @NonNull LocalDate date,
            @NonNull PaymentData paymentData,
            @Nullable String comment
    ) {
        super(comment);
        this.date = date;
        this.paymentData = paymentData;
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
