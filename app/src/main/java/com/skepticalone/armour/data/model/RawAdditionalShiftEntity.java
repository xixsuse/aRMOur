package com.skepticalone.armour.data.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.skepticalone.armour.data.db.Contract;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

@Entity(tableName = Contract.AdditionalShifts.TABLE_NAME, indices = {@Index(value = {Contract.COLUMN_NAME_SHIFT_START}), @Index(value = {Contract.COLUMN_NAME_SHIFT_END})})
public final class RawAdditionalShiftEntity extends RawShift {

    @NonNull
    @Embedded
    private final RawPaymentData paymentData;

    public RawAdditionalShiftEntity(
            long id,
            @Nullable String comment,
            @NonNull ShiftData shiftData,
            @NonNull RawPaymentData paymentData
    ) {
        super(id, comment, shiftData);
        this.paymentData = paymentData;
    }

    public static RawAdditionalShiftEntity from(@Nullable Instant lastShiftEnd, @NonNull Pair<LocalTime, LocalTime> times, @NonNull ZoneId zoneId, int hourlyRateInCents){
        return new RawAdditionalShiftEntity(
                NO_ID,
                null,
                ShiftData.withEarliestStart(times.first, times.second, lastShiftEnd, zoneId, false),
                RawPaymentData.from(hourlyRateInCents)
        );
    }

    @NonNull
    public RawPaymentData getPaymentData() {
        return paymentData;
    }

}
