package com.skepticalone.armour.data.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.model.AdditionalShift;

import org.joda.time.DateTimeConstants;

import java.math.BigDecimal;

@Entity(tableName = Contract.AdditionalShifts.TABLE_NAME, indices = {@Index(value = {Contract.COLUMN_NAME_SHIFT_START}), @Index(value = {Contract.COLUMN_NAME_SHIFT_END})})
public final class AdditionalShiftEntity extends ShiftEntity implements AdditionalShift {

    @NonNull
    @Embedded
    private final PaymentData paymentData;
    @NonNull
    @Ignore
    private final BigDecimal totalPayment;

    public AdditionalShiftEntity(
            @NonNull ShiftData shiftData,
            @Nullable String comment,
            @NonNull PaymentData paymentData
    ) {
        super(shiftData, comment);
        this.paymentData = paymentData;
        totalPayment = this.paymentData.getPayment()
                .multiply(BigDecimal.valueOf(getShiftData().getDuration().getMillis()))
                .divide(BigDecimal.valueOf(DateTimeConstants.MILLIS_PER_HOUR), 2, BigDecimal.ROUND_HALF_UP);
    }

    @NonNull
    @Override
    public PaymentData getPaymentData() {
        return paymentData;
    }

    @NonNull
    @Override
    public BigDecimal getPayment() {
        return totalPayment;
    }
}
