package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.skepticalone.armour.data.model.Item.NO_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComplianceConfigTest {

    private final SortedSet<ShiftSpec> shiftSpecs = new TreeSet<>();
    private final Shift.ShiftType.Configuration shiftConfig = new Shift.ShiftType.Configuration(480, 960, 480, 1350, 1320, 480);

    @Before
    public void prepareShiftSpecs() {
        shiftSpecs.clear();
        shiftSpecs.add(new ShiftSpec(0, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(1, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(2, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(3, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(4, 8, 0, 16, 0));

        // weekend worked
        shiftSpecs.add(new ShiftSpec(5, 8, 0, 22, 30));
        shiftSpecs.add(new ShiftSpec(6, 8, 0, 22, 30));

        shiftSpecs.add(new ShiftSpec(7, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(8, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(9, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(10, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(11, 8, 0, 16, 0));

        // weekend free

        shiftSpecs.add(new ShiftSpec(14, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(15, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(16, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(17, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(18, 8, 0, 16, 0));

        // weekend worked
        shiftSpecs.add(new ShiftSpec(19, 8, 0, 22, 30));
        shiftSpecs.add(new ShiftSpec(20, 8, 0, 22, 30));

        shiftSpecs.add(new ShiftSpec(21, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(22, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(23, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(24, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(25, 8, 0, 16, 0));

        // weekend free

        shiftSpecs.add(new ShiftSpec(28, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(29, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(30, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(31, 8, 0, 16, 0));

        // going onto 7 nights
        shiftSpecs.add(new ShiftSpec(32, 22, 0, 8, 0));
        shiftSpecs.add(new ShiftSpec(33, 22, 0, 8, 0));
        shiftSpecs.add(new ShiftSpec(34, 22, 0, 8, 0));
        shiftSpecs.add(new ShiftSpec(35, 22, 0, 8, 0));
        shiftSpecs.add(new ShiftSpec(36, 22, 0, 8, 0));
        shiftSpecs.add(new ShiftSpec(37, 22, 0, 8, 0));
        shiftSpecs.add(new ShiftSpec(38, 22, 0, 8, 0));

        // weekend free

        shiftSpecs.add(new ShiftSpec(42, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(43, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(44, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(45, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(46, 8, 0, 16, 0));

        // weekend free

        shiftSpecs.add(new ShiftSpec(49, 8, 0, 16, 0));
        shiftSpecs.add(new ShiftSpec(50, 8, 0, 16, 0));
    }

    @NonNull
    private List<RosteredShift> getRosteredShifts(@NonNull RosteredShift.Compliance.Configuration complianceConfig) {
        ZoneId zoneId = ZoneId.systemDefault();
        List<RosteredShift> shifts = new ArrayList<>();
        for (ShiftSpec spec : shiftSpecs) {
            shifts.add(new RosteredShift(
                    new RosteredShiftEntity(NO_ID, null, ShiftData.from(spec.start.atZone(zoneId), spec.end), null),
                    zoneId,
                    shiftConfig,
                    shifts,
                    complianceConfig
            ));
        }
        return shifts;
    }

    @NonNull
    private List<RosteredShift> getRosteredShiftsWithAllComplianceChecks() {
        return getRosteredShifts(new RosteredShift.Compliance.Configuration(
                true,
                true,
                true,
                true,
                true
        ));
    }

    private void checkCompliance(@NonNull List<RosteredShift> shifts) {
        for (RosteredShift shift : shifts) {
            //noinspection ConstantConditions
            assertTrue("compliesWithMaximumDurationOverDay: " + shift.getShiftData().toString(), shift.getCompliance().compliesWithMaximumDurationOverDay() == null || shift.getCompliance().compliesWithMaximumDurationOverDay());
            //noinspection ConstantConditions
            assertTrue("compliesWithMaximumDurationOverWeek: " + shift.getShiftData().toString(), shift.getCompliance().compliesWithMaximumDurationOverWeek() == null || shift.getCompliance().compliesWithMaximumDurationOverWeek());
            //noinspection ConstantConditions
            assertTrue("compliesWithMaximumDurationOverFortnight: " + shift.getShiftData().toString(), shift.getCompliance().compliesWithMaximumDurationOverFortnight() == null || shift.getCompliance().compliesWithMaximumDurationOverFortnight());
            //noinspection ConstantConditions
            assertTrue("sufficientDurationBetweenShifts: " + shift.getShiftData().toString(), shift.getCompliance().sufficientDurationBetweenShifts() == null || shift.getCompliance().sufficientDurationBetweenShifts());
            //noinspection ConstantConditions
            assertTrue("previousWeekendFree: " + shift.getShiftData().toString(), shift.getCompliance().previousWeekendFree() == null || shift.getCompliance().previousWeekendFree());
            assertTrue(shift.getCompliance().isCompliant());
        }
    }

    @Test
    public void checkAllCompliant() {
        checkCompliance(getRosteredShiftsWithAllComplianceChecks());
    }

    private void expectNonCompliant(@NonNull RosteredShift.Compliance.Configuration configuration, int indexOfNonCompliantShift) {
        checkCompliance(getRosteredShifts(configuration));
        List<RosteredShift> shifts = getRosteredShiftsWithAllComplianceChecks();
        RosteredShift.Compliance compliance = shifts.get(indexOfNonCompliantShift).getCompliance();
        //noinspection ConstantConditions
        assertEquals(configuration.checkDurationOverDay, compliance.compliesWithMaximumDurationOverDay() == null || compliance.compliesWithMaximumDurationOverDay());
        //noinspection ConstantConditions
        assertEquals(configuration.checkDurationOverWeek, compliance.compliesWithMaximumDurationOverWeek() == null || compliance.compliesWithMaximumDurationOverWeek());
        //noinspection ConstantConditions
        assertEquals(configuration.checkDurationOverFortnight, compliance.compliesWithMaximumDurationOverFortnight() == null || compliance.compliesWithMaximumDurationOverFortnight());
        //noinspection ConstantConditions
        assertEquals(configuration.checkDurationBetweenShifts, compliance.sufficientDurationBetweenShifts() == null || compliance.sufficientDurationBetweenShifts() == null);
        //noinspection ConstantConditions
        assertEquals(configuration.checkPreviousWeekendFree, compliance.previousWeekendFree() == null || compliance.previousWeekendFree());
        assertFalse(compliance.isCompliant());
    }

    @Test
    public void insufficientDurationBetweenShifts() {
        ShiftSpec spec = new ShiftSpec(50, 23, 59, 0, 0);
        assertTrue(shiftSpecs.add(spec));
        RosteredShift.Compliance.Configuration configuration = new RosteredShift.Compliance.Configuration(
                true,
                true,
                true,
                false,
                true
        );
        expectNonCompliant(configuration, shiftSpecs.size() - 1);
    }

    @Test
    public void exceedsMaximumDurationOverDay() {
        assertTrue(shiftSpecs.remove(shiftSpecs.last()));
        ShiftSpec spec = new ShiftSpec(50, 8, 0, 0, 0);
        assertTrue(shiftSpecs.add(spec));
        checkAllCompliant();
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(50, 7, 59, 0, 0);
        assertTrue(shiftSpecs.add(spec));
        RosteredShift.Compliance.Configuration configuration = new RosteredShift.Compliance.Configuration(
                false,
                true,
                true,
                true,
                true
        );
        expectNonCompliant(configuration, shiftSpecs.size() - 1);
    }

    @Test
    public void exceedsMaximumDurationOverWeek() {
        ShiftSpec spec = new ShiftSpec(7, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(7, 8, 0, 19, 0);
        assertTrue(shiftSpecs.add(spec));
        checkAllCompliant();
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(7, 8, 0, 19, 1);
        assertTrue(shiftSpecs.add(spec));
        RosteredShift.Compliance.Configuration configuration = new RosteredShift.Compliance.Configuration(
                true,
                false,
                true,
                true,
                true
        );
        expectNonCompliant(configuration, 7);
    }

    @Test
    public void exceedsMaximumDurationOverFortnight() {
        ShiftSpec spec;
        spec = new ShiftSpec(0, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(0, 8, 0, 19, 0);
        assertTrue(shiftSpecs.add(spec));
        spec = new ShiftSpec(1, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(1, 8, 0, 20, 0);
        assertTrue(shiftSpecs.add(spec));
        spec = new ShiftSpec(2, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(2, 8, 0, 19, 0);
        assertTrue(shiftSpecs.add(spec));
        spec = new ShiftSpec(3, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(3, 8, 0, 20, 0);
        assertTrue(shiftSpecs.add(spec));
        spec = new ShiftSpec(4, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(4, 8, 0, 19, 0);
        assertTrue(shiftSpecs.add(spec));

        spec = new ShiftSpec(7, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(7, 8, 0, 20, 0);
        assertTrue(shiftSpecs.add(spec));
        spec = new ShiftSpec(8, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(8, 8, 0, 19, 0);
        assertTrue(shiftSpecs.add(spec));
        spec = new ShiftSpec(9, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(9, 8, 0, 20, 0);
        assertTrue(shiftSpecs.add(spec));
        spec = new ShiftSpec(10, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(10, 8, 0, 19, 0);
        assertTrue(shiftSpecs.add(spec));
        spec = new ShiftSpec(11, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(11, 8, 0, 20, 0);
        assertTrue(shiftSpecs.add(spec));

        RosteredShift.Compliance.Configuration configuration = new RosteredShift.Compliance.Configuration(
                true,
                false,
                true,
                true,
                true
        );
        checkCompliance(getRosteredShifts(configuration));

        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(11, 8, 0, 20, 1);
        assertTrue(shiftSpecs.add(spec));

        configuration = new RosteredShift.Compliance.Configuration(
                true,
                false,
                false,
                true,
                true
        );
        expectNonCompliant(configuration, 11);
    }

    @Test
    public void consecutiveWeekendsWorked() {
        ShiftSpec spec;
        spec = new ShiftSpec(11, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(11, 16, 0, 0, 0);
        assertTrue(shiftSpecs.add(spec));
        checkAllCompliant();
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(11, 16, 0, 0, 1);
        assertTrue(shiftSpecs.add(spec));
        RosteredShift.Compliance.Configuration configuration = new RosteredShift.Compliance.Configuration(
                true,
                true,
                true,
                true,
                false
        );
        expectNonCompliant(configuration, 11);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(11, 16, 0, 0, 0);
        assertTrue(shiftSpecs.add(spec));
        spec = new ShiftSpec(14, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(14, 0, 0, 8, 0);
        assertTrue(shiftSpecs.add(spec));
        checkAllCompliant();
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(13, 23, 59, 0, 0);
        assertTrue(shiftSpecs.add(spec));
        expectNonCompliant(configuration, 17);

    }

    private static final class ShiftSpec implements Comparable<ShiftSpec> {

        private static final LocalDate START_DATE = LocalDate.of(2017, 5, 1);
        private final LocalDateTime start;
        private final LocalTime end;

        private ShiftSpec(int daysAfterStart, int startHour, int startMinute, int endHour, int endMinute) {
            start = LocalDateTime.of(START_DATE.plusDays(daysAfterStart), LocalTime.of(startHour, startMinute));
            end = LocalTime.of(endHour, endMinute);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ShiftSpec) {
                ShiftSpec other = (ShiftSpec) obj;
                return start.isEqual(other.start);
            }
            return false;
        }

        @Override
        public int compareTo(@NonNull ShiftSpec other) {
            return start.compareTo(other.start);
        }

    }

}