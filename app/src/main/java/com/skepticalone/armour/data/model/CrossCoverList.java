package com.skepticalone.armour.data.model;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.List;

public final class CrossCoverList extends ItemList<CrossCoverEntity, CrossCover> {

    public CrossCoverList(@NonNull Context context, @NonNull LiveData<List<CrossCoverEntity>> liveRawCrossCoverShifts) {
        super(context, liveRawCrossCoverShifts);
    }

    @Override
    void onUpdated(@Nullable List<CrossCoverEntity> rawCrossCoverShifts, @Nullable ZoneId timeZone) {
        if (rawCrossCoverShifts != null && timeZone != null) {
            List<CrossCover> crossCoverShifts = new ArrayList<>(rawCrossCoverShifts.size());
            for (CrossCoverEntity rawCrossCover : rawCrossCoverShifts) {
                crossCoverShifts.add(new CrossCover(rawCrossCover, timeZone));
            }
            setValue(crossCoverShifts);
        }
    }

}
