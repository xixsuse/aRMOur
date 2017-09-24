package com.skepticalone.armour.data.util;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import org.threeten.bp.Duration;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyConverter {

    private static final long SECONDS_PER_HOUR = 3600L;
    private static final int MONEY_SCALE = 2;

    @TypeConverter
    public static long moneyToCents(@NonNull BigDecimal money) {
        return money.setScale(MONEY_SCALE, RoundingMode.HALF_UP).unscaledValue().longValue();
    }

    @TypeConverter
    @NonNull
    public static BigDecimal centsToMoney(long cents) {
        return BigDecimal.valueOf(cents, MONEY_SCALE);
    }

    @NonNull
    public static BigDecimal hourlyRateToTotal(@NonNull BigDecimal hourlyRate, @NonNull Duration duration) {
        return hourlyRate.multiply(BigDecimal.valueOf(duration.getSeconds()))
                .divide(BigDecimal.valueOf(SECONDS_PER_HOUR), MONEY_SCALE, BigDecimal.ROUND_HALF_UP);
    }

}
