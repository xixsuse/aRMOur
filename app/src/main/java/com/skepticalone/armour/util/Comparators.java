package com.skepticalone.armour.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.util.PaymentData;
import com.skepticalone.armour.data.util.ShiftData;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

public final class Comparators {

    public static boolean equalStrings(@Nullable final String string1, @Nullable final String string2) {
        return string1 == null ? string2 == null : string1.equals(string2);
    }

    public static boolean equalDateTimes(@Nullable final DateTime date1, @Nullable final DateTime date2) {
        return date1 == null ? date2 == null : (date2 != null && date1.getMillis() == date2.getMillis());
    }

    public static boolean equalLocalDates(@NonNull final LocalDate date1, @NonNull final LocalDate date2) {
        return date1.isEqual(date2);
    }

    public static boolean equalBigDecimals(@NonNull final BigDecimal bigDecimal1, @NonNull final BigDecimal bigDecimal2) {
        return bigDecimal1.equals(bigDecimal2);
    }

    public static boolean equalPaymentData(@NonNull final PaymentData paymentData1, @NonNull final PaymentData paymentData2) {
        return equalBigDecimals(paymentData1.getPayment(), paymentData2.getPayment()) &&
                equalDateTimes(paymentData1.getClaimed(), paymentData2.getClaimed()) &&
                equalDateTimes(paymentData1.getPaid(), paymentData2.getPaid());
    }

    public static boolean equalShiftData(@NonNull final ShiftData shiftData1, @NonNull final ShiftData shiftData2) {
        return shiftData1.getStart().getMillis() == shiftData2.getStart().getMillis() &&
                shiftData1.getEnd().getMillis() == shiftData2.getEnd().getMillis();
    }

}
