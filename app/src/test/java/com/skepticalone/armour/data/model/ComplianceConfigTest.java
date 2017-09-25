package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

import static com.skepticalone.armour.data.model.Item.NO_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComplianceConfigTest {

    private static final int DEFAULT_YEAR = 2017;
    private final ArrayList<RosteredShift> shifts = new ArrayList<>();
    private final Shift.ShiftType.Configuration shiftConfig = new Shift.ShiftType.Configuration(480, 960, 480, 1350, 1320, 480);
    private final RosteredShift.Compliance.Configuration complianceConfig = new RosteredShift.Compliance.Configuration(
            true,
            true,
            true,
            true,
            true
    );

    private static RosteredShift newShift(int month, int startDayOfMonth, int startHour, @SuppressWarnings("SameParameterValue") int startMinute, int endHour, int endMinute,
                                          @NonNull ZoneId zoneId,
                                          @NonNull Shift.ShiftType.Configuration shiftConfig,
                                          @NonNull List<RosteredShift> previousShifts,
                                          @NonNull RosteredShift.Compliance.Configuration complianceConfig) {
        ZonedDateTime start = ZonedDateTime.of(DEFAULT_YEAR, month, startDayOfMonth, startHour, startMinute, 0, 0, zoneId),
                end = ZonedDateTime.of(DEFAULT_YEAR, month, startDayOfMonth, endHour, endMinute, 0, 0, zoneId);
        while (!end.isAfter(start)) {
            end = end.plusDays(1);
        }
        return new RosteredShift(
                new RawRosteredShiftEntity(NO_ID, null, new RawShift.ShiftData(start.toInstant(), end.toInstant()), null),
                zoneId,
                shiftConfig,
                previousShifts,
                complianceConfig
        );
    }
//
//    private void adjustByMinutesAndCheck(int shiftIndex, int startMinutes, int endMinutes) {
//        RosteredShift shift = shifts.get(shiftIndex);
//        shifts.set(shiftIndex, new RosteredShift(new RawShift.RawShiftData(
//                shift.getShiftData().getStart().plusSeconds(startMinutes * 60),
//                shift.getShiftData().getEnd().plusSeconds(endMinutes * 60)
//        ), null, null));
//        checker.process(shifts, ZoneId.systemDefault());
//    }

    @Before
    public void setUp() {

        ZoneId zoneId = ZoneId.systemDefault();

        shifts.clear();

        shifts.add(0, newShift(5, 1, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(1, newShift(5, 2, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(2, newShift(5, 3, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(3, newShift(5, 4, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(4, newShift(5, 5, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));

        // weekend worked
        shifts.add(5, newShift(5, 6, 8, 0, 22, 30, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(6, newShift(5, 7, 8, 0, 22, 30, zoneId, shiftConfig, shifts, complianceConfig));

        shifts.add(7, newShift(5, 8, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(8, newShift(5, 9, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(9, newShift(5, 10, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(10, newShift(5, 11, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(11, newShift(5, 12, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));

        // weekend free

        shifts.add(12, newShift(5, 15, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(13, newShift(5, 16, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(14, newShift(5, 17, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(15, newShift(5, 18, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(16, newShift(5, 19, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));

        // weekend worked
        shifts.add(17, newShift(5, 20, 8, 0, 22, 30, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(18, newShift(5, 21, 8, 0, 22, 30, zoneId, shiftConfig, shifts, complianceConfig));

        shifts.add(19, newShift(5, 22, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(20, newShift(5, 23, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(21, newShift(5, 24, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(22, newShift(5, 25, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(23, newShift(5, 26, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));

        // weekend free

        shifts.add(24, newShift(5, 29, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(25, newShift(5, 30, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(26, newShift(5, 31, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(27, newShift(6, 1, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));

        // going onto 7 nights
        shifts.add(28, newShift(6, 2, 22, 0, 8, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(29, newShift(6, 3, 22, 0, 8, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(30, newShift(6, 4, 22, 0, 8, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(31, newShift(6, 5, 22, 0, 8, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(32, newShift(6, 6, 22, 0, 8, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(33, newShift(6, 7, 22, 0, 8, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(34, newShift(6, 8, 22, 0, 8, 0, zoneId, shiftConfig, shifts, complianceConfig));

        // weekend free

        shifts.add(35, newShift(6, 12, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(36, newShift(6, 13, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(37, newShift(6, 14, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(38, newShift(6, 15, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(39, newShift(6, 16, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));

        // weekend free

        shifts.add(40, newShift(6, 19, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
        shifts.add(41, newShift(6, 20, 8, 0, 16, 0, zoneId, shiftConfig, shifts, complianceConfig));
    }

    @Test
    public void initialData() {
        for (RosteredShift shift : shifts) {
            assertTrue(shift.getCompliance().isCompliant());
            assertFalse("exceedsMaximumDurationOverDay: " + shift.getShiftData().toString(), shift.getCompliance().exceedsMaximumDurationOverDay());
            assertFalse("exceedsMaximumDurationOverWeek: " + shift.getShiftData().toString(), shift.getCompliance().exceedsMaximumDurationOverWeek());
            assertFalse("exceedsMaximumDurationOverFortnight: " + shift.getShiftData().toString(), shift.getCompliance().exceedsMaximumDurationOverFortnight());
            assertFalse("insufficientDurationBetweenShifts: " + shift.getShiftData().toString(), shift.getCompliance().insufficientDurationBetweenShifts());
            assertFalse("consecutiveWeekendsWorked: " + shift.getShiftData().toString(), shift.getCompliance().consecutiveWeekendsWorked());
        }
    }
//
//    @Test
//    public void insufficientDurationBetweenShifts() {
//        adjustByMinutesAndCheck(11, -480, -900);
//        assertTrue(shifts.get(11).isCompliant());
//        assertFalse(shifts.get(11).insufficientDurationBetweenShifts());
//        adjustByMinutesAndCheck(11, -1, -1);
//        assertFalse(shifts.get(11).isCompliant());
//        assertTrue(shifts.get(11).insufficientDurationBetweenShifts());
//        new RawRosteredShiftEntity.ComplianceConfiguration(
//                true,
//                true,
//                true,
//                false,
//                true
//        ).process(shifts, ZoneId.systemDefault());
//        assertTrue(shifts.get(11).isCompliant());
//        assertFalse(shifts.get(11).insufficientDurationBetweenShifts());
//    }
//
//    @Test
//    public void exceedsMaximumDurationOverDay() {
//        adjustByMinutesAndCheck(31, 0, 360);
//        assertTrue(shifts.get(31).isCompliant());
//        assertFalse(shifts.get(31).exceedsMaximumDurationOverDay());
//        adjustByMinutesAndCheck(31, 0, 1);
//        assertFalse(shifts.get(31).isCompliant());
//        assertTrue(shifts.get(31).exceedsMaximumDurationOverDay());
//        new RawRosteredShiftEntity.ComplianceConfiguration(
//                false,
//                true,
//                true,
//                true,
//                true
//        ).process(shifts, ZoneId.systemDefault());
//        assertTrue(shifts.get(31).isCompliant());
//        assertFalse(shifts.get(31).exceedsMaximumDurationOverDay());
//    }
//
//    @Test
//    public void exceedsMaximumDurationOverWeek() {
//        adjustByMinutesAndCheck(7, 0, 180);
//        assertTrue(shifts.get(7).isCompliant());
//        assertFalse(shifts.get(7).exceedsMaximumDurationOverWeek());
//        adjustByMinutesAndCheck(7, 0, 1);
//        assertFalse(shifts.get(7).isCompliant());
//        assertTrue(shifts.get(7).exceedsMaximumDurationOverWeek());
//        new RawRosteredShiftEntity.ComplianceConfiguration(
//                true,
//                false,
//                true,
//                true,
//                true
//        ).process(shifts, ZoneId.systemDefault());
//        assertTrue(shifts.get(7).isCompliant());
//        assertFalse(shifts.get(7).exceedsMaximumDurationOverWeek());
//    }
//
//    @Test
//    public void exceedsMaximumDurationOverFortnight() {
//        checker = new RawRosteredShiftEntity.ComplianceConfiguration(
//                true,
//                false,
//                true,
//                true,
//                true
//        );
//        adjustByMinutesAndCheck(0, 0, 180);
//        adjustByMinutesAndCheck(1, 0, 240);
//        adjustByMinutesAndCheck(2, 0, 180);
//        adjustByMinutesAndCheck(3, 0, 240);
//        adjustByMinutesAndCheck(4, 0, 180);
//        adjustByMinutesAndCheck(7, 0, 240);
//        adjustByMinutesAndCheck(8, 0, 180);
//        adjustByMinutesAndCheck(9, 0, 240);
//        adjustByMinutesAndCheck(10, 0, 180);
//        adjustByMinutesAndCheck(11, 0, 240);
//        assertTrue(shifts.get(11).isCompliant());
//        assertFalse(shifts.get(11).exceedsMaximumDurationOverFortnight());
//        adjustByMinutesAndCheck(11, 0, 1);
//        assertFalse(shifts.get(11).isCompliant());
//        assertTrue(shifts.get(11).exceedsMaximumDurationOverFortnight());
//        checker = new RawRosteredShiftEntity.ComplianceConfiguration(
//                true,
//                false,
//                false,
//                true,
//                true
//        );
//        checker.process(shifts, ZoneId.systemDefault());
//        assertTrue(shifts.get(11).isCompliant());
//        assertFalse(shifts.get(11).exceedsMaximumDurationOverFortnight());
//    }
//
//    @Test
//    public void consecutiveWeekendsWorked() {
//        shifts.set(11, newShift(5, 12, 8, 0, 16, 0));
//        adjustByMinutesAndCheck(11, 480, 480);
//        assertTrue(shifts.get(17).isCompliant());
//        assertFalse(shifts.get(17).consecutiveWeekendsWorked());
//        adjustByMinutesAndCheck(11, 1, 1);
//        assertFalse(shifts.get(17).isCompliant());
//        assertTrue(shifts.get(17).consecutiveWeekendsWorked());
//        checker = new RawRosteredShiftEntity.ComplianceConfiguration(
//                true,
//                true,
//                true,
//                true,
//                false
//        );
//        checker.process(shifts, ZoneId.systemDefault());
//        assertTrue(shifts.get(17).isCompliant());
//        assertFalse(shifts.get(17).consecutiveWeekendsWorked());
//        checker = new RawRosteredShiftEntity.ComplianceConfiguration(
//                true,
//                true,
//                true,
//                true,
//                true
//        );
//        adjustByMinutesAndCheck(11, -481, -481);
//        assertTrue(shifts.get(17).isCompliant());
//        assertFalse(shifts.get(17).consecutiveWeekendsWorked());
//        adjustByMinutesAndCheck(12, -480, -480);
//        assertTrue(shifts.get(17).isCompliant());
//        assertFalse(shifts.get(17).consecutiveWeekendsWorked());
//        adjustByMinutesAndCheck(12, -1, -1);
//        assertFalse(shifts.get(17).isCompliant());
//        assertTrue(shifts.get(17).consecutiveWeekendsWorked());
//        checker = new RawRosteredShiftEntity.ComplianceConfiguration(
//                true,
//                true,
//                true,
//                true,
//                false
//        );
//        checker.process(shifts, ZoneId.systemDefault());
//        assertTrue(shifts.get(17).isCompliant());
//        assertFalse(shifts.get(17).consecutiveWeekendsWorked());
//    }

}