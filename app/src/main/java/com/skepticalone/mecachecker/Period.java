package com.skepticalone.mecachecker;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;

public class Period {
    public final long start;
    public final long end;

    @Nullable
    static Period getWeekend(long start, long end, Calendar calendarToRecycle) {
        calendarToRecycle.setTimeInMillis(start);
        int day = calendarToRecycle.get(Calendar.DAY_OF_WEEK);
        if (!(day == Calendar.SATURDAY || day == Calendar.SUNDAY)) {
            calendarToRecycle.setTimeInMillis(end);
            day = calendarToRecycle.get(Calendar.DAY_OF_WEEK);
        }
        if (!((day == Calendar.SATURDAY && (calendarToRecycle.get(Calendar.HOUR_OF_DAY) > 0 || calendarToRecycle.get(Calendar.MINUTE) > 0)) || day == Calendar.SUNDAY)) {
            return null;
        }
        calendarToRecycle.set(Calendar.HOUR_OF_DAY, 0);
        calendarToRecycle.set(Calendar.MINUTE, 0);
        calendarToRecycle.set(Calendar.MILLISECOND, 0);
        if (day == Calendar.SUNDAY) {
            calendarToRecycle.add(Calendar.DAY_OF_MONTH, -1);
        }
        long startOfWeekend = calendarToRecycle.getTimeInMillis();
        calendarToRecycle.add(Calendar.DAY_OF_MONTH, 2);
        return new Period(startOfWeekend, calendarToRecycle.getTimeInMillis());
    }

    @NonNull
    public Period advanced(Calendar calendarToRecycle, int field, int amount) {
        calendarToRecycle.setTimeInMillis(start);
        calendarToRecycle.add(field, amount);
        long startOfNewPeriod = calendarToRecycle.getTimeInMillis();
        calendarToRecycle.setTimeInMillis(end);
        calendarToRecycle.add(field, amount);
        return new Period(startOfNewPeriod, calendarToRecycle.getTimeInMillis());
    }

    private Period(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Period) {
            Period other = (Period) obj;
            return start == other.start && end == other.end;
        }
        return false;
    }
}