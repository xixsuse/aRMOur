package com.skepticalone.mecachecker.data.util;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyConverter {

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

}
