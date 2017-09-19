package com.skepticalone.armour.data.entity;

import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.DateTime;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComplianceCheckerTest {

    private static final int DEFAULT_YEAR = 2017;
    private final ArrayList<RosteredShiftEntity> shifts = new ArrayList<>();
    private RosteredShiftEntity.ComplianceConfiguration checker;

    @SuppressWarnings("SameParameterValue")
    private static RosteredShiftEntity newShift(int month, int startDayOfMonth, int startHour, int startMinute, int endHour, int endMinute) {
        DateTime start = new DateTime(DEFAULT_YEAR, month, startDayOfMonth, startHour, startMinute),
                end = new DateTime(DEFAULT_YEAR, month, startDayOfMonth, endHour, endMinute);
        while (!end.isAfter(start)) {
            end = end.plusDays(1);
        }
        return new RosteredShiftEntity(new ShiftData(start, end), null, null);
    }

    private void adjustByMinutesAndCheck(int shiftIndex, int startMinutes, int endMinutes) {
        RosteredShiftEntity shift = shifts.get(shiftIndex);
        shifts.set(shiftIndex, new RosteredShiftEntity(new ShiftData(
                shift.getShiftData().start.plusMinutes(startMinutes),
                shift.getShiftData().end.plusMinutes(endMinutes)
        ), null, null));
        checker.process(shifts, );
    }

    @Before
    public void setUp() {
        checker = new RosteredShiftEntity.ComplianceConfiguration(
                true,
                true,
                true,
                true,
                true
        );
        shifts.clear();

        shifts.add(0, newShift(5, 1, 8, 0, 16, 0));
        shifts.add(1, newShift(5, 2, 8, 0, 16, 0));
        shifts.add(2, newShift(5, 3, 8, 0, 16, 0));
        shifts.add(3, newShift(5, 4, 8, 0, 16, 0));
        shifts.add(4, newShift(5, 5, 8, 0, 16, 0));

        // weekend worked
        shifts.add(5, newShift(5, 6, 8, 0, 22, 30));
        shifts.add(6, newShift(5, 7, 8, 0, 22, 30));

        shifts.add(7, newShift(5, 8, 8, 0, 16, 0));
        shifts.add(8, newShift(5, 9, 8, 0, 16, 0));
        shifts.add(9, newShift(5, 10, 8, 0, 16, 0));
        shifts.add(10, newShift(5, 11, 8, 0, 16, 0));
        shifts.add(11, newShift(5, 12, 8, 0, 16, 0));

        // weekend free

        shifts.add(12, newShift(5, 15, 8, 0, 16, 0));
        shifts.add(13, newShift(5, 16, 8, 0, 16, 0));
        shifts.add(14, newShift(5, 17, 8, 0, 16, 0));
        shifts.add(15, newShift(5, 18, 8, 0, 16, 0));
        shifts.add(16, newShift(5, 19, 8, 0, 16, 0));

        // weekend worked
        shifts.add(17, newShift(5, 20, 8, 0, 22, 30));
        shifts.add(18, newShift(5, 21, 8, 0, 22, 30));

        shifts.add(19, newShift(5, 22, 8, 0, 16, 0));
        shifts.add(20, newShift(5, 23, 8, 0, 16, 0));
        shifts.add(21, newShift(5, 24, 8, 0, 16, 0));
        shifts.add(22, newShift(5, 25, 8, 0, 16, 0));
        shifts.add(23, newShift(5, 26, 8, 0, 16, 0));

        // weekend free

        shifts.add(24, newShift(5, 29, 8, 0, 16, 0));
        shifts.add(25, newShift(5, 30, 8, 0, 16, 0));
        shifts.add(26, newShift(5, 31, 8, 0, 16, 0));
        shifts.add(27, newShift(6, 1, 8, 0, 16, 0));

        // going onto 7 nights
        shifts.add(28, newShift(6, 2, 22, 0, 8, 0));
        shifts.add(29, newShift(6, 3, 22, 0, 8, 0));
        shifts.add(30, newShift(6, 4, 22, 0, 8, 0));
        shifts.add(31, newShift(6, 5, 22, 0, 8, 0));
        shifts.add(32, newShift(6, 6, 22, 0, 8, 0));
        shifts.add(33, newShift(6, 7, 22, 0, 8, 0));
        shifts.add(34, newShift(6, 8, 22, 0, 8, 0));

        // weekend free

        shifts.add(35, newShift(6, 12, 8, 0, 16, 0));
        shifts.add(36, newShift(6, 13, 8, 0, 16, 0));
        shifts.add(37, newShift(6, 14, 8, 0, 16, 0));
        shifts.add(38, newShift(6, 15, 8, 0, 16, 0));
        shifts.add(39, newShift(6, 16, 8, 0, 16, 0));

        // weekend free

        shifts.add(40, newShift(6, 19, 8, 0, 16, 0));
        shifts.add(41, newShift(6, 20, 8, 0, 16, 0));
    }

    @Test
    public void initialData() {
        checker.process(shifts, );
        for (RosteredShiftEntity shift : shifts) {
            assertTrue(shift.isCompliant());
            assertFalse("exceedsMaximumDurationOverDay: " + shift.getShiftData().toString(), shift.exceedsMaximumDurationOverDay());
            assertFalse("exceedsMaximumDurationOverWeek: " + shift.getShiftData().toString(), shift.exceedsMaximumDurationOverWeek());
            assertFalse("exceedsMaximumDurationOverFortnight: " + shift.getShiftData().toString(), shift.exceedsMaximumDurationOverFortnight());
            assertFalse("insufficientDurationBetweenShifts: " + shift.getShiftData().toString(), shift.insufficientDurationBetweenShifts());
            assertFalse("consecutiveWeekendsWorked: " + shift.getShiftData().toString(), shift.consecutiveWeekendsWorked());
        }
    }

    @Test
    public void insufficientDurationBetweenShifts() {
        adjustByMinutesAndCheck(11, -480, -900);
        assertTrue(shifts.get(11).isCompliant());
        assertFalse(shifts.get(11).insufficientDurationBetweenShifts());
        adjustByMinutesAndCheck(11, -1, -1);
        assertFalse(shifts.get(11).isCompliant());
        assertTrue(shifts.get(11).insufficientDurationBetweenShifts());
        new RosteredShiftEntity.ComplianceConfiguration(
                true,
                true,
                true,
                false,
                true
        ).process(shifts, );
        assertTrue(shifts.get(11).isCompliant());
        assertFalse(shifts.get(11).insufficientDurationBetweenShifts());
    }

    @Test
    public void exceedsMaximumDurationOverDay() {
        adjustByMinutesAndCheck(31, 0, 360);
        assertTrue(shifts.get(31).isCompliant());
        assertFalse(shifts.get(31).exceedsMaximumDurationOverDay());
        adjustByMinutesAndCheck(31, 0, 1);
        assertFalse(shifts.get(31).isCompliant());
        assertTrue(shifts.get(31).exceedsMaximumDurationOverDay());
        new RosteredShiftEntity.ComplianceConfiguration(
                false,
                true,
                true,
                true,
                true
        ).process(shifts, );
        assertTrue(shifts.get(31).isCompliant());
        assertFalse(shifts.get(31).exceedsMaximumDurationOverDay());
    }

    @Test
    public void exceedsMaximumDurationOverWeek() {
        adjustByMinutesAndCheck(7, 0, 180);
        assertTrue(shifts.get(7).isCompliant());
        assertFalse(shifts.get(7).exceedsMaximumDurationOverWeek());
        adjustByMinutesAndCheck(7, 0, 1);
        assertFalse(shifts.get(7).isCompliant());
        assertTrue(shifts.get(7).exceedsMaximumDurationOverWeek());
        new RosteredShiftEntity.ComplianceConfiguration(
                true,
                false,
                true,
                true,
                true
        ).process(shifts, );
        assertTrue(shifts.get(7).isCompliant());
        assertFalse(shifts.get(7).exceedsMaximumDurationOverWeek());
    }

    @Test
    public void exceedsMaximumDurationOverFortnight() {
        checker = new RosteredShiftEntity.ComplianceConfiguration(
                true,
                false,
                true,
                true,
                true
        );
        adjustByMinutesAndCheck(0, 0, 180);
        adjustByMinutesAndCheck(1, 0, 240);
        adjustByMinutesAndCheck(2, 0, 180);
        adjustByMinutesAndCheck(3, 0, 240);
        adjustByMinutesAndCheck(4, 0, 180);
        adjustByMinutesAndCheck(7, 0, 240);
        adjustByMinutesAndCheck(8, 0, 180);
        adjustByMinutesAndCheck(9, 0, 240);
        adjustByMinutesAndCheck(10, 0, 180);
        adjustByMinutesAndCheck(11, 0, 240);
        assertTrue(shifts.get(11).isCompliant());
        assertFalse(shifts.get(11).exceedsMaximumDurationOverFortnight());
        adjustByMinutesAndCheck(11, 0, 1);
        assertFalse(shifts.get(11).isCompliant());
        assertTrue(shifts.get(11).exceedsMaximumDurationOverFortnight());
        checker = new RosteredShiftEntity.ComplianceConfiguration(
                true,
                false,
                false,
                true,
                true
        );
        checker.process(shifts, );
        assertTrue(shifts.get(11).isCompliant());
        assertFalse(shifts.get(11).exceedsMaximumDurationOverFortnight());
    }

    @Test
    public void consecutiveWeekendsWorked() {
        shifts.set(11, newShift(5, 12, 8, 0, 16, 0));
        adjustByMinutesAndCheck(11, 480, 480);
        assertTrue(shifts.get(17).isCompliant());
        assertFalse(shifts.get(17).consecutiveWeekendsWorked());
        adjustByMinutesAndCheck(11, 1, 1);
        assertFalse(shifts.get(17).isCompliant());
        assertTrue(shifts.get(17).consecutiveWeekendsWorked());
        checker = new RosteredShiftEntity.ComplianceConfiguration(
                true,
                true,
                true,
                true,
                false
        );
        checker.process(shifts, );
        assertTrue(shifts.get(17).isCompliant());
        assertFalse(shifts.get(17).consecutiveWeekendsWorked());
        checker = new RosteredShiftEntity.ComplianceConfiguration(
                true,
                true,
                true,
                true,
                true
        );
        adjustByMinutesAndCheck(11, -481, -481);
        assertTrue(shifts.get(17).isCompliant());
        assertFalse(shifts.get(17).consecutiveWeekendsWorked());
        adjustByMinutesAndCheck(12, -480, -480);
        assertTrue(shifts.get(17).isCompliant());
        assertFalse(shifts.get(17).consecutiveWeekendsWorked());
        adjustByMinutesAndCheck(12, -1, -1);
        assertFalse(shifts.get(17).isCompliant());
        assertTrue(shifts.get(17).consecutiveWeekendsWorked());
        checker = new RosteredShiftEntity.ComplianceConfiguration(
                true,
                true,
                true,
                true,
                false
        );
        checker.process(shifts, );
        assertTrue(shifts.get(17).isCompliant());
        assertFalse(shifts.get(17).consecutiveWeekendsWorked());
    }

}