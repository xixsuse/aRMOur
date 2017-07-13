package com.skepticalone.mecachecker.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.PaymentData;

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
        return paymentData1.equals(paymentData2);
    }

}
