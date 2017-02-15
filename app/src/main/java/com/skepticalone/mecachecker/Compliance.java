package com.skepticalone.mecachecker;

import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public final class Compliance {

//    public static final int MINIMUM_DURATION_REST = MINIMUM_REST_HOURS * MILLIS_PER_HOUR;
//    public static final int MINIMUM_DURATION_REST = MINIMUM_REST_HOURS * MILLIS_PER_HOUR;
//    public static final int MINIMUM_DURATION_REST = MINIMUM_REST_HOURS * MILLIS_PER_HOUR;


    static boolean checkMinimumRestHoursBetweenShifts(Iterable<? extends PeriodWithComplianceData> shifts) {
        boolean compliant = true;
        Period lastShift = null;
        for (PeriodWithComplianceData shift : shifts) {
            if (lastShift != null) {
                long millis = shift.timeSince(lastShift);
                if (millis < AppConstants.MINIMUM_DURATION_REST) {
                    ConcreteTimeSpan timeSpan = new ConcreteTimeSpan(lastShift.getEnd(), shift.getStart());
                    timeSpan.setHours(millis);
                    shift.markNonCompliantWithMinimumRestHoursBetweenShifts(timeSpan);
                    compliant = false;
                }
            }
            lastShift = shift;
        }
        return compliant;
    }

    static boolean checkMaximumHoursPerDay(List<? extends PeriodWithComplianceData> shifts) {
        return checkMaximumHoursInPeriod(shifts, PeriodToCheck.day);
    }

    static boolean checkMaximumHoursPerWeek(List<? extends PeriodWithComplianceData> shifts) {
        return checkMaximumHoursInPeriod(shifts, PeriodToCheck.week);
    }

    static boolean checkMaximumHoursPerFortnight(List<? extends PeriodWithComplianceData> shifts) {
        return checkMaximumHoursInPeriod(shifts, PeriodToCheck.fortnight);
    }

    private static boolean checkMaximumHoursInPeriod(List<? extends PeriodWithComplianceData> shifts, PeriodToCheck periodToCheck) {
        boolean compliant = true;
        int periodInDays;
        long maxMillisInPeriod;
        switch (periodToCheck) {
            case day:
                periodInDays = 1;
                maxMillisInPeriod = AppConstants.MAXIMUM_DURATION_OVER_DAY;
                break;
            case week:
                periodInDays = 7;
                maxMillisInPeriod = AppConstants.MAXIMUM_DURATION_OVER_WEEK;
                break;
            case fortnight:
                periodInDays = 14;
                maxMillisInPeriod = AppConstants.MAXIMUM_DURATION_OVER_FORTNIGHT;
                break;
            default:
                throw new IllegalArgumentException();
        }
        Calendar periodFromNow = new GregorianCalendar();
        for (int i = 0; i < shifts.size(); i++) {
            Date startOfPeriod = shifts.get(i).start.getTime();
            periodFromNow.setTime(startOfPeriod);
            periodFromNow.add(Calendar.DATE, periodInDays);
            ConcreteTimeSpan timeSpan = new ConcreteTimeSpan(startOfPeriod, periodFromNow.getTime());
            long totalDurationInMillis = 0;
            for (int j = i; j < shifts.size(); j++) {
                PeriodWithComplianceData shift = shifts.get(j);
                if (shift.start.after(periodFromNow)) break;
                else if (shift.end.after(periodFromNow)) {
                    totalDurationInMillis += periodFromNow.getTimeInMillis() - shift.start.getTimeInMillis();
                } else {
                    totalDurationInMillis += shift.getDuration();
                }
                if (totalDurationInMillis > maxMillisInPeriod) {
                    timeSpan.setHours(totalDurationInMillis);
                    switch (periodToCheck) {
                        case day:
                            shift.markNonCompliantWithMaximumHoursPerDay(timeSpan);
                            break;
                        case week:
                            shift.markNonCompliantWithMaximumHoursPerWeek(timeSpan);
                            break;
                        case fortnight:
                            shift.markNonCompliantWithMaximumHoursPerFortnight(timeSpan);
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    compliant = false;
                }
            }
        }
        return compliant;
    }

    public static long getDurationOverDay(Cursor cursor, int startColumnIndex, int endColumnIndex, Calendar calendarToRecycle, int positionToCheck, boolean retrospective) {
        return getDurationOverPeriod(cursor, startColumnIndex, endColumnIndex, calendarToRecycle, positionToCheck, 1, retrospective);
    }

    public static long getDurationOverWeek(Cursor cursor, int startColumnIndex, int endColumnIndex, Calendar calendarToRecycle, int positionToCheck, boolean retrospective) {
        return getDurationOverPeriod(cursor, startColumnIndex, endColumnIndex, calendarToRecycle, positionToCheck, 7, retrospective);
    }

    public static long getDurationOverFortnight(Cursor cursor, int startColumnIndex, int endColumnIndex, Calendar calendarToRecycle, int positionToCheck, boolean retrospective) {
        return getDurationOverPeriod(cursor, startColumnIndex, endColumnIndex, calendarToRecycle, positionToCheck, 14, retrospective);
    }

    private static long getDurationOverPeriod(Cursor cursor, int startColumnIndex, int endColumnIndex, Calendar calendarToRecycle, int positionToCheck, int periodInDays, boolean retrospective) {
        long totalDuration = 0, periodStart, periodEnd;
        cursor.moveToPosition(positionToCheck);
        if (retrospective) {
            periodEnd = cursor.getLong(endColumnIndex);
            calendarToRecycle.setTimeInMillis(periodEnd);
            calendarToRecycle.add(Calendar.DATE, -periodInDays);
            periodStart = calendarToRecycle.getTimeInMillis();
            do {
                long end = cursor.getLong(endColumnIndex);
                if (periodStart >= end) break;
                long start = cursor.getLong(startColumnIndex);
                totalDuration += end - Math.max(periodStart, start);
            } while (cursor.moveToPrevious());
        } else {
            periodStart = cursor.getLong(startColumnIndex);
            calendarToRecycle.setTimeInMillis(periodStart);
            calendarToRecycle.add(Calendar.DATE, periodInDays);
            periodEnd = calendarToRecycle.getTimeInMillis();
            do {
                long start = cursor.getLong(startColumnIndex);
                if (periodEnd <= start) break;
                long end = cursor.getLong(endColumnIndex);
                totalDuration += Math.min(periodEnd, end) - start;
            } while (cursor.moveToNext());
        }
        return totalDuration;
    }

    public static long getDurationOfRest(Cursor cursor, int startColumnIndex, int endColumnIndex, int positionToCheck) {
        cursor.moveToPosition(positionToCheck);
        long currentStart = cursor.getLong(startColumnIndex);
        if (cursor.moveToPrevious()) {
            long previousEnd = cursor.getLong(endColumnIndex);
            return currentStart - previousEnd;
        } else return -1L;
    }


    static boolean checkMaximumConsecutiveWeekends(Iterable<? extends PeriodWithComplianceData> shifts) {
        boolean compliant = true;
        Period forbiddenWeekend = null;
        for (PeriodWithComplianceData shift : shifts) {
            if (forbiddenWeekend == null || !shift.start.before(forbiddenWeekend.end)) {
                forbiddenWeekend = shift.getForbiddenWeekend();
            } else if (shift.overlapsWith(forbiddenWeekend)) {
                shift.markNonCompliantWithMaximumConsecutiveWeekends();
                compliant = false;
            }
        }
        return compliant;
    }

    private enum PeriodToCheck {
        day,
        week,
        fortnight
    }

    private static class ConcreteTimeSpan implements NonCompliantTimeSpan {

        private final Date start, end;
        private float hours = 0;

        private ConcreteTimeSpan(Date start, Date end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Date getStart() {
            return start;
        }

        @Override
        public Date getEnd() {
            return end;
        }

        @Override
        public float getHours() {
            return hours;
        }

        void setHours(long millis) {
            hours = (float) millis / AppConstants.MILLIS_PER_HOUR;
        }
    }

}
