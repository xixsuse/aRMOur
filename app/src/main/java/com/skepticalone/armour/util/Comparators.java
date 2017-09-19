package com.skepticalone.armour.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.entity.PaymentData;
import com.skepticalone.armour.data.entity.ShiftData;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

public final class Comparators {

    public static boolean equalStrings(@Nullable final String string1, @Nullable final String string2) {
        return string1 == null ? string2 == null : string1.equals(string2);
    }

    public static boolean equalInstants(@Nullable final Instant instant1, @Nullable final Instant instant2) {
        return instant1 == null ? instant2 == null : (instant2 != null && instant1.getEpochSecond() == instant2.getEpochSecond());
    }

    public static boolean equalLocalDates(@NonNull final LocalDate date1, @NonNull final LocalDate date2) {
        return date1.isEqual(date2);
    }

    public static boolean equalBigDecimals(@NonNull final BigDecimal bigDecimal1, @NonNull final BigDecimal bigDecimal2) {
        return bigDecimal1.equals(bigDecimal2);
    }

    public static boolean equalPaymentData(@NonNull final PaymentData paymentData1, @NonNull final PaymentData paymentData2) {
        return equalBigDecimals(paymentData1.getPayment(), paymentData2.getPayment()) &&
                equalInstants(paymentData1.getClaimed(), paymentData2.getClaimed()) &&
                equalInstants(paymentData1.getPaid(), paymentData2.getPaid());
    }

    public static boolean equalShiftData(@NonNull final ShiftData shiftData1, @NonNull final ShiftData shiftData2) {
        return shiftData1.getStart().getEpochSecond() == shiftData2.getStart().getEpochSecond() &&
                shiftData1.getEnd().getEpochSecond() == shiftData2.getEnd().getEpochSecond();
    }

}
