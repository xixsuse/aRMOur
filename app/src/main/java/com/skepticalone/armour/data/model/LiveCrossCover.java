package com.skepticalone.armour.data.model;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.List;

public final class LiveCrossCover extends LiveItems<RawCrossCoverEntity, CrossCover> {

    public LiveCrossCover(@NonNull Context context, @NonNull LiveData<List<RawCrossCoverEntity>> liveRawCrossCoverShifts) {
        super(context, liveRawCrossCoverShifts);
    }

    @Override
    void onUpdated(@Nullable List<RawCrossCoverEntity> rawCrossCoverShifts, @Nullable ZoneId timeZone) {
        if (rawCrossCoverShifts != null && timeZone != null) {
            List<CrossCover> crossCoverShifts = new ArrayList<>(rawCrossCoverShifts.size());
            for (RawCrossCoverEntity rawCrossCover : rawCrossCoverShifts) {
                crossCoverShifts.add(new CrossCover(rawCrossCover, timeZone));
            }
            setValue(crossCoverShifts);
        }
    }

}
