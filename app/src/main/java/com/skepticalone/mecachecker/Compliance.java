package com.skepticalone.mecachecker;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

final class Compliance {

    private static final int MINIMUM_REST_HOURS = 8;
    private static final int MAXIMUM_HOURS_PER_DAY = 16;
    private static final int MAXIMUM_HOURS_PER_WEEK = 72;
    private static final int MAXIMUM_HOURS_PER_FORTNIGHT = 144;

    private static final int MILLIS_PER_HOUR = 1000 * 60 * 60;
    private static final int MINIMUM_REST_IN_MILLIS = MINIMUM_REST_HOURS * MILLIS_PER_HOUR;

    static boolean checkMinimumRestHoursBetweenShifts(Collection<Shift> shifts) {
        Shift lastShift = null;
        for (Shift shift : shifts) {
            if (lastShift != null && shift.timeSince(lastShift) < MINIMUM_REST_IN_MILLIS) {
                return false;
            }
            lastShift = shift;
        }
        return true;
    }
//
//    static boolean checkMaximumHoursPerDay(Iterable<Shift> shifts) {
//        for (Shift shift: shifts){
//            long duration = shift.getDuration();
//            if (duration > MAXIMUM_TIME_PER_DAY_IN_MILLIS) {
//                return false;
//            }
//        }
//        return true;
//    }

    static boolean checkMaximumHoursPerDay(List<Shift> shifts) {
        return checkMaximumHoursInPeriod(1, MAXIMUM_HOURS_PER_DAY, shifts);
    }

    static boolean checkMaximumHoursPerWeek(List<Shift> shifts) {
        return checkMaximumHoursInPeriod(7, MAXIMUM_HOURS_PER_WEEK, shifts);
    }

    static boolean checkMaximumHoursPerFortnight(List<Shift> shifts) {
        return checkMaximumHoursInPeriod(14, MAXIMUM_HOURS_PER_FORTNIGHT, shifts);
    }

    private static boolean checkMaximumHoursInPeriod(int periodInDays, int maxHoursInPeriod, List<Shift> shifts) {
        long maxMillisInPeriod = maxHoursInPeriod * MILLIS_PER_HOUR;
        Calendar periodFromNow = new GregorianCalendar();
        for (int i = 0; i < shifts.size(); i++) {
            periodFromNow.setTime(shifts.get(i).start.getTime());
            periodFromNow.add(Calendar.DATE, periodInDays);
            long totalDurationInMillis = 0;
            for (int j = i; j < shifts.size(); j++) {
                Shift shift = shifts.get(j);
                if (shift.start.after(periodFromNow)) break;
                else if (shift.end.after(periodFromNow)) {
                    totalDurationInMillis += periodFromNow.getTimeInMillis() - shift.start.getTimeInMillis();
                } else {
                    totalDurationInMillis += shift.getDuration();
                }
                if (totalDurationInMillis > maxMillisInPeriod) {
                    return false;
                }
            }
        }
        return true;
    }
}
