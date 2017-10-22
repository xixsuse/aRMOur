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
            assertNull(compliance.getRecoveryInformation());
            assertNull(compliance.sufficientRecoveryFollowingNights());
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
    public void consecutiveNights() {
        final Compliance.Configuration baseConfig = NONE_COMPLIANT.withCheckConsecutiveNightsWorked(true);
        List<RosteredShift> rosteredShifts;

        assertTrue(shiftSpecs.add(new ShiftSpec(5, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(7, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig);
        assertTrue(rosteredShifts.get(6).getCompliance().compliesWithMaximumConsecutiveNightsWorked());
        assertFalse(rosteredShifts.get(7).getCompliance().compliesWithMaximumConsecutiveNightsWorked());

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(true, false)));
        assertTrue(rosteredShifts.get(4).getCompliance().compliesWithMaximumConsecutiveNightsWorked());
        assertFalse(rosteredShifts.get(5).getCompliance().compliesWithMaximumConsecutiveNightsWorked());

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(false, false)));
        assertTrue(rosteredShifts.get(3).getCompliance().compliesWithMaximumConsecutiveNightsWorked());
        assertFalse(rosteredShifts.get(4).getCompliance().compliesWithMaximumConsecutiveNightsWorked());

    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void oneRecoveryDayFollowingNights() {
        final Compliance.Configuration baseConfig = NONE_COMPLIANT.withCheckRecoveryDaysFollowingNights(true);
        List<RosteredShift> rosteredShifts;
        Compliance compliance;
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(5, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertFalse(compliance.sufficientRecoveryFollowingNights());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(0, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(4, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertNull(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(true, true)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(4, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertFalse(compliance.sufficientRecoveryFollowingNights());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(1, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(true, true)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(3, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(true, false)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(3, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertFalse(compliance.sufficientRecoveryFollowingNights());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(2, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(true, false)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(2, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(3, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(true, false)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(1, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertNull(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

//        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(1, 5)));
//        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
//        assertNull(compliance.getIndexOfNightShift());
//        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
//        assertTrue(compliance.sufficientRecoveryFollowingNights());
//        assertTrue(compliance.isCompliant());
//
//        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(2, 6)));
//        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
//        assertNull(compliance.getIndexOfNightShift());
//        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
//        assertNull(compliance.sufficientRecoveryFollowingNights());
//        assertTrue(compliance.isCompliant());
//
//        assertTrue(shiftSpecs.remove(new ShiftSpec(6, 8, 0, 16, 0)));
//        assertTrue(shiftSpecs.add(new ShiftSpec(7, 8, 0, 16, 0)));
//        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(2, 5)));
//        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
//        assertNull(compliance.getIndexOfNightShift());
//        assertEquals(2, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
//        assertTrue(compliance.sufficientRecoveryFollowingNights());
//        assertTrue(compliance.isCompliant());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void twoRecoveryDaysFollowingNights() {
        final Compliance.Configuration baseConfig = NONE_COMPLIANT.withCheckRecoveryDaysFollowingNights(true);
        List<RosteredShift> rosteredShifts;
        Compliance compliance;
        assertTrue(shiftSpecs.add(new ShiftSpec(7, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(5, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(2, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(0, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(4, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(2, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertNull(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(true, false)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(4, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(2, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(1, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(true, false)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(3, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(2, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(2, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(true, false)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(2, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(2, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(3, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(true, false)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(1, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(2, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertNull(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void adequateRecoveryOnlyAppliesForConsecutiveNights() {
        assertTrue(shiftSpecs.remove(new ShiftSpec(0, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(-1, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 8, 0, 16, 0)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT.withCheckRecoveryDaysFollowingNights(true));
        Compliance compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(4, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertNull(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());
    }

}