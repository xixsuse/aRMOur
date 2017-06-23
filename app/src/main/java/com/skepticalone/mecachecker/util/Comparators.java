package com.skepticalone.mecachecker.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.model.Expense;

import org.joda.time.DateTime;

import java.math.BigDecimal;


public final class Comparators {
    //    private static boolean compareObjects(@Nullable Object object1, @Nullable Object object2){
//        return object1 == null ? object2 == null : object1.equals(object2);
//    }
    public static boolean equalBigDecimals(@Nullable final BigDecimal bigDecimal1, @Nullable final BigDecimal bigDecimal2) {
        return bigDecimal1 == null ? bigDecimal2 == null : bigDecimal1.equals(bigDecimal2);
    }

    public static boolean equalStrings(@Nullable final String string1, @Nullable final String string2) {
        return string1 == null ? string2 == null : string1.equals(string2);
    }

    public static boolean equalDateTimes(@Nullable final DateTime date1, @Nullable final DateTime date2) {
        return date1 == null ? date2 == null : (date2 != null && date1.getMillis() == date2.getMillis());
    }

    public static boolean areItemsTheSame(@NonNull final Expense expense1, @NonNull final Expense expense2) {
        return expense1.getId() == expense2.getId();
    }

    public static boolean areContentsTheSame(@NonNull final Expense expense1, @NonNull final Expense expense2) {
        return
                equalStrings(expense1.getTitle(), expense2.getTitle()) &&
                        equalStrings(expense1.getComment(), expense2.getComment()) &&
                        equalBigDecimals(expense1.getPayment(), expense2.getPayment()) &&
                        equalDateTimes(expense1.getClaimed(), expense2.getClaimed()) &&
                        equalDateTimes(expense1.getPaid(), expense2.getPaid())
                ;
    }

}
