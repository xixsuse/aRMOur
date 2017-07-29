package com.skepticalone.mecachecker.data.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.db.Contract;
import com.skepticalone.mecachecker.data.model.AdditionalShift;
import com.skepticalone.mecachecker.data.util.PaymentData;
import com.skepticalone.mecachecker.data.util.ShiftData;

@Entity(tableName = Contract.AdditionalShifts.TABLE_NAME, indices = {@Index(name = Contract.AdditionalShifts.INDEX, value = {Contract.COLUMN_NAME_SHIFT_START})})
public final class AdditionalShiftEntity extends ItemEntity implements AdditionalShift {

    @NonNull
    @Embedded
    private final ShiftData shiftData;
    @NonNull
    @Embedded
    private final PaymentData paymentData;

    public AdditionalShiftEntity(
            @NonNull PaymentData paymentData,
            @NonNull ShiftData shiftData,
            @Nullable String comment
    ) {
        super(comment);
        this.shiftData = shiftData;
        this.paymentData = paymentData;
    }

    @NonNull
    @Override
    public ShiftData getShiftData() {
        return shiftData;
    }

    @NonNull
    @Override
    public PaymentData getPaymentData() {
        return paymentData;
    }


}
