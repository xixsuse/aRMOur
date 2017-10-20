package com.skepticalone.armour.data.model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NightShiftTest extends RosteredShiftTest {

    @Override
    public void prepareShiftSpecs() {
        super.prepareShiftSpecs();
        assertTrue(shiftSpecs.add(new ShiftSpec(0, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(1, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(3, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 22, 0, 8, 0)));
    }

    @Test
    public void calculateIsNightShift() {
        assertNull(new ShiftSpec(8, 0, 16, 0).toSingleTestShift(ALL_COMPLIANT).getCompliance().getIndexOfNightShift());
        assertNotNull(new ShiftSpec(22, 30, 8, 0).toSingleTestShift(ALL_COMPLIANT).getCompliance().getIndexOfNightShift());
        assertNotNull(new ShiftSpec(1, 0, 10, 0).toSingleTestShift(ALL_COMPLIANT).getCompliance().getIndexOfNightShift());
        assertNotNull(new ShiftSpec(6, 0, 0, 0).toSingleTestShift(ALL_COMPLIANT).getCompliance().getIndexOfNightShift());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void defineNights() {
        List<RosteredShift> rosteredShifts = getRosteredShifts(ALL_COMPLIANT);
        for (int i = 0; i < rosteredShifts.size(); i++) {
            Compliance compliance = rosteredShifts.get(i).getCompliance();
            assertEquals(i, compliance.getIndexOfNightShift().intValue());
            assertNull(compliance.getRecoveryDaysFollowingNights());
            assertNull(compliance.adequateRecovery());
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void doNotIncrementIndexUnlessNightsDistinct() {
        assertTrue(shiftSpecs.remove(new ShiftSpec(4, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 22, 0, 2, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 4, 0, 8, 0)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT);
        assertEquals(4, rosteredShifts.get(4).getCompliance().getIndexOfNightShift().intValue());
        assertEquals(4, rosteredShifts.get(5).getCompliance().getIndexOfNightShift().intValue());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void adequateRecovery() {
        List<RosteredShift> rosteredShifts;
        Compliance compliance;
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(ALL_COMPLIANT.withCheckAdequateRecovery(new Compliance.Configuration.AdequateRecoveryDefinition(2, 5)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(1, compliance.getRecoveryDaysFollowingNights().intValue());
        assertFalse(compliance.adequateRecovery());

        rosteredShifts = getRosteredShifts(ALL_COMPLIANT.withCheckAdequateRecovery(new Compliance.Configuration.AdequateRecoveryDefinition(1, 5)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(1, compliance.getRecoveryDaysFollowingNights().intValue());
        assertTrue(compliance.adequateRecovery());

        rosteredShifts = getRosteredShifts(ALL_COMPLIANT.withCheckAdequateRecovery(new Compliance.Configuration.AdequateRecoveryDefinition(2, 6)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(1, compliance.getRecoveryDaysFollowingNights().intValue());
        assertNull(compliance.adequateRecovery());

        assertTrue(shiftSpecs.remove(new ShiftSpec(6, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(7, 8, 0, 16, 0)));
        rosteredShifts = getRosteredShifts(ALL_COMPLIANT.withCheckAdequateRecovery(new Compliance.Configuration.AdequateRecoveryDefinition(2, 5)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(2, compliance.getRecoveryDaysFollowingNights().intValue());
        assertTrue(compliance.adequateRecovery());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void adequateRecoveryOnlyAppliesForConsecutiveNights() {
        assertTrue(shiftSpecs.remove(new ShiftSpec(0, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(-1, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 8, 0, 16, 0)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckAdequateRecovery(Compliance.Configuration.AdequateRecoveryDefinition.DEFAULT));
        Compliance compliance = rosteredShifts.get(4).getCompliance();
        assertEquals(3, compliance.getIndexOfNightShift().intValue());
        assertNull(compliance.getRecoveryDaysFollowingNights());
        assertNull(compliance.adequateRecovery());
        compliance = rosteredShifts.get(5).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(1, compliance.getRecoveryDaysFollowingNights().intValue());
        assertNull(compliance.adequateRecovery());
    }

}