package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Converters {

    private static final int MONEY_SCALE = 2;

    @TypeConverter
    public static int moneyToCents(@NonNull BigDecimal money) {
        return money.setScale(MONEY_SCALE, RoundingMode.HALF_UP).intValue();
    }

    @TypeConverter
    @NonNull
    public static BigDecimal centsToMoney(int cents) {
        return BigDecimal.valueOf(cents, MONEY_SCALE);
    }

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
