package com.skepticalone.armour.data.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class NightShiftTest extends RosteredShiftTest {

    @Before
    public void setUp() {
        assertTrue(shiftSpecs.add(new ShiftSpec(-3, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(-2, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(-1, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(0, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(1, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(3, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 22, 0, 8, 0)));
    }

    @Test
    public void calculateIsNightShift() {
        assertNull(new ShiftSpec(0, 8, 0, 16, 0).toTestShift(NONE_COMPLIANT, null).getCompliance().getIndexOfNightShift());
        assertNotNull(new ShiftSpec(0, 22, 30, 8, 0).toTestShift(NONE_COMPLIANT, null).getCompliance().getIndexOfNightShift());
        assertNotNull(new ShiftSpec(0, 1, 0, 10, 0).toTestShift(NONE_COMPLIANT, null).getCompliance().getIndexOfNightShift());
        assertNotNull(new ShiftSpec(0, 6, 0, 0, 0).toTestShift(NONE_COMPLIANT, null).getCompliance().getIndexOfNightShift());
    }

    @Test
    public void defineNights() {
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT);
        for (int i = 0; i < rosteredShifts.size(); i++) {
            Compliance compliance = rosteredShifts.get(i).getCompliance();
            assertEquals(i, compliance.getIndexOfNightShift().intValue());
            assertNull(compliance.getRecoveryInformation());
            assertNull(compliance.sufficientRecoveryFollowingNights());
        }
    }

    @Test
    public void doNotIncrementIndexUnlessNightsDistinct() {
        assertTrue(shiftSpecs.remove(new ShiftSpec(4, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 22, 0, 2, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 4, 0, 8, 0)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT);
        assertEquals(7, rosteredShifts.get(7).getCompliance().getIndexOfNightShift().intValue());
        assertEquals(7, rosteredShifts.get(8).getCompliance().getIndexOfNightShift().intValue());
    }

    @Test
    public void consecutiveNights() {
        final Compliance.Configuration baseConfig = NONE_COMPLIANT.withCheckConsecutiveNightsWorked(true);
        List<RosteredShift> rosteredShifts;

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

    @Test
    public void recoveryDaysFollowingNights() {
        final Compliance.Configuration baseConfig = NONE_COMPLIANT.withCheckRecoveryDaysFollowingNights(true);
        List<RosteredShift> rosteredShifts;
        Compliance compliance;
        assertTrue(shiftSpecs.remove(new ShiftSpec(4, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(7, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(0, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertFalse(compliance.sufficientRecoveryFollowingNights());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(4, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(7, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(0, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertFalse(compliance.sufficientRecoveryFollowingNights());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(5, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(7, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertFalse(compliance.sufficientRecoveryFollowingNights());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(6, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(7, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(7, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(2, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

    }

    @Test
    public void sufficientRecoveryOnlyAppliesForConsecutiveNights() {

        final Compliance.Configuration baseConfig = NONE_COMPLIANT.withCheckRecoveryDaysFollowingNights(true);
        List<RosteredShift> rosteredShifts;
        Compliance compliance;
        assertTrue(shiftSpecs.remove(new ShiftSpec(0, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getIndexOfNightShift());
        assertEquals(4, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(0, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

    }

    @Test
    public void testMinimumNightsForRecovery() {

        final Compliance.Configuration baseConfig = NONE_COMPLIANT.withCheckRecoveryDaysFollowingNights(true);
        List<RosteredShift> rosteredShifts;
        Compliance compliance;

        assertTrue(shiftSpecs.remove(new ShiftSpec(-3, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.remove(new ShiftSpec(-2, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.remove(new ShiftSpec(-1, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.remove(new ShiftSpec(0, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.remove(new ShiftSpec(1, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.remove(new ShiftSpec(2, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.remove(new ShiftSpec(3, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(1, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(0, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(false, false)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(1, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(0, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.add(new ShiftSpec(3, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(2, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(0, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(false, false)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(2, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(0, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertFalse(compliance.sufficientRecoveryFollowingNights());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(5, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(2, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(0, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(false, false)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(2, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(0, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertFalse(compliance.sufficientRecoveryFollowingNights());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(6, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(7, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(2, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(false, false)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(2, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.add(new ShiftSpec(2, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(3, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(false, false)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(3, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertFalse(compliance.sufficientRecoveryFollowingNights());
        assertFalse(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(false, true)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(3, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.add(new ShiftSpec(1, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(4, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(false, true)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(4, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertFalse(compliance.sufficientRecoveryFollowingNights());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.add(new ShiftSpec(0, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(5, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(1, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertFalse(compliance.sufficientRecoveryFollowingNights());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(7, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(8, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(null));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(5, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(2, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.withSaferRostersOptions(new Compliance.Configuration.SaferRostersOptions(false, true)));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(5, compliance.getRecoveryInformation().previousConsecutiveNightShifts);
        assertEquals(2, compliance.getRecoveryInformation().recoveryDaysFollowingNights);
        assertTrue(compliance.sufficientRecoveryFollowingNights());
        assertTrue(compliance.isCompliant());

    }

}