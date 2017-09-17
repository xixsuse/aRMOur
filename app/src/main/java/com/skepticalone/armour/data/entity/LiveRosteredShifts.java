package com.skepticalone.armour.data.entity;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class LiveRosteredShifts extends MediatorLiveData<List<RosteredShiftEntity>> {

//    @NonNull
//    private final LiveData<List<RawShift>> rawShifts;
//    @NonNull
//    private final LiveShiftTypeCalculator shiftTypeCalculator;
//    @NonNull
//    private final LiveComplianceChecker complianceChecker;

    public LiveRosteredShifts(@NonNull Application application, final @NonNull LiveData<List<RosteredShiftEntity>> shifts) {
        super();
        final LiveShiftTypeCalculator shiftTypeCalculator = LiveShiftTypeCalculator.getInstance(application);
        final LiveComplianceChecker complianceChecker = LiveComplianceChecker.getInstance(application);
        addSource(shifts, new Observer<List<RosteredShiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<RosteredShiftEntity> rawShifts) {
                updateSelf(rawShifts, shiftTypeCalculator.getValue(), complianceChecker.getValue());
            }
        });
        addSource(shiftTypeCalculator, new Observer<ShiftTypeCalculator>() {
            @Override
            public void onChanged(@Nullable ShiftTypeCalculator shiftTypeCalculator) {
                updateSelf(shifts.getValue(), shiftTypeCalculator, complianceChecker.getValue());
            }
        });
        addSource(complianceChecker, new Observer<ComplianceChecker>() {
            @Override
            public void onChanged(@Nullable ComplianceChecker complianceChecker) {
                updateSelf(shifts.getValue(), shiftTypeCalculator.getValue(), complianceChecker);
            }
        });
    }

    private void updateSelf(@Nullable List<RosteredShiftEntity> source, @Nullable ShiftTypeCalculator shiftTypeCalculator, @Nullable ComplianceChecker complianceChecker) {
        if (source != null && shiftTypeCalculator != null && complianceChecker != null) {
            List<RosteredShiftEntity> result = new ArrayList<>(source.size());
            for (RosteredShiftEntity sourceShift : source) {
                RosteredShiftEntity shift = sourceShift.copy();
                shiftTypeCalculator.process(shift);
                result.add(shift);
            }
            complianceChecker.process(result);
            setValue(result);
        }
    }

}
