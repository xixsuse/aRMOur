package com.skepticalone.mecachecker;

import android.support.annotation.Nullable;

import java.util.Calendar;

public class PeriodWithComplianceData extends HeavySpan {

    private NonCompliantTimeSpan compliantWithMinimumRestHoursBetweenShifts = null,
            compliantWithMaximumHoursPerDay = null,
            compliantWithMaximumHoursPerWeek = null,
            compliantWithMaximumHoursPerFortnight = null;
    private boolean compliantWithMaximumConsecutiveWeekends = true;

    PeriodWithComplianceData(long startSeconds, long endSeconds) {
        super(startSeconds, endSeconds);
    }

    PeriodWithComplianceData(Calendar start, Calendar end) {
        super(start, end);
    }

    public boolean isCompliantWithMinimumRestHoursBetweenShifts() {
        return compliantWithMinimumRestHoursBetweenShifts == null;
    }

    void markNonCompliantWithMinimumRestHoursBetweenShifts(NonCompliantTimeSpan nonCompliantPeriod) {
        if (compliantWithMinimumRestHoursBetweenShifts == null) {
            compliantWithMinimumRestHoursBetweenShifts = nonCompliantPeriod;
        }
    }

    @Nullable
    public NonCompliantTimeSpan nonCompliantPeriodWithMinimumRestHoursBetweenShifts() {
        return compliantWithMinimumRestHoursBetweenShifts;
    }

    public boolean isCompliantWithMaximumHoursPerDay() {
        return compliantWithMaximumHoursPerDay == null;
    }

    void markNonCompliantWithMaximumHoursPerDay(NonCompliantTimeSpan nonCompliantPeriod) {
        if (compliantWithMaximumHoursPerDay == null) {
            compliantWithMaximumHoursPerDay = nonCompliantPeriod;
        }
    }

    @Nullable
    public NonCompliantTimeSpan nonCompliantPeriodWithMaximumHoursPerDay() {
        return compliantWithMaximumHoursPerDay;
    }

    public boolean isCompliantWithMaximumHoursPerWeek() {
        return compliantWithMaximumHoursPerWeek == null;
    }

    void markNonCompliantWithMaximumHoursPerWeek(NonCompliantTimeSpan nonCompliantPeriod) {
        if (compliantWithMaximumHoursPerWeek == null) {
            compliantWithMaximumHoursPerWeek = nonCompliantPeriod;
        }
    }

    @Nullable
    public NonCompliantTimeSpan nonCompliantPeriodWithMaximumHoursPerWeek() {
        return compliantWithMaximumHoursPerWeek;
    }

    public boolean isCompliantWithMaximumHoursPerFortnight() {
        return compliantWithMaximumHoursPerFortnight == null;
    }

    void markNonCompliantWithMaximumHoursPerFortnight(NonCompliantTimeSpan nonCompliantPeriod) {
        if (compliantWithMaximumHoursPerFortnight == null) {
            compliantWithMaximumHoursPerFortnight = nonCompliantPeriod;
        }
    }

    @Nullable
    public NonCompliantTimeSpan nonCompliantPeriodWithMaximumHoursPerFortnight() {
        return compliantWithMaximumHoursPerFortnight;
    }

    public boolean isCompliantWithMaximumConsecutiveWeekends() {
        return compliantWithMaximumConsecutiveWeekends;
    }

    void markNonCompliantWithMaximumConsecutiveWeekends() {
        compliantWithMaximumConsecutiveWeekends = false;
    }
//    public boolean isCompliant(){
//        return
//                compliantWithMinimumRestHoursBetweenShifts &&
//                compliantWithMaximumHoursPerDay &&
//                compliantWithMaximumHoursPerWeek &&
//                compliantWithMaximumHoursPerFortnight &&
//                compliantWithMaximumConsecutiveWeekends
//        ;
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        if (!isCompliantWithMinimumRestHoursBetweenShifts()) {
            sb.append("\nNot compliantWithMinimumRestHoursBetweenShifts");
        }
        if (!isCompliantWithMaximumHoursPerDay()) {
            sb.append("\nNot compliantWithMaximumHoursPerDay");
        }
        if (!isCompliantWithMaximumHoursPerWeek()) {
            sb.append("\nNot compliantWithMaximumHoursPerWeek");
        }
        if (!isCompliantWithMaximumHoursPerFortnight()) {
            sb.append("\nNot compliantWithMaximumHoursPerFortnight");
        }
        if (!isCompliantWithMaximumConsecutiveWeekends()) {
            sb.append("\nNot compliantWithMaximumConsecutiveWeekends");
        }
        return sb.toString();
    }

}
