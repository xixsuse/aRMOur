package com.skepticalone.armour.data.model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class ConsecutiveDaysTest extends RosteredShiftTest {

    @Override
    public void prepareShiftSpecs() {
        super.prepareShiftSpecs();
        assertTrue(shiftSpecs.add(new ShiftSpec(0, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(1, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(3, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(7, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(8, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(9, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(10, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(11, 8, 0, 16, 30)));
    }

    @Test
    public void checkConsecutiveDays() {
        List<RosteredShift> rosteredShifts;
        rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckConsecutiveDays(true));
        for (int i = 0; i < rosteredShifts.size(); i++) {
            Compliance compliance = rosteredShifts.get(i).getCompliance();
            assertNull(compliance.getIndexOfNightShift());
            assertEquals(i, compliance.getIndexOfDay().intValue());
            assertTrue(compliance.compliesWithMaximumDaysPerWeek());
            assertTrue(compliance.isCompliant());
        }
        assertTrue(shiftSpecs.add(new ShiftSpec(12, 8, 0, 16, 30)));
        rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckConsecutiveDays(true));
        Compliance compliance = rosteredShifts.get(12).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(12, compliance.getIndexOfDay().intValue());
        assertFalse(compliance.compliesWithMaximumDaysPerWeek());
        assertFalse(compliance.isCompliant());
    }

    @Test
    public void resetCounterAfterSkippedDays() {
        assertTrue(shiftSpecs.remove(new ShiftSpec(2, 8, 0, 16, 30)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckConsecutiveDays(true));
        assertEquals(0, rosteredShifts.get(0).getCompliance().getIndexOfDay().intValue());
        assertEquals(1, rosteredShifts.get(1).getCompliance().getIndexOfDay().intValue());
        assertEquals(0, rosteredShifts.get(2).getCompliance().getIndexOfDay().intValue());
        assertEquals(1, rosteredShifts.get(3).getCompliance().getIndexOfDay().intValue());
    }

    @Test
    public void incrementCounterIfLongDay() {
        assertTrue(shiftSpecs.remove(new ShiftSpec(2, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 8, 0, 22, 30)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckConsecutiveDays(true));
        assertEquals(0, rosteredShifts.get(0).getCompliance().getIndexOfDay().intValue());
        assertEquals(1, rosteredShifts.get(1).getCompliance().getIndexOfDay().intValue());
        assertNull(rosteredShifts.get(1).getCompliance().getIndexOfLongDay());
        assertEquals(2, rosteredShifts.get(2).getCompliance().getIndexOfDay().intValue());
        assertNotNull(rosteredShifts.get(2).getCompliance().getIndexOfLongDay());
        assertEquals(3, rosteredShifts.get(3).getCompliance().getIndexOfDay().intValue());
        assertNull(rosteredShifts.get(3).getCompliance().getIndexOfLongDay());
        assertEquals(4, rosteredShifts.get(4).getCompliance().getIndexOfDay().intValue());
    }

    @Test
    public void doNotIncrementCounterUnlessDifferentDays() {
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 18, 0, 20, 30)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckConsecutiveDays(true));
        assertEquals(0, rosteredShifts.get(0).getCompliance().getIndexOfDay().intValue());
        assertEquals(1, rosteredShifts.get(1).getCompliance().getIndexOfDay().intValue());
        assertEquals(2, rosteredShifts.get(2).getCompliance().getIndexOfDay().intValue());
        assertEquals(2, rosteredShifts.get(3).getCompliance().getIndexOfDay().intValue());
        assertEquals(3, rosteredShifts.get(4).getCompliance().getIndexOfDay().intValue());
    }

    @Test
    public void resetCounterIfNightShift() {
        assertTrue(shiftSpecs.remove(new ShiftSpec(2, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 22, 0, 6, 30)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckConsecutiveDays(true));
        assertEquals(0, rosteredShifts.get(0).getCompliance().getIndexOfDay().intValue());
        assertEquals(1, rosteredShifts.get(1).getCompliance().getIndexOfDay().intValue());
        assertNotNull(rosteredShifts.get(2).getCompliance().getIndexOfNightShift());
        assertNull(rosteredShifts.get(2).getCompliance().getIndexOfDay());
        assertEquals(0, rosteredShifts.get(3).getCompliance().getIndexOfDay().intValue());
        assertEquals(1, rosteredShifts.get(4).getCompliance().getIndexOfDay().intValue());
        assertEquals(2, rosteredShifts.get(5).getCompliance().getIndexOfDay().intValue());
    }
}