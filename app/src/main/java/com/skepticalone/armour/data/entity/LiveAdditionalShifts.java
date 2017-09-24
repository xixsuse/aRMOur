package com.skepticalone.armour.data.entity;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RawAdditionalShiftEntity;

import org.threeten.bp.ZoneId;

import java.util.List;

public final class LiveAdditionalShifts extends MediatorLiveData<List<RawAdditionalShiftEntity>> {

    public LiveAdditionalShifts(@NonNull Application application, final @NonNull LiveData<List<RawAdditionalShiftEntity>> shifts) {
        super();
        final LiveShiftConfig liveShiftConfig = LiveShiftConfig.getInstance(application);
        addSource(shifts, new Observer<List<RawAdditionalShiftEntity>>() {
            @Override
            public void onChanged(@Nullable List<RawAdditionalShiftEntity> rawShifts) {
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

    private void updateSelf(@Nullable List<RawAdditionalShiftEntity> shifts, @Nullable ShiftConfig shiftConfig) {
        if (shifts != null && shiftConfig != null) {
            ZoneId zoneId = shiftConfig.getZoneId();
            for (RawAdditionalShiftEntity shift : shifts) {
                shiftConfig.process(shift, zoneId);
            }
            setValue(shifts);
        }
    }

}
