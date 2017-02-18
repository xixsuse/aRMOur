//package com.skepticalone.mecachecker;
//
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//
//import java.util.Calendar;
//
//public final class ShiftWeekendInfo {
//
//    @NonNull
//    public final HeavySpan currentWeekend;
//    @NonNull
//    public final HeavySpan previousWeekend;
//
//    @Nullable
//    public static ShiftWeekendInfo fromShift(long start, long end, Calendar calendarToRecycle){
//        calendarToRecycle.setTimeInMillis(start);
//        int day = calendarToRecycle.get(Calendar.DAY_OF_WEEK);
//        if (!(day == Calendar.SATURDAY || day == Calendar.SUNDAY)) {
//            calendarToRecycle.setTimeInMillis(end);
//            day = calendarToRecycle.get(Calendar.DAY_OF_WEEK);
//        }
//        if (!((day == Calendar.SATURDAY && (calendarToRecycle.get(Calendar.HOUR_OF_DAY) > 0 || calendarToRecycle.get(Calendar.MINUTE) > 0)) || day == Calendar.SUNDAY)) {
//            return null;
//        }
//        calendarToRecycle.set(Calendar.HOUR_OF_DAY, 0);
//        calendarToRecycle.set(Calendar.MINUTE, 0);
//        calendarToRecycle.set(Calendar.MILLISECOND, 0);
//        if (day == Calendar.SUNDAY) {
//            calendarToRecycle.add(Calendar.DAY_OF_MONTH, -1);
//        }
//        HeavySpan currentWeekend = HeavySpan.getWeekend(calendarToRecycle);
//        calendarToRecycle.setTimeInMillis(currentWeekend.start);
//        calendarToRecycle.add(Calendar.DAY_OF_MONTH, -7);
//        HeavySpan previousWeekend = HeavySpan.getWeekend(calendarToRecycle);
//        return new ShiftWeekendInfo(currentWeekend, previousWeekend);
//    }
//
//    private ShiftWeekendInfo(@NonNull HeavySpan currentWeekend, @NonNull HeavySpan previousWeekend){
//        this.currentWeekend = currentWeekend;
//        this.previousWeekend = previousWeekend;
//    }
//
//
//
//}
