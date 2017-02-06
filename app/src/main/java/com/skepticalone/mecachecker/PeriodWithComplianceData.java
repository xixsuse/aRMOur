package com.skepticalone.mecachecker;

import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

public class PeriodWithComplianceData extends Period {

    @Nullable
    private Date compliantWithMinimumRestHoursBetweenShifts = null,
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

    void markNonCompliantWithMinimumRestHoursBetweenShifts(Date startOfNonCompliantPeriod) {
        if (compliantWithMinimumRestHoursBetweenShifts == null) {
            compliantWithMinimumRestHoursBetweenShifts = startOfNonCompliantPeriod;
        }
    }

    public boolean isCompliantWithMaximumHoursPerDay() {
        return compliantWithMaximumHoursPerDay == null;
    }

    void markNonCompliantWithMaximumHoursPerDay(Date startOfNonCompliantPeriod) {
        if (compliantWithMaximumHoursPerDay == null) {
            compliantWithMaximumHoursPerDay = startOfNonCompliantPeriod;
        }
    }

    public boolean isCompliantWithMaximumHoursPerWeek() {
        return compliantWithMaximumHoursPerWeek == null;
    }

    void markNonCompliantWithMaximumHoursPerWeek(Date startOfNonCompliantPeriod) {
        if (compliantWithMaximumHoursPerWeek == null) {
            compliantWithMaximumHoursPerWeek = startOfNonCompliantPeriod;
        }
    }

    public boolean isCompliantWithMaximumHoursPerFortnight() {
        return compliantWithMaximumHoursPerFortnight == null;
    }

    void markNonCompliantWithMaximumHoursPerFortnight(Date startOfNonCompliantPeriod) {
        if (compliantWithMaximumHoursPerFortnight == null) {
            compliantWithMaximumHoursPerFortnight = startOfNonCompliantPeriod;
        }
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
