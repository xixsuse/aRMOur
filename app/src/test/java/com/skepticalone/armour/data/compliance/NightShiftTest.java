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
        assertNull(new ShiftSpec(0, 8, 0, 16, 0).toTestShift(NONE_COMPLIANT, null).getCompliance().getNight());
        assertNotNull(new ShiftSpec(0, 22, 30, 8, 0).toTestShift(NONE_COMPLIANT, null).getCompliance().getNight());
        assertNotNull(new ShiftSpec(0, 1, 0, 10, 0).toTestShift(NONE_COMPLIANT, null).getCompliance().getNight());
        assertNotNull(new ShiftSpec(0, 6, 0, 0, 0).toTestShift(NONE_COMPLIANT, null).getCompliance().getNight());
    }

    @Test
    public void defineNights() {
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT);
        for (int i = 0; i < rosteredShifts.size(); i++) {
            Compliance compliance = rosteredShifts.get(i).getCompliance();
            assertNotNull(compliance.getNight());
            assertEquals(i, compliance.getNight().getIndexOfNightShift());
            assertTrue(compliance.getNight().isCompliant());
        }
    }

    @Test
    public void doNotIncrementIndexUnlessNightsDistinct() {
        assertTrue(shiftSpecs.remove(new ShiftSpec(4, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 22, 0, 2, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 4, 0, 8, 0)));
        List<RosteredShift> rosteredShifts = getRosteredShifts(NONE_COMPLIANT);
        assertEquals(7, rosteredShifts.get(7).getCompliance().getNight().getIndexOfNightShift());
        assertEquals(7, rosteredShifts.get(8).getCompliance().getNight().getIndexOfNightShift());
    }

    @Test
    public void consecutiveNights() {
        final MockConfiguration baseConfig = NONE_COMPLIANT.withCheckConsecutiveNightsWorked(true);
        List<RosteredShift> rosteredShifts;

        rosteredShifts = getRosteredShifts(baseConfig);
        assertTrue(rosteredShifts.get(6).getCompliance().getNight().isCompliant());
        assertFalse(rosteredShifts.get(7).getCompliance().getNight().isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.toSaferRosters().withAllow5ConsecutiveNights(true));
        assertTrue(rosteredShifts.get(4).getCompliance().getNight().isCompliant());
        assertFalse(rosteredShifts.get(5).getCompliance().getNight().isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.toSaferRosters().withAllow5ConsecutiveNights(false));
        assertTrue(rosteredShifts.get(3).getCompliance().getNight().isCompliant());
        assertFalse(rosteredShifts.get(4).getCompliance().getNight().isCompliant());

    }

    @Test
    public void recoveryDaysFollowingNights() {
        final MockConfiguration baseConfig = NONE_COMPLIANT.withCheckRecoveryFollowingNights(true);
        List<RosteredShift> rosteredShifts;
        Compliance compliance;
        assertTrue(shiftSpecs.remove(new ShiftSpec(4, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig);
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getNight());
        assertEquals(7, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(0, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertFalse(compliance.getRecoveryFollowingNights().isCompliant());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(4, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig);
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getNight());
        assertEquals(7, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(0, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertFalse(compliance.getRecoveryFollowingNights().isCompliant());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(5, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig);
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getNight());
        assertEquals(7, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(1, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertFalse(compliance.getRecoveryFollowingNights().isCompliant());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(6, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(7, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig);
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getNight());
        assertEquals(7, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(2, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertTrue(compliance.getRecoveryFollowingNights().isCompliant());
        assertTrue(compliance.isCompliant());

    }

    @Test
    public void sufficientRecoveryOnlyAppliesForConsecutiveNights() {

        final MockConfiguration baseConfig = NONE_COMPLIANT.withCheckRecoveryFollowingNights(true);
        List<RosteredShift> rosteredShifts;
        Compliance compliance;
        assertTrue(shiftSpecs.remove(new ShiftSpec(0, 22, 0, 8, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig);
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertNull(compliance.getNight());
        assertEquals(4, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(0, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertTrue(compliance.getRecoveryFollowingNights().isCompliant());
        assertTrue(compliance.isCompliant());

    }

    @Test
    public void testMinimumNightsForRecovery() {

        final MockConfiguration baseConfig = NONE_COMPLIANT.withCheckRecoveryFollowingNights(true);
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

        rosteredShifts = getRosteredShifts(baseConfig);
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(1, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(0, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertTrue(compliance.getRecoveryFollowingNights().isCompliant());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.toSaferRosters());
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(1, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(0, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertTrue(compliance.getRecoveryFollowingNights().isCompliant());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.add(new ShiftSpec(3, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig);
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(2, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(0, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertTrue(compliance.getRecoveryFollowingNights().isCompliant());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.toSaferRosters());
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(2, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(0, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertFalse(compliance.getRecoveryFollowingNights().isCompliant());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(5, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig);
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(2, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(0, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertTrue(compliance.getRecoveryFollowingNights().isCompliant());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.toSaferRosters());
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(2, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(0, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertFalse(compliance.getRecoveryFollowingNights().isCompliant());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(6, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(7, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig);
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(2, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(1, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertTrue(compliance.getRecoveryFollowingNights().isCompliant());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.toSaferRosters());
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(2, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(1, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertTrue(compliance.getRecoveryFollowingNights().isCompliant());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.add(new ShiftSpec(2, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig);
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(3, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(1, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertTrue(compliance.getRecoveryFollowingNights().isCompliant());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.toSaferRosters());
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(3, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(1, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertFalse(compliance.getRecoveryFollowingNights().isCompliant());
        assertFalse(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.toSaferRosters().withAllowOnly1RecoveryDayFollowing3Nights(true));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(3, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(1, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertTrue(compliance.getRecoveryFollowingNights().isCompliant());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.add(new ShiftSpec(1, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig);
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(4, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(1, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertTrue(compliance.getRecoveryFollowingNights().isCompliant());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.toSaferRosters().withAllowOnly1RecoveryDayFollowing3Nights(true));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(4, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(1, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertFalse(compliance.getRecoveryFollowingNights().isCompliant());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.add(new ShiftSpec(0, 22, 0, 8, 0)));

        rosteredShifts = getRosteredShifts(baseConfig);
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(5, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(1, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertFalse(compliance.getRecoveryFollowingNights().isCompliant());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(7, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(8, 8, 0, 16, 0)));

        rosteredShifts = getRosteredShifts(baseConfig);
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(5, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(2, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertTrue(compliance.getRecoveryFollowingNights().isCompliant());
        assertTrue(compliance.isCompliant());

        rosteredShifts = getRosteredShifts(baseConfig.toSaferRosters().withAllowOnly1RecoveryDayFollowing3Nights(true));
        compliance = rosteredShifts.get(rosteredShifts.size() - 1).getCompliance();
        assertEquals(5, compliance.getRecoveryFollowingNights().getConsecutiveNights());
        assertEquals(2, compliance.getRecoveryFollowingNights().getRecoveryDays());
        assertTrue(compliance.getRecoveryFollowingNights().isCompliant());
        assertTrue(compliance.isCompliant());

    }

}