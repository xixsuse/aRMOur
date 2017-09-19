package com.skepticalone.armour.data.util;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import org.threeten.bp.Instant;

public final class InstantConverter {

    @TypeConverter
    @Nullable
    public static Long instantToEpochSecond(@Nullable Instant instant) {
        return instant == null ? null : instant.getEpochSecond();
    }

    @TypeConverter
    @Nullable
    public static Instant epochSecondToInstant(@Nullable Long epochSecond) {
        return epochSecond == null ? null : Instant.ofEpochSecond(epochSecond);
    }

}
