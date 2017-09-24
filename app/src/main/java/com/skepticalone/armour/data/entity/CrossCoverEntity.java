package com.skepticalone.armour.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.model.CrossCover;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

@Entity(tableName = Contract.CrossCoverShifts.TABLE_NAME, indices = {@Index(value = {Contract.CrossCoverShifts.COLUMN_NAME_DATE}, unique = true)})
public final class CrossCoverEntity extends ItemEntity implements CrossCover {
    @NonNull
    @ColumnInfo(name = Contract.CrossCoverShifts.COLUMN_NAME_DATE)
    private final LocalDate date;
    @NonNull
    @Embedded
    private final PaymentData paymentData;
    public CrossCoverEntity(
            long id,
            @NonNull LocalDate date,
            @NonNull PaymentData paymentData,
            @SuppressWarnings("SameParameterValue") @Nullable String comment
    ) {
        super(id, comment);
        this.date = date;
        this.paymentData = paymentData;
    }

    @NonNull
    public static LocalDate getNewDate(@Nullable final LocalDate lastDate) {
        LocalDate newDate = LocalDate.now();
        if (lastDate != null) {
            LocalDate earliestShiftDate = lastDate.plusDays(1);
            if (newDate.isBefore(earliestShiftDate)) newDate = earliestShiftDate;
        }
        return newDate;
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

    @NonNull
    @Override
    public BigDecimal getPayment() {
        return paymentData.getPayment();
    }
}
