package com.skepticalone.armour.data.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ComplianceConfigTest extends RosteredShiftTest {

    @Override
    public void prepareShiftSpecs() {
        super.prepareShiftSpecs();
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

    @Test
    public void insufficientDurationBetweenShifts() {
        ShiftSpec spec = new ShiftSpec(50, 23, 59, 0, 0);
        assertTrue(shiftSpecs.add(spec));
        expectNonCompliant(ALL_COMPLIANT.withCheckDurationBetweenShifts(false), shiftSpecs.size() - 1);
    }

    @Test
    public void exceedsMaximumDurationOverDay() {
        assertTrue(shiftSpecs.remove(shiftSpecs.last()));
        ShiftSpec spec = new ShiftSpec(50, 8, 0, 0, 0);
        assertTrue(shiftSpecs.add(spec));
        checkBaselineCompliant();
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(50, 7, 59, 0, 0);
        assertTrue(shiftSpecs.add(spec));
        expectNonCompliant(ALL_COMPLIANT.withCheckDurationOverDay(false), shiftSpecs.size() - 1);
    }

    @Test
    public void exceedsMaximumDurationOverWeek() {
        ShiftSpec spec = new ShiftSpec(7, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(7, 8, 0, 19, 0);
        assertTrue(shiftSpecs.add(spec));
        checkBaselineCompliant();
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(7, 8, 0, 19, 1);
        assertTrue(shiftSpecs.add(spec));
        expectNonCompliant(ALL_COMPLIANT.withCheckDurationOverWeek(false), 7);
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
        expectCompliant(getRosteredShifts(ALL_COMPLIANT.withCheckDurationOverWeek(false)));

        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(11, 8, 0, 20, 1);
        assertTrue(shiftSpecs.add(spec));
        expectNonCompliant(ALL_COMPLIANT.withCheckDurationOverWeek(false).withCheckDurationOverFortnight(false), 11);
    }

    @Test
    public void consecutiveWeekendsWorked() {
        ShiftSpec spec;
        spec = new ShiftSpec(11, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(11, 16, 0, 0, 0);
        assertTrue(shiftSpecs.add(spec));
        checkBaselineCompliant();
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(11, 16, 0, 0, 1);
        assertTrue(shiftSpecs.add(spec));
        expectNonCompliant(ALL_COMPLIANT.withCheckConsecutiveWeekends(false), 11);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(11, 16, 0, 0, 0);
        assertTrue(shiftSpecs.add(spec));
        spec = new ShiftSpec(14, 8, 0, 16, 0);
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(14, 0, 0, 8, 0);
        assertTrue(shiftSpecs.add(spec));
        checkBaselineCompliant();
        assertTrue(shiftSpecs.remove(spec));
        spec = new ShiftSpec(13, 23, 59, 0, 0);
        assertTrue(shiftSpecs.add(spec));
        expectNonCompliant(ALL_COMPLIANT.withCheckConsecutiveWeekends(false), 17);

    }

}