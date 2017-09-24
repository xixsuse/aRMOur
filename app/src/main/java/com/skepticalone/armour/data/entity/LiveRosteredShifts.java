package com.skepticalone.armour.data.entity;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.List;

public final class LiveRosteredShifts extends MediatorLiveData<List<RosteredShiftEntity>> {

    public LiveRosteredShifts(@NonNull Application application, final @NonNull LiveData<List<RosteredShiftEntity>> shifts) {
        super();
        final LiveShiftConfig liveShiftConfig = LiveShiftConfig.getInstance(application);
        final LiveComplianceConfig liveComplianceConfig = LiveComplianceConfig.getInstance(application);
        addSource(shifts, new Observer<List<RosteredShiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<RosteredShiftEntity> rawShifts) {
                updateSelf(rawShifts, liveShiftConfig.getValue(), liveComplianceConfig.getValue());
            }
        });
        addSource(liveShiftConfig, new Observer<ShiftConfig>() {
            @Override
            public void onChanged(@Nullable ShiftConfig shiftConfig) {
                updateSelf(shifts.getValue(), shiftConfig, liveComplianceConfig.getValue());
            }
        });
        addSource(liveComplianceConfig, new Observer<ComplianceConfig>() {
            @Override
            public void onChanged(@Nullable ComplianceConfig complianceConfig) {
                updateSelf(shifts.getValue(), liveShiftConfig.getValue(), complianceConfig);
            }
        });
    }

    private void updateSelf(@Nullable List<RosteredShiftEntity> source, @Nullable ShiftConfig shiftConfig, @Nullable ComplianceConfig complianceConfig) {
        if (source != null && shiftConfig != null && complianceConfig != null) {
            ZoneId zoneId = shiftConfig.getZoneId();
            List<RosteredShiftEntity> result = new ArrayList<>(source.size());
            for (RosteredShiftEntity sourceShift : source) {
                RosteredShiftEntity shift = sourceShift.copy();
                shiftConfig.process(shift, zoneId);
                result.add(shift);
            }
            complianceConfig.process(result, zoneId);
            setValue(result);
        }
    }

}
