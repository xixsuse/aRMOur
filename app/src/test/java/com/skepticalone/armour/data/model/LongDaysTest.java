package com.skepticalone.armour.data.model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LongDaysTest extends RosteredShiftTest {

    @Override
    public void prepareShiftSpecs() {
        super.prepareShiftSpecs();
        assertTrue(shiftSpecs.add(new ShiftSpec(0, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(1, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 8, 0, 22, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(3, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 8, 0, 22, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(9, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(10, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(11, 8, 0, 16, 30)));
    }


    @SuppressWarnings("ConstantConditions")
    @Test
    public void defineLongDays() {
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckLongDaysPerWeek(true));
        for (int i = 0; i < rosteredShifts.size(); i++) {
            Compliance compliance = rosteredShifts.get(i).getCompliance();
            assertNull(compliance.getIndexOfNightShift());
            if (i == 2 || i == 4) {
                assertNotNull(compliance.getIndexOfLongDay());
                assertTrue(compliance.compliesWithMaximumLongDaysPerWeek());
            } else {
                assertNull(compliance.getIndexOfLongDay());
                assertNull(compliance.compliesWithMaximumLongDaysPerWeek());
            }
            assertTrue(compliance.isCompliant());
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void checkMaximumLongDaysPerWeek() {
        assertTrue(shiftSpecs.remove(new ShiftSpec(9, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.remove(new ShiftSpec(10, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.remove(new ShiftSpec(11, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(8, 8, 0, 22, 30)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckLongDaysPerWeek(true));
        Compliance compliance = getRosteredShifts(NONE_COMPLIANT.withCheckLongDaysPerWeek(true)).get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(2, compliance.getIndexOfLongDay().intValue());
        assertFalse(compliance.compliesWithMaximumLongDaysPerWeek());
        assertTrue(shiftSpecs.remove(new ShiftSpec(8, 8, 0, 22, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(9, 8, 0, 22, 30)));
        rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckLongDaysPerWeek(true));
        compliance = getRosteredShifts(NONE_COMPLIANT.withCheckLongDaysPerWeek(true)).get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(1, compliance.getIndexOfLongDay().intValue());
        assertTrue(compliance.compliesWithMaximumLongDaysPerWeek());
        assertTrue(shiftSpecs.remove(new ShiftSpec(9, 8, 0, 22, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(11, 8, 0, 22, 30)));
        rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckLongDaysPerWeek(true));
        compliance = getRosteredShifts(NONE_COMPLIANT.withCheckLongDaysPerWeek(true)).get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(0, compliance.getIndexOfLongDay().intValue());
        assertTrue(compliance.compliesWithMaximumLongDaysPerWeek());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void doNotIncrementOrResetCounterIfNightShift() {
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 23, 0, 6, 30)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckConsecutiveDays(true));
        assertNull(rosteredShifts.get(0).getCompliance().getIndexOfLongDay());
        assertNull(rosteredShifts.get(1).getCompliance().getIndexOfLongDay());
        assertEquals(0, rosteredShifts.get(2).getCompliance().getIndexOfLongDay().intValue());
        assertNotNull(rosteredShifts.get(3).getCompliance().getIndexOfNightShift());
        assertNull(rosteredShifts.get(3).getCompliance().getIndexOfLongDay());
        assertNull(rosteredShifts.get(4).getCompliance().getIndexOfLongDay());
        assertEquals(1, rosteredShifts.get(5).getCompliance().getIndexOfLongDay().intValue());
    }
}