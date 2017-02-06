package com.skepticalone.mecachecker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public final class Compliance {

    private static final int MINIMUM_REST_HOURS = 8;
    private static final int MAXIMUM_HOURS_PER_DAY = 16;
    private static final int MAXIMUM_HOURS_PER_WEEK = 72;
    private static final int MAXIMUM_HOURS_PER_FORTNIGHT = 144;
    private static final int MILLIS_PER_HOUR = 1000 * 60 * 60;
    private static final int MINIMUM_REST_IN_MILLIS = MINIMUM_REST_HOURS * MILLIS_PER_HOUR;

    public static boolean checkMinimumRestHoursBetweenShifts(Iterable<? extends PeriodWithComplianceData> shifts) {
        boolean compliant = true;
        Period lastShift = null;
        for (PeriodWithComplianceData shift : shifts) {
            if (lastShift != null && shift.timeSince(lastShift) < MINIMUM_REST_IN_MILLIS) {
                shift.markNonCompliantWithMinimumRestHoursBetweenShifts(lastShift.end.getTime());
                compliant = false;
            }
            lastShift = shift;
        }
        return compliant;
    }

    public static boolean checkMaximumHoursPerDay(List<? extends PeriodWithComplianceData> shifts) {
        return checkMaximumHoursInPeriod(shifts, PeriodToCheck.day);
    }

    public static boolean checkMaximumHoursPerWeek(List<? extends PeriodWithComplianceData> shifts) {
        return checkMaximumHoursInPeriod(shifts, PeriodToCheck.week);
    }

    public static boolean checkMaximumHoursPerFortnight(List<? extends PeriodWithComplianceData> shifts) {
        return checkMaximumHoursInPeriod(shifts, PeriodToCheck.fortnight);
    }

    private static boolean checkMaximumHoursInPeriod(List<? extends PeriodWithComplianceData> shifts, PeriodToCheck periodToCheck) {
        boolean compliant = true;
        int periodInDays;
        long maxMillisInPeriod;
        switch (periodToCheck) {
            case day:
                periodInDays = 1;
                maxMillisInPeriod = MAXIMUM_HOURS_PER_DAY * MILLIS_PER_HOUR;
                break;
            case week:
                periodInDays = 7;
                maxMillisInPeriod = MAXIMUM_HOURS_PER_WEEK * MILLIS_PER_HOUR;
                break;
            case fortnight:
                periodInDays = 14;
                maxMillisInPeriod = MAXIMUM_HOURS_PER_FORTNIGHT * MILLIS_PER_HOUR;
                break;
            default:
                throw new IllegalArgumentException();
        }
        Calendar periodFromNow = new GregorianCalendar();
        for (int i = 0; i < shifts.size(); i++) {
            Date startOfPeriod = shifts.get(i).start.getTime();
            periodFromNow.setTime(startOfPeriod);
            periodFromNow.add(Calendar.DATE, periodInDays);
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
                    switch (periodToCheck) {
                        case day:
                            shift.markNonCompliantWithMaximumHoursPerDay(startOfPeriod);
                            break;
                        case week:
                            shift.markNonCompliantWithMaximumHoursPerWeek(startOfPeriod);
                            break;
                        case fortnight:
                            shift.markNonCompliantWithMaximumHoursPerFortnight(startOfPeriod);
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

    public static boolean checkMaximumConsecutiveWeekends(Iterable<? extends PeriodWithComplianceData> shifts) {
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

//    @NonNull
//    private static List<PeriodWithComplianceData> checkMaximumHoursInPeriod(int periodInDays, int maxHoursInPeriod, List<? extends PeriodWithComplianceData> shifts, final int itemIndex) {
//        List<PeriodWithComplianceData> nonCompliantShifts = new ArrayList<>();
//        long maxMillisInPeriod = maxHoursInPeriod * MILLIS_PER_HOUR;
//        Calendar periodFromNow = new GregorianCalendar();
//        periodFromNow.setTime(shifts.get(itemIndex).start.getTime());
//        periodFromNow.add(Calendar.DATE, periodInDays);
//        long totalDurationInMillis = 0;
//        for (int i = itemIndex; i < shifts.size(); i++) {
//            PeriodWithComplianceData shift = shifts.get(i);
//            if (shift.start.after(periodFromNow)) break;
//            else if (shift.end.after(periodFromNow)) {
//                totalDurationInMillis += periodFromNow.getTimeInMillis() - shift.start.getTimeInMillis();
//            } else {
//                totalDurationInMillis += shift.getDuration();
//            }
//            if (totalDurationInMillis > maxMillisInPeriod) {
//                nonCompliantShifts.add(shift);
//            }
//        }
//        return nonCompliantShifts;
//    }

//    @NonNull
//    private static List<PeriodWithComplianceData> checkMaximumHoursInPeriod(int periodInDays, int maxHoursInPeriod, List<? extends PeriodWithComplianceData> shifts) {
//        List<PeriodWithComplianceData> nonCompliantShifts = new ArrayList<>();
//        long maxMillisInPeriod = maxHoursInPeriod * MILLIS_PER_HOUR;
//        Calendar periodFromNow = new GregorianCalendar();
//        for (int i = 0; i < shifts.size(); i++) {
//            periodFromNow.setTime(shifts.get(i).start.getTime());
//            periodFromNow.add(Calendar.DATE, periodInDays);
//            long totalDurationInMillis = 0;
//            for (int j = i; j < shifts.size(); j++) {
//                PeriodWithComplianceData shift = shifts.get(j);
//                if (shift.start.after(periodFromNow)) break;
//                else if (shift.end.after(periodFromNow)) {
//                    totalDurationInMillis += periodFromNow.getTimeInMillis() - shift.start.getTimeInMillis();
//                } else {
//                    totalDurationInMillis += shift.getDuration();
//                }
//                if (totalDurationInMillis > maxMillisInPeriod) {
//                    nonCompliantShifts.add(shift);
//                }
//            }
//        }
//        return nonCompliantShifts;
//    }

//    abstract class MarkedPeriod extends PeriodWithComplianceData {
//
//        abstract void markAsNonCompliant();
//    }

    private enum PeriodToCheck {
        day,
        week,
        fortnight
    }

}
