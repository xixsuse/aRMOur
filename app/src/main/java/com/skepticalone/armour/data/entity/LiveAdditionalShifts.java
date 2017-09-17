package com.skepticalone.armour.data.entity;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public final class LiveAdditionalShifts extends MediatorLiveData<List<AdditionalShiftEntity>> {

    public LiveAdditionalShifts(@NonNull Application application, final @NonNull LiveData<List<AdditionalShiftEntity>> shifts) {
        super();
        final LiveShiftTypeCalculator shiftTypeCalculator = LiveShiftTypeCalculator.getInstance(application);
        addSource(shifts, new Observer<List<AdditionalShiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<AdditionalShiftEntity> rawShifts) {
                updateSelf(rawShifts, shiftTypeCalculator.getValue());
            }
        });
        addSource(shiftTypeCalculator, new Observer<ShiftTypeCalculator>() {
            @Override
            public void onChanged(@Nullable ShiftTypeCalculator shiftTypeCalculator) {
                updateSelf(shifts.getValue(), shiftTypeCalculator);
            }
        });
    }

    private void updateSelf(@Nullable List<AdditionalShiftEntity> shifts, @Nullable ShiftTypeCalculator shiftTypeCalculator) {
        if (shifts != null && shiftTypeCalculator != null) {
            for (AdditionalShiftEntity shift : shifts) {
                shiftTypeCalculator.process(shift);
            }
            setValue(shifts);
        }
    }

}
