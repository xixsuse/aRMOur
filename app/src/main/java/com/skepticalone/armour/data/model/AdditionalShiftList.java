package com.skepticalone.armour.data.model;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.List;

public final class AdditionalShiftList extends ShiftList<AdditionalShiftEntity, AdditionalShift> {

    public AdditionalShiftList(@NonNull Context context, @NonNull LiveData<List<AdditionalShiftEntity>> liveRawShifts) {
        super(context, liveRawShifts);
    }

    @Override
    void onUpdated(@Nullable List<AdditionalShiftEntity> rawAdditionalShifts, @Nullable ZoneId timeZone, @Nullable Shift.ShiftType.Configuration shiftConfig) {
        if (rawAdditionalShifts != null && timeZone != null && shiftConfig != null) {
            List<AdditionalShift> additionalShifts = new ArrayList<>(rawAdditionalShifts.size());
            for (AdditionalShiftEntity rawAdditionalShift : rawAdditionalShifts) {
                additionalShifts.add(new AdditionalShift(rawAdditionalShift, timeZone, shiftConfig));
            }
            setValue(additionalShifts);
        }
    }

}
