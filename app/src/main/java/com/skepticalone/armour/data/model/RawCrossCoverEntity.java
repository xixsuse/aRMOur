package com.skepticalone.armour.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.Contract;

import org.threeten.bp.LocalDate;

@Entity(tableName = Contract.CrossCoverShifts.TABLE_NAME, indices = {@Index(value = {Contract.CrossCoverShifts.COLUMN_NAME_DATE}, unique = true)})
public final class RawCrossCoverEntity extends Item {
    @NonNull
    @ColumnInfo(name = Contract.CrossCoverShifts.COLUMN_NAME_DATE)
    private final LocalDate date;
    @NonNull
    @Embedded
    private final RawPaymentData paymentData;
    public RawCrossCoverEntity(
            long id,
            @Nullable String comment,
            @NonNull LocalDate date,
            @NonNull RawPaymentData paymentData
    ) {
        super(id, comment);
        this.date = date;
        this.paymentData = paymentData;
    }

    public static RawCrossCoverEntity from(@NonNull LocalDate date, int cents) {
        return new RawCrossCoverEntity(NO_ID, null, date, RawPaymentData.from(cents));
    }

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    @NonNull
    public RawPaymentData getPaymentData() {
        return paymentData;
    }
    //
//    @NonNull
//    public static LocalDate getNewDate(@Nullable final LocalDate lastDate) {
//        LocalDate newDate = LocalDate.now();
//        if (lastDate != null) {
//            LocalDate earliestShiftDate = lastDate.plusDays(1);
//            if (newDate.isBefore(earliestShiftDate)) newDate = earliestShiftDate;
//        }
//        return newDate;
//    }
//
//    @NonNull
//    @Override
//    public LocalDate getDate() {
//        return date;
//    }
//
//    @NonNull
//    @Override
//    public RawPaymentData getPaymentData() {
//        return paymentData;
//    }
//
//    @NonNull
//    @Override
//    public BigDecimal getTotalPayment() {
//        return paymentData.getPayment();
//    }
}
