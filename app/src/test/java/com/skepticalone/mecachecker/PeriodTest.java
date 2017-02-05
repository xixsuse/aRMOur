package com.skepticalone.mecachecker;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PeriodTest {

    @Test
    public void overlapsWith() {
        Period period = new Period(10, 20);

        assertFalse(period.overlapsWith(new Period(5, 9)));
        assertFalse(period.overlapsWith(new Period(5, 10)));
        assertTrue(period.overlapsWith(new Period(5, 15)));
        assertTrue(period.overlapsWith(new Period(5, 25)));

        assertTrue(period.overlapsWith(new Period(10, 15)));
        assertTrue(period.overlapsWith(new Period(10, 20)));
        assertTrue(period.overlapsWith(new Period(10, 25)));

        assertTrue(period.overlapsWith(new Period(15, 19)));
        assertTrue(period.overlapsWith(new Period(15, 20)));
        assertTrue(period.overlapsWith(new Period(15, 25)));

        assertFalse(period.overlapsWith(new Period(20, 25)));

        assertFalse(period.overlapsWith(new Period(25, 30)));
    }

    @Test
    public void getForbiddenWeekend() {

        Period weekDayShift = new MockPeriod(1, 8, 0, 16, 0);
        Period weekendShift = new MockPeriod(6, 8, 0, 16, 0);
        Period startOnWeekdayShift = new MockPeriod(8, 0, 0, 16, 0);
        Period startOnWeekendShift = new MockPeriod(7, 23, 59, 16, 0);
        Period endOnWeekdayShift = new MockPeriod(5, 22, 0, 0, 0);
        Period endOnWeekendShift = new MockPeriod(5, 22, 0, 8, 0);

        assertFalse(weekDayShift.involvesWeekend());
        assertNull(weekDayShift.getForbiddenWeekend());
        assertFalse(startOnWeekdayShift.involvesWeekend());
        assertNull(startOnWeekdayShift.getForbiddenWeekend());
        assertFalse(endOnWeekdayShift.involvesWeekend());
        assertNull(endOnWeekdayShift.getForbiddenWeekend());

        assertTrue(weekendShift.involvesWeekend());
        assertNotNull(weekendShift.getForbiddenWeekend());

        assertFalse(weekendShift.overlapsWith(weekendShift.getForbiddenWeekend()));

        assertTrue(startOnWeekendShift.involvesWeekend());
        assertNotNull(startOnWeekendShift.getForbiddenWeekend());
        assertTrue(endOnWeekendShift.involvesWeekend());
        assertNotNull(endOnWeekendShift.getForbiddenWeekend());
    }

    @Test
    public void isSameDay() {
        Period shift = new MockPeriod(1, 16, 0, 23, 59);
        assertTrue(shift.isSameDay());
        shift.advance(Calendar.MINUTE, 1);
        assertFalse(shift.isSameDay());
    }

    @Test
    public void getDaysFromDayUntilNextDay() {
        assertSame(7, Period.getDaysFromDayUntilNextDay(Calendar.WEDNESDAY, Calendar.WEDNESDAY));
        assertSame(3, Period.getDaysFromDayUntilNextDay(Calendar.WEDNESDAY, Calendar.SATURDAY));
        assertSame(5, Period.getDaysFromDayUntilNextDay(Calendar.WEDNESDAY, Calendar.MONDAY));
        assertSame(7, Period.getDaysFromDayUntilNextDay(Calendar.SATURDAY, Calendar.SATURDAY));
        assertSame(3, Period.getDaysFromDayUntilNextDay(Calendar.SATURDAY, Calendar.TUESDAY));
        assertSame(7, Period.getDaysFromDayUntilNextDay(Calendar.SUNDAY, Calendar.SUNDAY));
        assertSame(3, Period.getDaysFromDayUntilNextDay(Calendar.SUNDAY, Calendar.WEDNESDAY));
    }
}