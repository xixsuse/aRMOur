package com.skepticalone.armour.data.model;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class RosteredShiftTest {

    @NonNull
    final static Compliance.Configuration ALL_COMPLIANT = new Compliance.Configuration(
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            null
    );
    @NonNull
    final static Compliance.Configuration NONE_COMPLIANT = new Compliance.Configuration(
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            null
    );
    @NonNull
    final SortedSet<ShiftSpec> shiftSpecs = new TreeSet<>();

    @CallSuper
    @Before
    public void prepareShiftSpecs() {
        shiftSpecs.clear();
    }

    @Test
    public final void checkBaselineCompliant() {
        expectCompliant(getRosteredShifts(ALL_COMPLIANT));
    }

    @NonNull
    final List<RosteredShift> getRosteredShifts(@NonNull Compliance.Configuration complianceConfig) {
        List<RosteredShift> shifts = new ArrayList<>();
        for (ShiftSpec spec : shiftSpecs) {
            shifts.add(spec.toTestShift(
                    complianceConfig,
                    shifts
            ));
        }
        return shifts;
    }

    @SuppressWarnings("ConstantConditions")
    final void expectCompliant(@NonNull List<RosteredShift> shifts) {
        for (RosteredShift shift : shifts) {
            assertTrue("compliesWithMaximumDurationOverDay: " + shift.getShiftData().toString(), shift.getCompliance().compliesWithMaximumDurationOverDay() == null || shift.getCompliance().compliesWithMaximumDurationOverDay());
            assertTrue("compliesWithMaximumDurationOverWeek: " + shift.getShiftData().toString(), shift.getCompliance().compliesWithMaximumDurationOverWeek() == null || shift.getCompliance().compliesWithMaximumDurationOverWeek());
            assertTrue("compliesWithMaximumDurationOverFortnight: " + shift.getShiftData().toString(), shift.getCompliance().compliesWithMaximumDurationOverFortnight() == null || shift.getCompliance().compliesWithMaximumDurationOverFortnight());
            assertTrue("sufficientDurationBetweenShifts: " + shift.getShiftData().toString(), shift.getCompliance().sufficientDurationBetweenShifts() == null || shift.getCompliance().sufficientDurationBetweenShifts());
            assertTrue("previousWeekendFree: " + shift.getShiftData().toString(), shift.getCompliance().previousWeekendFree() == null || shift.getCompliance().previousWeekendFree());
            assertTrue("compliesWithMaximumLongDaysPerWeek: " + shift.getShiftData().toString(), shift.getCompliance().compliesWithMaximumLongDaysPerWeek() == null || shift.getCompliance().compliesWithMaximumLongDaysPerWeek());
            assertTrue("consecutiveNightsWorked: " + shift.getShiftData().toString(), shift.getCompliance().compliesWithMaximumConsecutiveNightsWorked() == null || shift.getCompliance().compliesWithMaximumConsecutiveNightsWorked());
            assertTrue("recoveryDaysFollowingNights: " + shift.getShiftData().toString(), shift.getCompliance().sufficientRecoveryFollowingNights() == null || shift.getCompliance().sufficientRecoveryFollowingNights());
            assertTrue("isCompliant: " + shift.getShiftData().toString(), shift.getCompliance().isCompliant());
        }
    }

    @SuppressWarnings("ConstantConditions")
    final void expectNonCompliant(@NonNull Compliance.Configuration configuration, int indexOfNonCompliantShift) {
        expectCompliant(getRosteredShifts(configuration));
        List<RosteredShift> shifts = getRosteredShifts(ALL_COMPLIANT);
        Compliance compliance = shifts.get(indexOfNonCompliantShift).getCompliance();
        assertEquals(configuration.checkDurationOverDay, compliance.compliesWithMaximumDurationOverDay() == null || compliance.compliesWithMaximumDurationOverDay());
        assertEquals(configuration.checkDurationOverWeek, compliance.compliesWithMaximumDurationOverWeek() == null || compliance.compliesWithMaximumDurationOverWeek());
        assertEquals(configuration.checkDurationOverFortnight, compliance.compliesWithMaximumDurationOverFortnight() == null || compliance.compliesWithMaximumDurationOverFortnight());
        assertEquals(configuration.checkDurationBetweenShifts, compliance.sufficientDurationBetweenShifts() == null || compliance.sufficientDurationBetweenShifts());
        assertEquals(configuration.checkConsecutiveWeekends, compliance.previousWeekendFree() == null || compliance.previousWeekendFree());
        assertEquals(configuration.checkLongDaysPerWeek, compliance.compliesWithMaximumLongDaysPerWeek() == null || compliance.compliesWithMaximumLongDaysPerWeek());
        assertEquals(configuration.checkConsecutiveNightsWorked, compliance.compliesWithMaximumConsecutiveNightsWorked() == null || compliance.compliesWithMaximumConsecutiveNightsWorked());
        assertEquals(configuration.checkRecoveryDaysFollowingNights, compliance.sufficientRecoveryFollowingNights() == null || compliance.sufficientRecoveryFollowingNights());
        assertFalse(compliance.isCompliant());
    }

}