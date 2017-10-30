package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.ShiftSpec;

import org.junit.Test;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RDOTest extends RosteredShiftTest {

    private static final MockConfigurationSaferRosters CONFIGURATION_SAFER_ROSTERS = new MockConfiguration().toSaferRosters().withCheckRDOs(true);

    @Test
    public void sanity() {
        for (int i = 0; i < 12; i++) {
            assertTrue(shiftSpecs.add(new ShiftSpec(i, 8, 0, 16, 0)));
        }
        List<RosteredShift> rosteredShifts = getRosteredShifts(CONFIGURATION_SAFER_ROSTERS);
        RDO rdo = new RDO(rosteredShifts, CONFIGURATION_SAFER_ROSTERS);
        for (int i = 0; i < rosteredShifts.size(); i++) {
            RosteredShift shift = rosteredShifts.get(i);
            RowRosteredDayOff rosteredDayOff = shift.getCompliance().getRosteredDayOff();
            if (i == 5 || i == 6) {
                assertNotNull(rosteredDayOff);
                assertNull(rosteredDayOff.getDate());
                assertFalse(rosteredDayOff.isCompliant());
            } else {
                assertNull(rosteredDayOff);
            }
            LocalDate shiftDate = shift.getShiftData().getStart().toLocalDate();
            assertTrue(rdo.getWorkedDates().contains(shiftDate));
            assertFalse(rdo.getWeekdayRosteredDaysOff().contains(shiftDate));
        }
        assertEquals(12, rdo.getWorkedDates().size());
        assertTrue(rdo.getWeekdayRosteredDaysOff().isEmpty());
    }

    private void testDaysOff(boolean allowMidweekRDOs, @NonNull int[] dates, @NonNull int[] assignedWeekdayRDOsIndices, @NonNull int[] weekdayRDOs) {
        shiftSpecs.clear();
        String message = "testDaysOff() called with: allowMidweekRDOs = [" + allowMidweekRDOs + "], dates = [" + Arrays.toString(dates) + "], assignedWeekdayRDOsIndex = [" + Arrays.toString(assignedWeekdayRDOsIndices) + "], weekdayRDOs = [" + Arrays.toString(weekdayRDOs) + "]";
        assertEquals(message, assignedWeekdayRDOsIndices.length, weekdayRDOs.length);
        for (int daysAfterStart : dates
                ) {
            shiftSpecs.add(new ShiftSpec(daysAfterStart, 8, 0, 16, 0));
        }
        MockConfigurationSaferRosters configuration = CONFIGURATION_SAFER_ROSTERS.withAllowMidweekRDOs(allowMidweekRDOs);
        List<RosteredShift> rosteredShifts = getRosteredShifts(configuration);
        for (int assignedWeekdayRdoIndex : assignedWeekdayRDOsIndices) {
            assertTrue(message, assignedWeekdayRdoIndex < rosteredShifts.size());
        }
        RDO rdo = new RDO(rosteredShifts, configuration);
        for (int shiftIndex = 0; shiftIndex < rosteredShifts.size(); shiftIndex++) {
            RosteredShift shift = rosteredShifts.get(shiftIndex);
            Compliance compliance = shift.getCompliance();
            boolean shouldHaveAssignedRDO = false;
            for (int assignedWeekdayRdoIndex = 0; !shouldHaveAssignedRDO && assignedWeekdayRdoIndex < assignedWeekdayRDOsIndices.length; assignedWeekdayRdoIndex++) {
                if (shiftIndex == assignedWeekdayRDOsIndices[assignedWeekdayRdoIndex]) {
                    shouldHaveAssignedRDO = true;
                    assertTrue(message, shift.getShiftData().getStart().getDayOfWeek() == DayOfWeek.SATURDAY || shift.getShiftData().getStart().getDayOfWeek() == DayOfWeek.SUNDAY);
                    assertNotNull(compliance.getWeekend());
                    LocalDate rosteredDayOff = ShiftSpec.START_DATE.plusDays(weekdayRDOs[assignedWeekdayRdoIndex]);
                    assertNotNull(compliance.getRosteredDayOff());
                    assertNotNull(compliance.getRosteredDayOff().getDate());
                    assertEquals(rosteredDayOff, compliance.getRosteredDayOff().getDate());
                    assertTrue(message, rdo.getWeekdayRosteredDaysOff().contains(rosteredDayOff));
                }
            }
            if (!shouldHaveAssignedRDO) {
                if (compliance.getWeekend() != null) {
                    assertNotNull(compliance.getRosteredDayOff());
                    assertNull(compliance.getRosteredDayOff().getDate());
                } else {
                    assertNull(compliance.getRosteredDayOff());
                }
            }
        }
        assertEquals(message, assignedWeekdayRDOsIndices.length, rdo.getWeekdayRosteredDaysOff().size());
    }

    @Test
    public void oneDayOff() {
        for (int i = 0; i < 12; i++) {
            if (i == 5 || i == 6) continue;
            int[] dates = new int[11];
            for (int j = 0; j < 12; j++) {
                if (j == i) continue;
                dates[j < i ? j : j - 1] = j;
            }
            if (i == 0 || i == 11) {
                testDaysOff(true, dates, new int[]{i < 5 ? 4 : 5}, new int[]{i});
                testDaysOff(false, dates, new int[]{i < 5 ? 4 : 5}, new int[]{i});
            } else {
                testDaysOff(true, dates, new int[]{}, new int[]{});
                testDaysOff(false, dates, new int[]{}, new int[]{});
            }
        }
    }

    @Test
    public void twoDaysOffStartFirstWeek() {
        testDaysOff(true, new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, new int[]{3, 4}, new int[]{0, 1});
        testDaysOff(false, new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, new int[]{3, 4}, new int[]{0, 1});
    }

    @Test
    public void twoDaysOffMiddleFirstWeek() {
        testDaysOff(true, new int[]{0, 3, 4, 5, 6, 7, 8, 9, 10, 11}, new int[]{3, 4}, new int[]{1, 2});
        testDaysOff(false, new int[]{0, 3, 4, 5, 6, 7, 8, 9, 10, 11}, new int[]{}, new int[]{});
    }

    @Test
    public void twoDaysOffEndFirstWeek() {
        testDaysOff(true, new int[]{0, 1, 2, 5, 6, 7, 8, 9, 10, 11}, new int[]{3, 4}, new int[]{3, 4});
        testDaysOff(false, new int[]{0, 1, 2, 5, 6, 7, 8, 9, 10, 11}, new int[]{}, new int[]{});
    }

    @Test
    public void twoDaysOffStartSecondWeek() {
        testDaysOff(true, new int[]{0, 1, 2, 3, 4, 5, 6, 9, 10, 11}, new int[]{5, 6}, new int[]{7, 8});
        testDaysOff(false, new int[]{0, 1, 2, 3, 4, 5, 6, 9, 10, 11}, new int[]{}, new int[]{});
    }

    @Test
    public void twoDaysOffMiddleSecondWeek() {
        testDaysOff(true, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 11}, new int[]{5, 6}, new int[]{9, 10});
        testDaysOff(false, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 11}, new int[]{}, new int[]{});
    }

    @Test
    public void twoDaysOffEndSecondWeek() {
        testDaysOff(true, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, new int[]{5, 6}, new int[]{10, 11});
        testDaysOff(false, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, new int[]{5, 6}, new int[]{10, 11});
    }

    @Test
    public void threeScatteredDaysOff() {
        testDaysOff(true, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 11, 12, 13, 14, 15, 16, 17}, new int[]{4, 5, 9, 10}, new int[]{0, 9, 10, 18});
        testDaysOff(false, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 11, 12, 13, 14, 15, 16, 17}, new int[]{4, 9}, new int[]{0, 18});
    }

}