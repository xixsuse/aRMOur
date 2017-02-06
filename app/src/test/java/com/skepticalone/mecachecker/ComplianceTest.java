package com.skepticalone.mecachecker;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unused")
public class ComplianceTest {

    private final List<PeriodWithComplianceData> shifts = new ArrayList<>();

    @Before
    public void setup() {
        shifts.clear();
        //WEEK
        shifts.add(new MockPeriod(1, 8, 0, 16, 0));
        shifts.add(new MockPeriod(2, 8, 0, 16, 0));
        shifts.add(new MockPeriod(3, 8, 0, 16, 0));
        shifts.add(new MockPeriod(4, 8, 0, 16, 0));
        shifts.add(new MockPeriod(5, 8, 0, 16, 0));
        //working weekend
        shifts.add(new MockPeriod(6, 8, 0, 22, 30));
        shifts.add(new MockPeriod(7, 8, 0, 22, 30));
        //WEEK
        shifts.add(new MockPeriod(8, 8, 0, 16, 0));
        shifts.add(new MockPeriod(9, 8, 0, 16, 0));
        shifts.add(new MockPeriod(10, 8, 0, 16, 0));
        shifts.add(new MockPeriod(11, 8, 0, 16, 0));
        shifts.add(new MockPeriod(12, 8, 0, 16, 0));
        //free weekend
        //free weekend
        //WEEK
        shifts.add(new MockPeriod(15, 8, 0, 16, 0));
        shifts.add(new MockPeriod(16, 8, 0, 16, 0));
        shifts.add(new MockPeriod(17, 8, 0, 16, 0));
        shifts.add(new MockPeriod(18, 8, 0, 16, 0));
        shifts.add(new MockPeriod(19, 8, 0, 16, 0));
        //working weekend
        shifts.add(new MockPeriod(20, 8, 0, 22, 30));
        shifts.add(new MockPeriod(21, 8, 0, 22, 30));
        //WEEK
        shifts.add(new MockPeriod(22, 8, 0, 16, 0));
        shifts.add(new MockPeriod(23, 8, 0, 16, 0));
        shifts.add(new MockPeriod(24, 8, 0, 16, 0));
        shifts.add(new MockPeriod(25, 8, 0, 16, 0));
        shifts.add(new MockPeriod(26, 8, 0, 16, 0));
        //free weekend
        //free weekend
        //WEEK
        shifts.add(new MockPeriod(29, 8, 0, 16, 0));
        shifts.add(new MockPeriod(30, 8, 0, 16, 0));
        shifts.add(new MockPeriod(31, 8, 0, 16, 0));
        shifts.add(new MockPeriod(32, 8, 0, 16, 0));
        // going onto 7 nights
        shifts.add(new MockPeriod(33, 22, 0, 8, 0));
        shifts.add(new MockPeriod(34, 22, 0, 8, 0));
        shifts.add(new MockPeriod(35, 22, 0, 8, 0));
        shifts.add(new MockPeriod(36, 22, 0, 8, 0));
        shifts.add(new MockPeriod(37, 22, 0, 8, 0));
        shifts.add(new MockPeriod(38, 22, 0, 8, 0));
        shifts.add(new MockPeriod(39, 22, 0, 8, 0));
        //free weekend
        //free weekend
        //WEEK
        shifts.add(new MockPeriod(43, 8, 0, 16, 0));
        shifts.add(new MockPeriod(44, 8, 0, 16, 0));
        shifts.add(new MockPeriod(45, 8, 0, 16, 0));
        shifts.add(new MockPeriod(46, 8, 0, 16, 0));
        shifts.add(new MockPeriod(47, 8, 0, 16, 0));
        //free weekend
        //free weekend
        //WEEK
        shifts.add(new MockPeriod(50, 8, 0, 16, 0));
        shifts.add(new MockPeriod(51, 8, 0, 16, 0));
    }

    @Test
    public void checkBaselineCompliant() {
        assertTrue(Compliance.checkMinimumRestHoursBetweenShifts(shifts));
        assertTrue(Compliance.checkMaximumHoursPerDay(shifts));
        assertTrue(Compliance.checkMaximumHoursPerWeek(shifts));
        assertTrue(Compliance.checkMaximumHoursPerFortnight(shifts));
        assertTrue(Compliance.checkMaximumConsecutiveWeekends(shifts));
    }

    @Test
    public void checkMinimumRestHoursBetweenShifts() {
        shifts.get(11).advance(Calendar.HOUR_OF_DAY, -8);
        assertTrue(Compliance.checkMinimumRestHoursBetweenShifts(shifts));
        shifts.get(11).advance(Calendar.MINUTE, -1);
        assertFalse(Compliance.checkMinimumRestHoursBetweenShifts(shifts));
        assertFalse(shifts.get(11).isCompliantWithMinimumRestHoursBetweenShifts());
    }

    @Test
    public void checkMaximumHoursPerDay() {
        shifts.get(11).end.add(Calendar.HOUR_OF_DAY, 8);
        assertTrue(Compliance.checkMaximumHoursPerDay(shifts));
        shifts.get(11).end.add(Calendar.MINUTE, 1);
        assertFalse(Compliance.checkMaximumHoursPerDay(shifts));
        assertFalse(shifts.get(11).isCompliantWithMaximumHoursPerDay());
    }

    @Test
    public void checkMaximumHoursPerWeek() {
        shifts.get(7).end.add(Calendar.HOUR_OF_DAY, 3);
        assertTrue(Compliance.checkMaximumHoursPerWeek(shifts));
        shifts.get(7).end.add(Calendar.MINUTE, 1);
        assertFalse(Compliance.checkMaximumHoursPerWeek(shifts));
        assertFalse(shifts.get(7).isCompliantWithMaximumHoursPerWeek());
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
        assertTrue(Compliance.checkMaximumHoursPerFortnight(shifts));
        shifts.get(11).end.add(Calendar.MINUTE, 1);
        assertFalse(Compliance.checkMaximumHoursPerFortnight(shifts));
        assertFalse(shifts.get(11).isCompliantWithMaximumHoursPerFortnight());
    }

    @Test
    public void checkMaximumConsecutiveWeekends() {
        shifts.add(12, new MockPeriod(15, 0, 0, 0, 1));
        assertTrue(Compliance.checkMaximumConsecutiveWeekends(shifts));
        shifts.get(12).advance(Calendar.MINUTE, -1);
        assertFalse(Compliance.checkMaximumConsecutiveWeekends(shifts));
        assertFalse(shifts.get(12).isCompliantWithMaximumConsecutiveWeekends());
    }

}
