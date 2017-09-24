package com.skepticalone.armour.data.model;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.List;

public final class LiveAdditionalShifts extends LiveShifts<RawAdditionalShiftEntity, AdditionalShift> {

    LiveAdditionalShifts(@NonNull Context context, @NonNull LiveData<List<RawAdditionalShiftEntity>> liveRawShifts) {
        super(context, liveRawShifts);
    }
    //
//    @NonNull
//    private final LiveData<Shift.ShiftType.Configuration> liveShiftConfig;
//
//    LiveAdditionalShifts(@NonNull Context context, @NonNull LiveData<List<RawAdditionalShiftEntity>> liveRawAdditionalShifts) {
//        super(context, liveRawAdditionalShifts);
//        liveShiftConfig = Shift.ShiftType.LiveShiftConfig.getInstance(context);
//        addSource(liveShiftConfig, new Observer<Shift.ShiftType.Configuration>() {
//            @Override
//            public void onChanged(@Nullable Shift.ShiftType.Configuration shiftConfig) {
//                onUpdated(liveRawItems.getValue(), liveTimeZone.getValue(), shiftConfig);
//            }
//        });
//    }

    @Override
    void onUpdated(@Nullable List<RawAdditionalShiftEntity> rawAdditionalShifts, @Nullable ZoneId timeZone, @Nullable Shift.ShiftType.Configuration shiftConfig) {
        if (rawAdditionalShifts != null && timeZone != null && shiftConfig != null) {
            List<AdditionalShift> additionalShifts = new ArrayList<>(rawAdditionalShifts.size());
            for (RawAdditionalShiftEntity rawAdditionalShift : rawAdditionalShifts) {
                additionalShifts.add(new AdditionalShift(rawAdditionalShift, timeZone, shiftConfig));
            }
            setValue(additionalShifts);
        }
    }

}
