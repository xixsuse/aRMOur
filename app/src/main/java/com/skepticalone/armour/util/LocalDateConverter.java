package com.skepticalone.armour.util;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;

public final class LocalDateConverter {

    @TypeConverter
    public static long dateToEpochDay(@NonNull LocalDate date) {
        return date.toEpochDay();
    }

    @TypeConverter
    @NonNull
    public static LocalDate epochDayToDate(long epochDay) {
        return LocalDate.ofEpochDay(epochDay);
    }

}
