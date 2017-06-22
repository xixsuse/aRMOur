package com.skepticalone.mecachecker.db.converter;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

public class DateTimeConverter {

    @TypeConverter
    @Nullable
    public static Long dateTimeToMillis(@Nullable DateTime dateTime) {
        return dateTime == null ? null : dateTime.getMillis();
    }

    @TypeConverter
    @Nullable
    public static DateTime millisToDateTime(@Nullable Long millis) {
        return millis == null ? null : new DateTime(millis);
    }

}
