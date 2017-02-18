package com.skepticalone.mecachecker;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class HeavySpanTest {

    @Test
    public void overlapsWith() {
        HeavySpan period = new HeavySpan(10, 20);

        assertFalse(period.overlapsWith(new HeavySpan(5, 9)));
        assertFalse(period.overlapsWith(new HeavySpan(5, 10)));
        assertTrue(period.overlapsWith(new HeavySpan(5, 15)));
        assertTrue(period.overlapsWith(new HeavySpan(5, 25)));

        assertTrue(period.overlapsWith(new HeavySpan(10, 15)));
        assertTrue(period.overlapsWith(new HeavySpan(10, 20)));
        assertTrue(period.overlapsWith(new HeavySpan(10, 25)));

        assertTrue(period.overlapsWith(new HeavySpan(15, 19)));
        assertTrue(period.overlapsWith(new HeavySpan(15, 20)));
        assertTrue(period.overlapsWith(new HeavySpan(15, 25)));

        assertFalse(period.overlapsWith(new HeavySpan(20, 25)));

        assertFalse(period.overlapsWith(new HeavySpan(25, 30)));
    }

    @Test
    public void getForbiddenWeekend() {

        HeavySpan weekDayShift = new MockPeriod(1, 8, 0, 16, 0);
        HeavySpan weekendShift = new MockPeriod(6, 8, 0, 16, 0);
        HeavySpan startOnWeekdayShift = new MockPeriod(8, 0, 0, 16, 0);
        HeavySpan startOnWeekendShift = new MockPeriod(7, 23, 59, 16, 0);
        HeavySpan endOnWeekdayShift = new MockPeriod(5, 22, 0, 0, 0);
        HeavySpan endOnWeekendShift = new MockPeriod(5, 22, 0, 8, 0);

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
        HeavySpan shift = new MockPeriod(1, 16, 0, 23, 59);
        assertTrue(shift.isSameDay());
        shift.advance(Calendar.MINUTE, 1);
        assertFalse(shift.isSameDay());
    }

    @Test
    public void getDaysFromDayUntilNextDay() {
        assertSame(7, HeavySpan.getDaysFromDayUntilNextDay(Calendar.WEDNESDAY, Calendar.WEDNESDAY));
        assertSame(3, HeavySpan.getDaysFromDayUntilNextDay(Calendar.WEDNESDAY, Calendar.SATURDAY));
        assertSame(5, HeavySpan.getDaysFromDayUntilNextDay(Calendar.WEDNESDAY, Calendar.MONDAY));
        assertSame(7, HeavySpan.getDaysFromDayUntilNextDay(Calendar.SATURDAY, Calendar.SATURDAY));
        assertSame(3, HeavySpan.getDaysFromDayUntilNextDay(Calendar.SATURDAY, Calendar.TUESDAY));
        assertSame(7, HeavySpan.getDaysFromDayUntilNextDay(Calendar.SUNDAY, Calendar.SUNDAY));
        assertSame(3, HeavySpan.getDaysFromDayUntilNextDay(Calendar.SUNDAY, Calendar.WEDNESDAY));
    }
}