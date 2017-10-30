package com.skepticalone.armour.data.compliance;

import com.skepticalone.armour.data.model.ShiftSpec;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class ConfigurationTest extends RosteredShiftTest {

    @Test
    public void insufficientDurationBetweenShifts() {
        MockConfiguration mockConfiguration = NONE_COMPLIANT.withCheckDurationBetweenShifts(true);
        assertTrue(shiftSpecs.add(new ShiftSpec(0, 16, 0, 0, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(1, 8, 0, 16, 0)));
        Compliance compliance = getRosteredShifts(mockConfiguration).get(1).getCompliance();
        assertTrue(compliance.getDurationBetweenShifts().isCompliant());
        assertTrue(compliance.isCompliant());
        assertTrue(shiftSpecs.remove(new ShiftSpec(1, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(1, 7, 59, 16, 0)));
        compliance = getRosteredShifts(mockConfiguration).get(1).getCompliance();
        assertFalse(compliance.getDurationBetweenShifts().isCompliant());
        assertFalse(compliance.isCompliant());
    }

    @Test
    public void exceedsMaximumDurationOverDay() {
        MockConfiguration mockConfiguration = NONE_COMPLIANT.withCheckDurationOverDay(true);
        assertTrue(shiftSpecs.add(new ShiftSpec(0, 6, 0, 22, 0)));
        Compliance compliance = getRosteredShifts(mockConfiguration).get(0).getCompliance();
        assertTrue(compliance.getDurationOverDay().isCompliant());
        assertTrue(compliance.isCompliant());
        assertTrue(shiftSpecs.remove(new ShiftSpec(0, 6, 0, 22, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(0, 6, 0, 22, 1)));
        compliance = getRosteredShifts(mockConfiguration).get(0).getCompliance();
        assertFalse(compliance.getDurationOverDay().isCompliant());
        assertFalse(compliance.isCompliant());
    }

    @Test
    public void exceedsMaximumDurationOverWeek() {
        MockConfiguration mockConfiguration = NONE_COMPLIANT.withCheckDurationOverWeek(true);
        assertTrue(shiftSpecs.add(new ShiftSpec(0, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(1, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(3, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 6, 0, 18, 0)));

        Compliance compliance = getRosteredShifts(mockConfiguration).get(6).getCompliance();
        assertTrue(compliance.getDurationOverWeek().isCompliant());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(6, 6, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 5, 59, 18, 0)));

        compliance = getRosteredShifts(mockConfiguration).get(6).getCompliance();
        assertFalse(compliance.getDurationOverWeek().isCompliant());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(6, 5, 59, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 6, 0, 18, 0)));

        compliance = getRosteredShifts(mockConfiguration).get(6).getCompliance();
        assertTrue(compliance.getDurationOverWeek().isCompliant());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.add(new ShiftSpec(-1, 8, 0, 18, 0)));

        compliance = getRosteredShifts(mockConfiguration).get(7).getCompliance();
        assertTrue(compliance.getDurationOverWeek().isCompliant());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(-1, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(-1, 8, 0, 18, 1)));

        compliance = getRosteredShifts(mockConfiguration).get(7).getCompliance();
        assertFalse(compliance.getDurationOverWeek().isCompliant());
        assertFalse(compliance.isCompliant());

    }

    @Test
    public void exceedsMaximumDurationOverFortnight() {
        MockConfiguration mockConfiguration = NONE_COMPLIANT.withCheckDurationOverFortnight(true);

        assertTrue(shiftSpecs.add(new ShiftSpec(0, 8, 0, 19, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(1, 8, 0, 20, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 8, 0, 19, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(3, 8, 0, 20, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 8, 0, 19, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 8, 0, 22, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 8, 0, 22, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(7, 8, 0, 20, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(8, 8, 0, 19, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(9, 8, 0, 20, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(10, 8, 0, 19, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(13, 7, 0, 19, 1)));

        Compliance compliance = getRosteredShifts(mockConfiguration).get(11).getCompliance();
        assertFalse(compliance.getDurationOverFortnight().isCompliant());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(13, 7, 0, 19, 1)));
        assertTrue(shiftSpecs.add(new ShiftSpec(13, 7, 0, 19, 0)));

        compliance = getRosteredShifts(mockConfiguration).get(11).getCompliance();
        assertTrue(compliance.getDurationOverFortnight().isCompliant());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.add(new ShiftSpec(-1, 8, 0, 19, 0)));

        compliance = getRosteredShifts(mockConfiguration).get(12).getCompliance();
        assertTrue(compliance.getDurationOverFortnight().isCompliant());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(-1, 8, 0, 19, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(-1, 8, 1, 19, 1)));

        compliance = getRosteredShifts(mockConfiguration).get(12).getCompliance();
        assertFalse(compliance.getDurationOverFortnight().isCompliant());
        assertFalse(compliance.isCompliant());
    }

    @Test
    public void consecutiveWeekendsWorked() {

        MockConfiguration mockConfiguration = NONE_COMPLIANT.withCheckFrequencyOfWeekends(true);

        assertTrue(shiftSpecs.add(new ShiftSpec(-1, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 8, 0, 0, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(12, 8, 0, 16, 0)));

        Compliance compliance = getRosteredShifts(mockConfiguration).get(0).getCompliance();
        assertNotNull(compliance.getWeekend());

        compliance = getRosteredShifts(mockConfiguration).get(1).getCompliance();
        assertNull(compliance.getWeekend());

        compliance = getRosteredShifts(mockConfiguration).get(2).getCompliance();
        assertEquals(0, compliance.getWeekend().getMaximumConsecutiveWeekendsInPeriod());
        assertEquals(1, compliance.getWeekend().getConsecutiveWeekendsInPeriod());
        assertTrue(compliance.getWeekend().isCompliant());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(4, 8, 0, 0, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 0, 0, 0, 1)));

        compliance = getRosteredShifts(mockConfiguration).get(2).getCompliance();
        assertEquals(1, compliance.getWeekend().getMaximumConsecutiveWeekendsInPeriod());
        assertEquals(2, compliance.getWeekend().getConsecutiveWeekendsInPeriod());
        assertFalse(compliance.getWeekend().isCompliant());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(5, 0, 0, 0, 1)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 23, 59, 0, 0)));

        compliance = getRosteredShifts(mockConfiguration).get(2).getCompliance();
        assertFalse(compliance.getWeekend().isCompliant());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(6, 23, 59, 0, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(7, 0, 0, 0, 1)));

        compliance = getRosteredShifts(mockConfiguration).get(2).getCompliance();
        assertTrue(compliance.getWeekend().isCompliant());
        assertTrue(compliance.isCompliant());

    }

}