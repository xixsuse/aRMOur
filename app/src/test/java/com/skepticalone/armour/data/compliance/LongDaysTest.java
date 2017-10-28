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
public class LongDaysTest extends RosteredShiftTest {

    @Before
    public void setUp() {
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

    @Test
    public void defineLongDays() {
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckLongDaysPerWeek(true));
        for (int i = 0; i < rosteredShifts.size(); i++) {
            Compliance compliance = rosteredShifts.get(i).getCompliance();
            assertNull(compliance.getNight());
            if (i == 2 || i == 4) {
                assertNotNull(compliance.getLongDay());
                assertTrue(compliance.getLongDay().isCompliant());
            } else {
                assertNull(compliance.getLongDay());
            }
            assertTrue(compliance.isCompliant());
        }
    }

    @Test
    public void checkMaximumLongDaysPerWeek() {
        assertTrue(shiftSpecs.remove(new ShiftSpec(9, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.remove(new ShiftSpec(10, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.remove(new ShiftSpec(11, 8, 0, 16, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(8, 8, 0, 22, 30)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckLongDaysPerWeek(true));
        Compliance compliance = getRosteredShifts(NONE_COMPLIANT.withCheckLongDaysPerWeek(true)).get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(2, compliance.getLongDay().getIndexOfLongDay());
        assertFalse(compliance.getLongDay().isCompliant());
        assertTrue(shiftSpecs.remove(new ShiftSpec(8, 8, 0, 22, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(9, 8, 0, 22, 30)));
        rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckLongDaysPerWeek(true));
        compliance = getRosteredShifts(NONE_COMPLIANT.withCheckLongDaysPerWeek(true)).get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(1, compliance.getLongDay().getIndexOfLongDay());
        assertTrue(compliance.getLongDay().isCompliant());
        assertTrue(shiftSpecs.remove(new ShiftSpec(9, 8, 0, 22, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(11, 8, 0, 22, 30)));
        rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckLongDaysPerWeek(true));
        compliance = getRosteredShifts(NONE_COMPLIANT.withCheckLongDaysPerWeek(true)).get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(0, compliance.getLongDay().getIndexOfLongDay());
        assertTrue(compliance.getLongDay().isCompliant());
    }

    @Test
    public void doNotIncrementOrResetCounterIfNightShift() {
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 23, 0, 6, 30)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckConsecutiveDays(true));
        assertNull(rosteredShifts.get(0).getCompliance().getLongDay());
        assertNull(rosteredShifts.get(1).getCompliance().getLongDay());
        assertEquals(0, rosteredShifts.get(2).getCompliance().getLongDay().getIndexOfLongDay());
        assertNotNull(rosteredShifts.get(3).getCompliance().getNight().getIndexOfNightShift());
        assertNull(rosteredShifts.get(3).getCompliance().getLongDay());
        assertNull(rosteredShifts.get(4).getCompliance().getLongDay());
        assertEquals(1, rosteredShifts.get(5).getCompliance().getLongDay().getIndexOfLongDay());
    }
}