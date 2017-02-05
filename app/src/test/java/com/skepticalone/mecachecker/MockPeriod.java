package com.skepticalone.mecachecker;

import java.util.Calendar;
import java.util.GregorianCalendar;

final class MockPeriod extends Period {

    private static final int DEFAULT_YEAR = 2017;
    private static final int DEFAULT_MONTH = Calendar.MAY;

    private MockPeriod(Calendar newStart, Calendar newEnd) {
        super(newStart, newEnd);
        while (!end.after(start)) {
            end.add(Calendar.DATE, 1);
        }
    }

    private MockPeriod(Calendar newStart, int endHourOfDay, int endMinute) {
        this(newStart, new GregorianCalendar(newStart.get(Calendar.YEAR), newStart.get(Calendar.MONTH), newStart.get(Calendar.DAY_OF_MONTH), endHourOfDay, endMinute));
    }

    MockPeriod(int dayOfMonth, int startHourOfDay, int startMinute, int endHourOfDay, int endMinute) {
        this(new GregorianCalendar(DEFAULT_YEAR, DEFAULT_MONTH, dayOfMonth, startHourOfDay, startMinute), endHourOfDay, endMinute);
    }

}