package com.skepticalone.armour.data.entity;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComplianceCheckerTest {

    private static final Comparator<RosteredShiftEntity> SHIFT_COMPARATOR = new Comparator<RosteredShiftEntity>() {
        @Override
        public int compare(RosteredShiftEntity shift, RosteredShiftEntity other) {
            return shift.shiftData.start.compareTo(other.shiftData.start);
        }
    };
    private final RosteredShiftEntity.ComplianceChecker checker = new RosteredShiftEntity.ComplianceChecker(
            true,
            true,
            true,
            true,
            true
    );
    private final ArrayList<RosteredShiftEntity> shifts = new ArrayList<>();

    private static RosteredShiftEntity customShift(int year, int month, int startDayOfMonth, int startHour, int startMinute, int endDayOfMonth, int endHour, int endMinute) {
        return new RosteredShiftEntity(new ShiftData(
                new DateTime(year, month, startDayOfMonth, startHour, startMinute),
                new DateTime(year, month, endDayOfMonth, endHour, endMinute)
        ), null, null);
    }

    private static RosteredShiftEntity dayShift(int year, int month, int dayOfMonth, int startHour, int startMinute, int endHour, int endMinute) {
        return customShift(year, month, dayOfMonth, startHour, startMinute, dayOfMonth, endHour, endMinute);
    }

    private static RosteredShiftEntity nightShift(int year, int month, int startDayOfMonth, int startHour, int startMinute, int endHour, int endMinute) {
        return customShift(year, month, startDayOfMonth, startHour, startMinute, startDayOfMonth + 1, endHour, endMinute);
    }

    private void check() {
//        shifts.sort(SHIFT_COMPARATOR);
        checker.apply(shifts);
    }

    @Before
    public void setUp() throws Exception {
        shifts.clear();

        shifts.add(0, dayShift(2017, 5, 1, 8, 0, 16, 0));
        shifts.add(1, dayShift(2017, 5, 2, 8, 0, 16, 0));
        shifts.add(2, dayShift(2017, 5, 3, 8, 0, 16, 0));
        shifts.add(3, dayShift(2017, 5, 4, 8, 0, 16, 0));
        shifts.add(4, dayShift(2017, 5, 5, 8, 0, 22, 30));

        // weekend free

        shifts.add(5, dayShift(2017, 5, 8, 8, 0, 16, 0));
        shifts.add(6, dayShift(2017, 5, 9, 8, 0, 16, 0));
        shifts.add(7, dayShift(2017, 5, 10, 8, 0, 22, 30));
        shifts.add(8, dayShift(2017, 5, 11, 8, 0, 16, 0));
        shifts.add(9, dayShift(2017, 5, 12, 8, 0, 16, 0));

        // weekend worked
        shifts.add(10, dayShift(2017, 5, 13, 8, 0, 16, 0));
        shifts.add(11, dayShift(2017, 5, 14, 8, 0, 16, 0));

        shifts.add(12, dayShift(2017, 5, 15, 8, 0, 16, 0));
        shifts.add(13, dayShift(2017, 5, 16, 8, 0, 16, 0));
        shifts.add(14, dayShift(2017, 5, 17, 8, 0, 16, 0));
        shifts.add(15, dayShift(2017, 5, 18, 8, 0, 16, 0));
        shifts.add(16, dayShift(2017, 5, 19, 8, 0, 16, 0));

        // weekend free

        shifts.add(17, dayShift(2017, 5, 22, 8, 0, 16, 0));
        shifts.add(18, dayShift(2017, 5, 23, 8, 0, 16, 0));
        shifts.add(19, dayShift(2017, 5, 24, 8, 0, 22, 30));
        shifts.add(20, dayShift(2017, 5, 25, 8, 0, 16, 0));
        shifts.add(21, dayShift(2017, 5, 26, 8, 0, 16, 0));

        // weekend worked
        shifts.add(22, dayShift(2017, 5, 27, 8, 0, 22, 30));
        shifts.add(23, dayShift(2017, 5, 28, 8, 0, 16, 0));

        shifts.add(24, dayShift(2017, 5, 29, 8, 0, 16, 0));
        shifts.add(25, dayShift(2017, 5, 30, 8, 0, 16, 0));
        shifts.add(26, dayShift(2017, 5, 31, 8, 0, 16, 0));
        shifts.add(27, dayShift(2017, 6, 1, 8, 0, 16, 0));
        shifts.add(28, dayShift(2017, 6, 2, 8, 0, 16, 0));
    }

    @Test
    public void testBaselineCompliance() throws Exception {
        check();
        for (RosteredShiftEntity shift : shifts) {
            assertTrue(shift.isCompliant());
            assertFalse("exceedsMaximumDurationOverDay: " + shift.shiftData.toString(), shift.exceedsMaximumDurationOverDay());
            assertFalse("exceedsMaximumDurationOverWeek: " + shift.shiftData.toString(), shift.exceedsMaximumDurationOverWeek());
            assertFalse("exceedsMaximumDurationOverFortnight: " + shift.shiftData.toString(), shift.exceedsMaximumDurationOverFortnight());
            assertFalse("insufficientDurationBetweenShifts: " + shift.shiftData.toString(), shift.insufficientDurationBetweenShifts());
            assertFalse("consecutiveWeekendsWorked: " + shift.shiftData.toString(), shift.consecutiveWeekendsWorked());
        }
    }

    @Test
    public void consecutiveWeekendsWorked() throws Exception {
        shifts.set(16, nightShift(2017, 5, 19, 8, 0, 0, 0));
        check();
        assertTrue(shifts.get(16).isCompliant());
        assertFalse(shifts.get(16).consecutiveWeekendsWorked());
        shifts.set(16, nightShift(2017, 5, 19, 8, 0, 0, 1));
        check();
        assertFalse(shifts.get(16).isCompliant());
        assertTrue(shifts.get(16).consecutiveWeekendsWorked());
        shifts.set(16, dayShift(2017, 5, 22, 0, 0, 8, 0));
        check();
        assertTrue(shifts.get(16).isCompliant());
        assertFalse(shifts.get(16).consecutiveWeekendsWorked());
        shifts.set(16, nightShift(2017, 5, 21, 23, 59, 8, 0));
        check();
        assertFalse(shifts.get(16).isCompliant());
        assertTrue(shifts.get(16).consecutiveWeekendsWorked());
    }

}