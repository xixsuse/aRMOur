package com.skepticalone.armour.data.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.List;

public final class LiveRosteredShifts extends LiveShifts<RawRosteredShiftEntity, RosteredShift> {

    @NonNull
    private final LiveData<RosteredShift.Compliance.Configuration> liveComplianceConfig;

    public LiveRosteredShifts(@NonNull Context context, @NonNull LiveData<List<RawRosteredShiftEntity>> liveRawRosteredShifts) {
        super(context, liveRawRosteredShifts);
        liveComplianceConfig = RosteredShift.Compliance.LiveComplianceConfig.getInstance(context);
        addSource(liveComplianceConfig, new Observer<RosteredShift.Compliance.Configuration>() {
            @Override
            public void onChanged(@Nullable RosteredShift.Compliance.Configuration complianceConfig) {
                onUpdated(liveRawItems.getValue(), liveTimeZone.getValue(), liveShiftConfig.getValue(), complianceConfig);
            }
        });
    }

    @Override
    void onUpdated(@Nullable List<RawRosteredShiftEntity> rawRosteredShifts, @Nullable ZoneId timeZone, @Nullable Shift.ShiftType.Configuration shiftConfig) {
        onUpdated(rawRosteredShifts, timeZone, shiftConfig, liveComplianceConfig.getValue());
    }

    private void onUpdated(@Nullable List<RawRosteredShiftEntity> rawRosteredShifts, @Nullable ZoneId timeZone, @Nullable Shift.ShiftType.Configuration shiftConfig, @Nullable RosteredShift.Compliance.Configuration complianceConfig) {
        if (rawRosteredShifts != null && timeZone != null && shiftConfig != null && complianceConfig != null) {
            List<RosteredShift> rosteredShifts = new ArrayList<>(rawRosteredShifts.size());
            for (RawRosteredShiftEntity rawRosteredShift : rawRosteredShifts) {
                rosteredShifts.add(new RosteredShift(rawRosteredShift, timeZone, shiftConfig, rosteredShifts, complianceConfig));
            }
            setValue(rosteredShifts);
        }
    }

}
