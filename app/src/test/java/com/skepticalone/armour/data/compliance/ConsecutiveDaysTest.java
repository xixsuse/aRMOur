package com.skepticalone.armour.data.compliance;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.ShiftSpec;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class ConsecutiveDaysTest extends RosteredShiftTest {

    @Before
    public void setUp() {
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
            assertNull(compliance.getNight());
            assertEquals(i, compliance.getConsecutiveDays().getIndexOfDayShift());
            assertTrue(compliance.getConsecutiveDays().isCompliant());
            assertTrue(compliance.isCompliant());
        }
        assertTrue(shiftSpecs.add(new ShiftSpec(12, 8, 0, 16, 30)));
        rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckConsecutiveDays(true));
        Compliance compliance = rosteredShifts.get(12).getCompliance();
        assertNull(compliance.getNight());
        assertEquals(12, compliance.getConsecutiveDays().getIndexOfDayShift());
        assertFalse(compliance.getConsecutiveDays().isCompliant());
        assertFalse(compliance.isCompliant());
    }

    @Test
    public void resetCounterAfterSkippedDays() {
        assertTrue(shiftSpecs.remove(new ShiftSpec(2, 8, 0, 16, 30)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckConsecutiveDays(true));
        assertEquals(0, rosteredShifts.get(0).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertEquals(1, rosteredShifts.get(1).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertEquals(0, rosteredShifts.get(2).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertEquals(1, rosteredShifts.get(3).getCompliance().getConsecutiveDays().getIndexOfDayShift());
    }

    @Test
    public void incrementCounterIfLongDay() {
        assertTrue(shiftSpecs.remove(new ShiftSpec(2, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 8, 0, 22, 30)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckConsecutiveDays(true));
        assertEquals(0, rosteredShifts.get(0).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertEquals(1, rosteredShifts.get(1).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertNull(rosteredShifts.get(1).getCompliance().getLongDay());
        assertEquals(2, rosteredShifts.get(2).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertNotNull(rosteredShifts.get(2).getCompliance().getLongDay());
        assertEquals(3, rosteredShifts.get(3).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertNull(rosteredShifts.get(3).getCompliance().getLongDay());
        assertEquals(4, rosteredShifts.get(4).getCompliance().getConsecutiveDays().getIndexOfDayShift());
    }

    @Test
    public void doNotIncrementCounterUnlessDifferentDays() {
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 18, 0, 20, 30)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckConsecutiveDays(true));
        assertEquals(0, rosteredShifts.get(0).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertEquals(1, rosteredShifts.get(1).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertEquals(2, rosteredShifts.get(2).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertEquals(2, rosteredShifts.get(3).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertEquals(3, rosteredShifts.get(4).getCompliance().getConsecutiveDays().getIndexOfDayShift());
    }

    @Test
    public void resetCounterIfNightShift() {
        assertTrue(shiftSpecs.remove(new ShiftSpec(2, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 22, 0, 6, 30)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckConsecutiveDays(true));
        assertEquals(0, rosteredShifts.get(0).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertEquals(1, rosteredShifts.get(1).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertNotNull(rosteredShifts.get(2).getCompliance().getNight());
        assertNull(rosteredShifts.get(2).getCompliance().getConsecutiveDays());
        assertEquals(0, rosteredShifts.get(3).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertEquals(1, rosteredShifts.get(4).getCompliance().getConsecutiveDays().getIndexOfDayShift());
        assertEquals(2, rosteredShifts.get(5).getCompliance().getConsecutiveDays().getIndexOfDayShift());
    }
}