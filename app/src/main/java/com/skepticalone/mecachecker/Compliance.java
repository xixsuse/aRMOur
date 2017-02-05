package com.skepticalone.mecachecker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

final class Compliance {

    private static final int MINIMUM_REST_HOURS = 8;
    private static final int MAXIMUM_HOURS_PER_DAY = 16;
    private static final int MAXIMUM_HOURS_PER_WEEK = 72;
    private static final int MAXIMUM_HOURS_PER_FORTNIGHT = 144;

    private static final int MILLIS_PER_HOUR = 1000 * 60 * 60;
    private static final int MINIMUM_REST_IN_MILLIS = MINIMUM_REST_HOURS * MILLIS_PER_HOUR;

    static boolean checkMinimumRestHoursBetweenShifts(Iterable<Period> shifts) {
        Period lastShift = null;
        for (Period shift : shifts) {
            if (lastShift != null && shift.timeSince(lastShift) < MINIMUM_REST_IN_MILLIS) {
                return false;
            }
            lastShift = shift;
        }
        return true;
    }

    static boolean checkMaximumHoursPerDay(List<Period> shifts) {
        return checkMaximumHoursInPeriod(1, MAXIMUM_HOURS_PER_DAY, shifts);
    }

    static boolean checkMaximumHoursPerWeek(List<Period> shifts) {
        return checkMaximumHoursInPeriod(7, MAXIMUM_HOURS_PER_WEEK, shifts);
    }

    static boolean checkMaximumHoursPerFortnight(List<Period> shifts) {
        return checkMaximumHoursInPeriod(14, MAXIMUM_HOURS_PER_FORTNIGHT, shifts);
    }

    private static boolean checkMaximumHoursInPeriod(int periodInDays, int maxHoursInPeriod, List<Period> shifts) {
        long maxMillisInPeriod = maxHoursInPeriod * MILLIS_PER_HOUR;
        Calendar periodFromNow = new GregorianCalendar();
        for (int i = 0; i < shifts.size(); i++) {
            periodFromNow.setTime(shifts.get(i).start.getTime());
            periodFromNow.add(Calendar.DATE, periodInDays);
            long totalDurationInMillis = 0;
            for (int j = i; j < shifts.size(); j++) {
                Period shift = shifts.get(j);
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

    static boolean checkMaximumConsecutiveWeekends(Iterable<Period> shifts) {
        Period forbiddenWeekend = null;
        for (Period shift : shifts) {
            if (forbiddenWeekend == null || !shift.start.before(forbiddenWeekend.end)) {
                forbiddenWeekend = shift.getForbiddenWeekend();
            } else if (shift.overlapsWith(forbiddenWeekend)) {
                return false;
            }
        }
        return true;
    }
}
