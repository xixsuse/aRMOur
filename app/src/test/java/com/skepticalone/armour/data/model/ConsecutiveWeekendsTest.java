package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class ConsecutiveWeekendsTest extends RosteredShiftTest {

    @NonNull
    private static ShiftSpec saturday(int week) {
        return new ShiftSpec(week * 7 + 5, 8, 0, 16, 0);
    }

    @Test
    public void defineWeekend() {
        assertNotNull(new ShiftSpec(-1, 23, 59, 0, 0).toTestShift(NONE_COMPLIANT, null).getCompliance().getConsecutiveWeekends());
        assertNull(new ShiftSpec(0, 0, 0, 0, 1).toTestShift(NONE_COMPLIANT, null).getCompliance().getConsecutiveWeekends());
        assertNull(new ShiftSpec(4, 23, 59, 0, 0).toTestShift(NONE_COMPLIANT, null).getCompliance().getConsecutiveWeekends());
        assertNotNull(new ShiftSpec(5, 0, 0, 0, 1).toTestShift(NONE_COMPLIANT, null).getCompliance().getConsecutiveWeekends());
    }

    private void consecutiveWeekends(@NonNull ComplianceConfiguration complianceConfiguration, int expectedPeriodInWeeks) {
        List<RosteredShift> shifts = getRosteredShifts(complianceConfiguration);
        boolean saferRosters = complianceConfiguration instanceof MockComplianceConfigurationSaferRosters;
        for (int i = 0; i < shifts.size(); i++) {
            RosteredShift shift = shifts.get(i);
            assertEquals(Integer.toString(i), saferRosters, shift.getCompliance().getConsecutiveWeekends().saferRosters());
            assertEquals(Integer.toString(i), expectedPeriodInWeeks, shift.getCompliance().getConsecutiveWeekends().getPeriodInWeeks());
            assertEquals(Integer.toString(i), Math.min(i + 1, expectedPeriodInWeeks), shift.getCompliance().getConsecutiveWeekends().getWeekendsWorkedInPeriod().size());
            assertEquals(Integer.toString(i), Math.min(i, expectedPeriodInWeeks - 1), shift.getCompliance().getConsecutiveWeekends().getConsecutiveWeekendCount());
            assertEquals(Integer.toString(i), saferRosters ? i < 2 : i == 0, shift.getCompliance().getConsecutiveWeekends().isCompliant());
            assertEquals(Integer.toString(i), saferRosters ? i < 2 : i == 0, shift.getCompliance().isCompliant());
        }

    }

    private void consecutiveWeekends(boolean _1in3Weekends, int expectedPeriodInWeeks) {
        consecutiveWeekends(NONE_COMPLIANT.withCheckConsecutiveWeekends(true).with1in3Weekends(_1in3Weekends), expectedPeriodInWeeks);
    }

    private void consecutiveWeekendsSaferRosters(boolean _1in3Weekends, boolean allowFrequentConsecutiveWeekends, int expectedPeriodInWeeks) {
        consecutiveWeekends(NONE_COMPLIANT.withCheckConsecutiveWeekends(true).with1in3Weekends(_1in3Weekends).toSaferRosters().withAllowFrequentConsecutiveWeekends(allowFrequentConsecutiveWeekends), expectedPeriodInWeeks);
    }

    @Test
    public void consecutiveWeekends() {
        for (int i = 0; i < 20; i++) {
            assertTrue(shiftSpecs.add(new ShiftSpec(i * 7 + 5, 8, 0, 16, 0)));
        }
        consecutiveWeekends(false, 2);
        consecutiveWeekends(true, 3);
        consecutiveWeekendsSaferRosters(false, false, 6);
        consecutiveWeekendsSaferRosters(false, true, 5);
        consecutiveWeekendsSaferRosters(true, false, 9);
        consecutiveWeekendsSaferRosters(true, true, 8);
    }

    @Test
    public void doNotCountDaysOnSameWeekend() {
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(12, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(13, 8, 0, 16, 0)));
        List<RosteredShift> shifts = getRosteredShifts(NONE_COMPLIANT.withCheckConsecutiveWeekends(true));
        for (int i = 0; i < 2; i++) {
            RosteredShift shift = shifts.get(i);
            assertEquals(1, shift.getCompliance().getConsecutiveWeekends().getWeekendsWorkedInPeriod().size());
            assertEquals(0, shift.getCompliance().getConsecutiveWeekends().getConsecutiveWeekendCount());
            assertTrue(shift.getCompliance().getConsecutiveWeekends().isCompliant());
            assertTrue(shift.getCompliance().isCompliant());
        }
        for (int i = 2; i < 4; i++) {
            RosteredShift shift = shifts.get(i);
            assertEquals(2, shift.getCompliance().getConsecutiveWeekends().getWeekendsWorkedInPeriod().size());
            assertEquals(1, shift.getCompliance().getConsecutiveWeekends().getConsecutiveWeekendCount());
            assertFalse(shift.getCompliance().getConsecutiveWeekends().isCompliant());
            assertFalse(shift.getCompliance().isCompliant());
        }
    }

    @Test
    public void countConsecutiveWeekendsOverPeriod() {
        assertTrue(shiftSpecs.add(saturday(-2)));
        assertTrue(shiftSpecs.add(saturday(0)));
        assertTrue(shiftSpecs.add(saturday(1)));

        List<RosteredShift> shifts;
        final MockComplianceConfiguration mockConfig = NONE_COMPLIANT.withCheckConsecutiveWeekends(true);

        shifts = getRosteredShifts(mockConfig.with1in3Weekends(false));
        assertTrue(shifts.get(0).getCompliance().getConsecutiveWeekends().isCompliant());
        assertTrue(shifts.get(1).getCompliance().getConsecutiveWeekends().isCompliant());
        assertFalse(shifts.get(2).getCompliance().getConsecutiveWeekends().isCompliant());

        shifts = getRosteredShifts(mockConfig.with1in3Weekends(true));
        assertTrue(shifts.get(0).getCompliance().getConsecutiveWeekends().isCompliant());
        assertFalse(shifts.get(1).getCompliance().getConsecutiveWeekends().isCompliant());
        assertFalse(shifts.get(2).getCompliance().getConsecutiveWeekends().isCompliant());

        shifts = getRosteredShifts(mockConfig.with1in3Weekends(true).toSaferRosters());
        assertTrue(shifts.get(0).getCompliance().getConsecutiveWeekends().isCompliant());
        assertTrue(shifts.get(1).getCompliance().getConsecutiveWeekends().isCompliant());
        assertFalse(shifts.get(2).getCompliance().getConsecutiveWeekends().isCompliant());

        assertTrue(shiftSpecs.remove(saturday(-2)));

        shifts = getRosteredShifts(mockConfig.with1in3Weekends(true).toSaferRosters());
        for (RosteredShift shift : shifts)
            assertTrue(shift.getCompliance().getConsecutiveWeekends().isCompliant());

        assertTrue(shiftSpecs.add(saturday(4)));

        shifts = getRosteredShifts(mockConfig.with1in3Weekends(false).toSaferRosters().withAllowFrequentConsecutiveWeekends(true));
        assertTrue(shifts.get(1).getCompliance().getConsecutiveWeekends().isCompliant());
        assertFalse(shifts.get(2).getCompliance().getConsecutiveWeekends().isCompliant());
        assertEquals(5, shifts.get(2).getCompliance().getConsecutiveWeekends().getPeriodInWeeks());
        assertEquals(1, shifts.get(2).getCompliance().getConsecutiveWeekends().getConsecutiveWeekendCount());
        assertEquals(3, shifts.get(2).getCompliance().getConsecutiveWeekends().getWeekendsWorkedInPeriod().size());

        assertTrue(shiftSpecs.remove(saturday(4)));
        assertTrue(shiftSpecs.add(saturday(5)));

        shifts = getRosteredShifts(mockConfig.with1in3Weekends(false).toSaferRosters().withAllowFrequentConsecutiveWeekends(true));
        assertTrue(shifts.get(1).getCompliance().getConsecutiveWeekends().isCompliant());
        assertTrue(shifts.get(2).getCompliance().getConsecutiveWeekends().isCompliant());
        assertEquals(5, shifts.get(2).getCompliance().getConsecutiveWeekends().getPeriodInWeeks());
        assertEquals(0, shifts.get(2).getCompliance().getConsecutiveWeekends().getConsecutiveWeekendCount());
        assertEquals(2, shifts.get(2).getCompliance().getConsecutiveWeekends().getWeekendsWorkedInPeriod().size());

        shifts = getRosteredShifts(mockConfig.with1in3Weekends(false).toSaferRosters().withAllowFrequentConsecutiveWeekends(false));
        assertTrue(shifts.get(1).getCompliance().getConsecutiveWeekends().isCompliant());
        assertFalse(shifts.get(2).getCompliance().getConsecutiveWeekends().isCompliant());
        assertEquals(6, shifts.get(2).getCompliance().getConsecutiveWeekends().getPeriodInWeeks());
        assertEquals(1, shifts.get(2).getCompliance().getConsecutiveWeekends().getConsecutiveWeekendCount());
        assertEquals(3, shifts.get(2).getCompliance().getConsecutiveWeekends().getWeekendsWorkedInPeriod().size());

        assertTrue(shiftSpecs.remove(saturday(5)));
        assertTrue(shiftSpecs.add(saturday(6)));

        shifts = getRosteredShifts(mockConfig.with1in3Weekends(false).toSaferRosters().withAllowFrequentConsecutiveWeekends(false));
        assertTrue(shifts.get(1).getCompliance().getConsecutiveWeekends().isCompliant());
        assertTrue(shifts.get(2).getCompliance().getConsecutiveWeekends().isCompliant());
        assertEquals(6, shifts.get(2).getCompliance().getConsecutiveWeekends().getPeriodInWeeks());
        assertEquals(0, shifts.get(2).getCompliance().getConsecutiveWeekends().getConsecutiveWeekendCount());
        assertEquals(2, shifts.get(2).getCompliance().getConsecutiveWeekends().getWeekendsWorkedInPeriod().size());

        shifts = getRosteredShifts(mockConfig.with1in3Weekends(true).toSaferRosters().withAllowFrequentConsecutiveWeekends(true));
        assertTrue(shifts.get(1).getCompliance().getConsecutiveWeekends().isCompliant());
        assertFalse(shifts.get(2).getCompliance().getConsecutiveWeekends().isCompliant());
        assertEquals(8, shifts.get(2).getCompliance().getConsecutiveWeekends().getPeriodInWeeks());
        assertEquals(1, shifts.get(2).getCompliance().getConsecutiveWeekends().getConsecutiveWeekendCount());
        assertEquals(3, shifts.get(2).getCompliance().getConsecutiveWeekends().getWeekendsWorkedInPeriod().size());

        assertTrue(shiftSpecs.remove(saturday(6)));
        assertTrue(shiftSpecs.add(saturday(7)));

        shifts = getRosteredShifts(mockConfig.with1in3Weekends(true).toSaferRosters().withAllowFrequentConsecutiveWeekends(true));
        assertTrue(shifts.get(1).getCompliance().getConsecutiveWeekends().isCompliant());
        assertFalse(shifts.get(2).getCompliance().getConsecutiveWeekends().isCompliant());
        assertEquals(8, shifts.get(2).getCompliance().getConsecutiveWeekends().getPeriodInWeeks());
        assertEquals(1, shifts.get(2).getCompliance().getConsecutiveWeekends().getConsecutiveWeekendCount());
        assertEquals(3, shifts.get(2).getCompliance().getConsecutiveWeekends().getWeekendsWorkedInPeriod().size());

        assertTrue(shiftSpecs.remove(saturday(7)));
        assertTrue(shiftSpecs.add(saturday(8)));

        shifts = getRosteredShifts(mockConfig.with1in3Weekends(true).toSaferRosters().withAllowFrequentConsecutiveWeekends(true));
        assertTrue(shifts.get(1).getCompliance().getConsecutiveWeekends().isCompliant());
        assertTrue(shifts.get(2).getCompliance().getConsecutiveWeekends().isCompliant());
        assertEquals(8, shifts.get(2).getCompliance().getConsecutiveWeekends().getPeriodInWeeks());
        assertEquals(0, shifts.get(2).getCompliance().getConsecutiveWeekends().getConsecutiveWeekendCount());
        assertEquals(2, shifts.get(2).getCompliance().getConsecutiveWeekends().getWeekendsWorkedInPeriod().size());

        shifts = getRosteredShifts(mockConfig.with1in3Weekends(true).toSaferRosters().withAllowFrequentConsecutiveWeekends(false));
        assertTrue(shifts.get(1).getCompliance().getConsecutiveWeekends().isCompliant());
        assertFalse(shifts.get(2).getCompliance().getConsecutiveWeekends().isCompliant());
        assertEquals(9, shifts.get(2).getCompliance().getConsecutiveWeekends().getPeriodInWeeks());
        assertEquals(1, shifts.get(2).getCompliance().getConsecutiveWeekends().getConsecutiveWeekendCount());
        assertEquals(3, shifts.get(2).getCompliance().getConsecutiveWeekends().getWeekendsWorkedInPeriod().size());

        assertTrue(shiftSpecs.remove(saturday(8)));
        assertTrue(shiftSpecs.add(saturday(9)));

        shifts = getRosteredShifts(mockConfig.with1in3Weekends(true).toSaferRosters().withAllowFrequentConsecutiveWeekends(false));
        assertTrue(shifts.get(1).getCompliance().getConsecutiveWeekends().isCompliant());
        assertTrue(shifts.get(2).getCompliance().getConsecutiveWeekends().isCompliant());
        assertEquals(9, shifts.get(2).getCompliance().getConsecutiveWeekends().getPeriodInWeeks());
        assertEquals(0, shifts.get(2).getCompliance().getConsecutiveWeekends().getConsecutiveWeekendCount());
        assertEquals(2, shifts.get(2).getCompliance().getConsecutiveWeekends().getWeekendsWorkedInPeriod().size());

    }

}