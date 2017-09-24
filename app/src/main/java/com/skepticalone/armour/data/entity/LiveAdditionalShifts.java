package com.skepticalone.armour.data.entity;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.ZoneId;

import java.util.List;

public final class LiveAdditionalShifts extends MediatorLiveData<List<AdditionalShiftEntity>> {

    public LiveAdditionalShifts(@NonNull Application application, final @NonNull LiveData<List<AdditionalShiftEntity>> shifts) {
        super();
        final LiveShiftConfig liveShiftConfig = LiveShiftConfig.getInstance(application);
        addSource(shifts, new Observer<List<AdditionalShiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<AdditionalShiftEntity> rawShifts) {
                updateSelf(rawShifts, liveShiftConfig.getValue());
            }
        });
        addSource(liveShiftConfig, new Observer<ShiftConfig>() {
            @Override
            public void onChanged(@Nullable ShiftConfig shiftConfig) {
                updateSelf(shifts.getValue(), shiftConfig);
            }
        });
    }

    private void updateSelf(@Nullable List<AdditionalShiftEntity> shifts, @Nullable ShiftConfig shiftConfig) {
        if (shifts != null && shiftConfig != null) {
            ZoneId zoneId = shiftConfig.getZoneId();
            for (AdditionalShiftEntity shift : shifts) {
                shiftConfig.process(shift, zoneId);
            }
            setValue(shifts);
        }
    }

}
