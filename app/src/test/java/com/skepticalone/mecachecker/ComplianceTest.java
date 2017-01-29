package com.skepticalone.mecachecker;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComplianceTest {

    private List<Shift> shifts;

    @Before
    public void setup() {
        shifts = new ArrayList<>();
        //WEEK
        shifts.add(getTestShift(1, 8, 0, 16, 0));
        shifts.add(getTestShift(2, 8, 0, 16, 0));
        shifts.add(getTestShift(3, 8, 0, 16, 0));
        shifts.add(getTestShift(4, 8, 0, 16, 0));
        shifts.add(getTestShift(5, 8, 0, 16, 0));
        shifts.add(getTestShift(6, 8, 0, 22, 30));
        shifts.add(getTestShift(7, 8, 0, 22, 30));
        //WEEK
        shifts.add(getTestShift(8, 8, 0, 16, 0));
        shifts.add(getTestShift(9, 8, 0, 16, 0));
        shifts.add(getTestShift(10, 8, 0, 16, 0));
        shifts.add(getTestShift(11, 8, 0, 16, 0));
        shifts.add(getTestShift(12, 8, 0, 16, 0));
        //free weekend
        //free weekend
        //WEEK
        shifts.add(getTestShift(15, 8, 0, 16, 0));
        shifts.add(getTestShift(16, 8, 0, 16, 0));
        shifts.add(getTestShift(17, 8, 0, 16, 0));
        shifts.add(getTestShift(18, 8, 0, 16, 0));
        shifts.add(getTestShift(19, 8, 0, 16, 0));
        shifts.add(getTestShift(20, 8, 0, 22, 30));
        shifts.add(getTestShift(21, 8, 0, 22, 30));
        //WEEK
        shifts.add(getTestShift(22, 8, 0, 16, 0));
        shifts.add(getTestShift(23, 8, 0, 16, 0));
        shifts.add(getTestShift(24, 8, 0, 16, 0));
        shifts.add(getTestShift(25, 8, 0, 16, 0));
        shifts.add(getTestShift(26, 8, 0, 16, 0));
        //free weekend
        //free weekend
    }

    @Test
    public void checkBaselineCompliant() {
        assertTrue(ComplianceChecks.checkMinimumRestHoursBetweenShifts(shifts));
        assertTrue(ComplianceChecks.checkMaximumHoursPerDay(shifts));
        assertTrue(ComplianceChecks.checkMaximumHoursPerWeek(shifts));
        assertTrue(ComplianceChecks.checkMaximumHoursPerFortnight(shifts));
    }

    @Test
    public void checkMinimumRestHoursBetweenShifts() {
        shifts.get(11).advance(Calendar.HOUR_OF_DAY, -8);
        assertTrue(ComplianceChecks.checkMinimumRestHoursBetweenShifts(shifts));
        shifts.get(11).advance(Calendar.MINUTE, -1);
        assertFalse(ComplianceChecks.checkMinimumRestHoursBetweenShifts(shifts));
    }

    @Test
    public void checkMaximumHoursPerDay() {
        shifts.get(11).end.add(Calendar.HOUR_OF_DAY, 8);
        assertTrue(ComplianceChecks.checkMaximumHoursPerDay(shifts));
        shifts.get(11).end.add(Calendar.MINUTE, 1);
        assertFalse(ComplianceChecks.checkMaximumHoursPerDay(shifts));
    }

    @Test
    public void checkMaximumHoursPerWeek() {
        shifts.get(7).end.add(Calendar.HOUR_OF_DAY, 3);
        assertTrue(ComplianceChecks.checkMaximumHoursPerWeek(shifts));
        shifts.get(7).end.add(Calendar.MINUTE, 1);
        assertFalse(ComplianceChecks.checkMaximumHoursPerWeek(shifts));
    }

    @Test
    public void checkMaximumHoursPerFortnight() {
        shifts.get(0).end.add(Calendar.HOUR_OF_DAY, 3);
        shifts.get(1).end.add(Calendar.HOUR_OF_DAY, 4);
        shifts.get(2).end.add(Calendar.HOUR_OF_DAY, 3);
        shifts.get(3).end.add(Calendar.HOUR_OF_DAY, 4);
        shifts.get(4).end.add(Calendar.HOUR_OF_DAY, 3);
        shifts.get(7).end.add(Calendar.HOUR_OF_DAY, 4);
        shifts.get(8).end.add(Calendar.HOUR_OF_DAY, 3);
        shifts.get(9).end.add(Calendar.HOUR_OF_DAY, 4);
        shifts.get(10).end.add(Calendar.HOUR_OF_DAY, 3);
        shifts.get(11).end.add(Calendar.HOUR_OF_DAY, 4);
        assertTrue(ComplianceChecks.checkMaximumHoursPerFortnight(shifts));
        shifts.get(11).end.add(Calendar.MINUTE, 1);
        assertFalse(ComplianceChecks.checkMaximumHoursPerFortnight(shifts));
    }

    private Shift getTestShift(int dayOfMonth, int startHourOfDay, int startMinute, int endHourOfDay, int endMinute) {
        Calendar start = new GregorianCalendar(2017, 5, dayOfMonth, startHourOfDay, startMinute);
        Calendar end = new GregorianCalendar(2017, 5, dayOfMonth, endHourOfDay, endMinute);
        while (!end.after(start)) {
            end.add(Calendar.DATE, 1);
        }
        return new Shift(start.getTimeInMillis() / 1000, end.getTimeInMillis() / 1000);
    }
}
