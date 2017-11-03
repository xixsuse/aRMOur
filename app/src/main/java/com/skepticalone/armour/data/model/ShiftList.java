package com.skepticalone.armour.data.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.ZoneId;

import java.util.List;

abstract class ShiftList<Entity, FinalItem> extends ItemList<Entity, FinalItem> {

    @NonNull
    final LiveData<Shift.ShiftType.Configuration> liveShiftConfig;

    ShiftList(@NonNull Context context, @NonNull LiveData<List<Entity>> liveRawShifts) {
        super(context, liveRawShifts);
        liveShiftConfig = Shift.ShiftType.LiveShiftConfiguration.getInstance(context);
        addSource(liveShiftConfig, new Observer<Shift.ShiftType.Configuration>() {
            @Override
            public void onChanged(@Nullable Shift.ShiftType.Configuration shiftConfig) {
                onUpdated(liveRawItems.getValue(), liveTimeZone.getValue(), shiftConfig);
            }
        });
    }

    @Override
    final void onUpdated(@Nullable List<Entity> rawShifts, @Nullable ZoneId timeZone) {
        onUpdated(rawShifts, timeZone, liveShiftConfig.getValue());
    }

    abstract void onUpdated(@Nullable List<Entity> rawShifts, @Nullable ZoneId timeZone, @Nullable Shift.ShiftType.Configuration shiftConfig);

}
