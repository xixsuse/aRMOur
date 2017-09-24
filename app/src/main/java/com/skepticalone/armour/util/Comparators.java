package com.skepticalone.armour.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RawPaymentData;
import com.skepticalone.armour.data.model.RawShift;

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

    public static boolean equalPaymentData(@NonNull final RawPaymentData paymentData1, @NonNull final RawPaymentData paymentData2) {
        return equalBigDecimals(paymentData1.getPayment(), paymentData2.getPayment()) &&
                equalInstants(paymentData1.getClaimed(), paymentData2.getClaimed()) &&
                equalInstants(paymentData1.getPaid(), paymentData2.getPaid());
    }

    public static boolean equalShiftData(@NonNull final RawShift.RawShiftData rawShiftData1, @NonNull final RawShift.RawShiftData rawShiftData2) {
        return rawShiftData1.getStart().getEpochSecond() == rawShiftData2.getStart().getEpochSecond() &&
                rawShiftData1.getEnd().getEpochSecond() == rawShiftData2.getEnd().getEpochSecond();
    }

}
