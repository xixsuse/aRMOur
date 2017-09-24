package com.skepticalone.armour.data.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.Contract;

@Entity(tableName = Contract.AdditionalShifts.TABLE_NAME, indices = {@Index(value = {Contract.COLUMN_NAME_SHIFT_START}), @Index(value = {Contract.COLUMN_NAME_SHIFT_END})})
public final class RawAdditionalShiftEntity extends RawShift {

    @NonNull
    @Embedded
    private final RawPaymentData paymentData;
//    @NonNull
//    @Ignore
//    private final BigDecimal totalPayment;

    public RawAdditionalShiftEntity(
            long id,
            @Nullable String comment,
            @NonNull ShiftData shiftData,
            @NonNull RawPaymentData paymentData
    ) {
        super(id, comment, shiftData);
        this.paymentData = paymentData;
    }
//
//    public RawAdditionalShiftEntity(
//            long id,
//            @Nullable String comment,
//            @NonNull RawShift.ShiftData shiftData,
//            @NonNull RawPaymentData paymentData
//    ) {
//        super(id, shiftData, comment);
//        this.paymentData = paymentData;
//        totalPayment = this.paymentData.getPayment()
//                .multiply(BigDecimal.valueOf(Duration.between(getRawShiftData().getStart(), getRawShiftData().getEnd()).getSeconds()))
//                .divide(BigDecimal.valueOf(SECONDS_PER_HOUR), 2, BigDecimal.ROUND_HALF_UP);
//    }

    @NonNull
    public RawPaymentData getPaymentData() {
        return paymentData;
    }
//
//    @NonNull
//    @Override
//    public BigDecimal getTotalPayment() {
//        return totalPayment;
//    }
}
