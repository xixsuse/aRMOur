package com.skepticalone.armour.util;

import android.support.annotation.Nullable;

import org.threeten.bp.ZonedDateTime;

public final class Comparators {

    public static boolean equalStrings(@Nullable final String string1, @Nullable final String string2) {
        return string1 == null ? string2 == null : string1.equals(string2);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean equalDateTimes(@Nullable final ZonedDateTime dateTime1, @Nullable final ZonedDateTime dateTime2) {
        return dateTime1 == null ? dateTime2 == null : (dateTime2 != null && dateTime1.isEqual(dateTime2));
    }
//
//    public static boolean equalPaymentData(@NonNull final Payable.PaymentData paymentData1, @NonNull final Payable.PaymentData paymentData2) {
//        return paymentData1.getPayment().equals(paymentData2.getPayment()) &&
//                equalDateTimes(paymentData1.getClaimed(), paymentData2.getClaimed()) &&
//                equalDateTimes(paymentData1.getPaid(), paymentData2.getPaid());
//    }
//
//    public static boolean equalShiftData(@NonNull final Shift.ShiftData shiftData1, @NonNull final Shift.ShiftData shiftData2) {
//        return shiftData1.getStart().isEqual(shiftData2.getStart()) &&
//                shiftData1.getEnd().isEqual(shiftData2.getEnd());
//    }

}
