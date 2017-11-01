package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

final class MockConfigurationSaferRosters extends ConfigurationSaferRosters {

    private MockConfigurationSaferRosters(boolean checkDurationOverDay, boolean checkDurationOverWeek, boolean checkDurationOverFortnight, boolean checkDurationBetweenShifts, boolean checkLongDaysPerWeek, boolean checkConsecutiveDays, boolean checkConsecutiveNightsWorked, boolean checkRecoveryDaysFollowingNights, boolean allow1in2Weekends, boolean checkFrequencyOfWeekends, boolean allowFrequentConsecutiveWeekends, boolean allow5ConsecutiveNights, boolean allowOnly1RecoveryDayFollowing3Nights, boolean allowMidweekRDOs, boolean checkRDOs) {
        super(checkDurationOverDay, checkDurationOverWeek, checkDurationOverFortnight, checkDurationBetweenShifts, checkLongDaysPerWeek, checkConsecutiveDays, checkConsecutiveNightsWorked, checkRecoveryDaysFollowingNights, allow1in2Weekends, checkFrequencyOfWeekends, allowFrequentConsecutiveWeekends, allow5ConsecutiveNights, allowOnly1RecoveryDayFollowing3Nights, allowMidweekRDOs, checkRDOs);
    }

    MockConfigurationSaferRosters(boolean checkDurationOverDay, boolean checkDurationOverWeek, boolean checkDurationOverFortnight, boolean checkDurationBetweenShifts, boolean checkLongDaysPerWeek, boolean checkConsecutiveDays, boolean checkConsecutiveNightsWorked, boolean checkRecoveryDaysFollowingNights, boolean allow1in2Weekends, boolean checkFrequencyOfWeekends) {
        this(
                checkDurationOverDay,
                checkDurationOverWeek,
                checkDurationOverFortnight,
                checkDurationBetweenShifts,
                checkLongDaysPerWeek,
                checkConsecutiveDays,
                checkConsecutiveNightsWorked,
                checkRecoveryDaysFollowingNights,
                allow1in2Weekends,
                checkFrequencyOfWeekends,
                false,
                false,
                false,
                false,
                false
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfigurationSaferRosters withAllowFrequentConsecutiveWeekends(boolean allowFrequentConsecutiveWeekends) {
        return new MockConfigurationSaferRosters(
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkDurationBetweenShifts(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveNightsWorked(),
                checkRecoveryFollowingNights(),
                allow1in2Weekends(),
                checkFrequencyOfWeekends(),
                allowFrequentConsecutiveWeekends,
                allow5ConsecutiveNights(),
                allowOnly1RecoveryDayFollowing3Nights(),
                allowMidweekRDOs(),
                checkRDOs()
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfigurationSaferRosters withAllow5ConsecutiveNights(boolean allow5ConsecutiveNights) {
        return new MockConfigurationSaferRosters(
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkDurationBetweenShifts(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveNightsWorked(),
                checkRecoveryFollowingNights(),
                allow1in2Weekends(),
                checkFrequencyOfWeekends(),
                allowFrequentConsecutiveWeekends(),
                allow5ConsecutiveNights,
                allowOnly1RecoveryDayFollowing3Nights(),
                allowMidweekRDOs(),
                checkRDOs()
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfigurationSaferRosters withAllowOnly1RecoveryDayFollowing3Nights(boolean allowOnly1RecoveryDayFollowing3Nights) {
        return new MockConfigurationSaferRosters(
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkDurationBetweenShifts(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveNightsWorked(),
                checkRecoveryFollowingNights(),
                allow1in2Weekends(),
                checkFrequencyOfWeekends(),
                allowFrequentConsecutiveWeekends(),
                allow5ConsecutiveNights(),
                allowOnly1RecoveryDayFollowing3Nights,
                allowMidweekRDOs(),
                checkRDOs()
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfigurationSaferRosters withAllowMidweekRDOs(boolean allowMidweekRDOs) {
        return new MockConfigurationSaferRosters(
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkDurationBetweenShifts(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveNightsWorked(),
                checkRecoveryFollowingNights(),
                allow1in2Weekends(),
                checkFrequencyOfWeekends(),
                allowFrequentConsecutiveWeekends(),
                allow5ConsecutiveNights(),
                allowOnly1RecoveryDayFollowing3Nights(),
                allowMidweekRDOs,
                checkRDOs()
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfigurationSaferRosters withCheckRDOs(boolean checkRDOs) {
        return new MockConfigurationSaferRosters(
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkDurationBetweenShifts(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveNightsWorked(),
                checkRecoveryFollowingNights(),
                allow1in2Weekends(),
                checkFrequencyOfWeekends(),
                allowFrequentConsecutiveWeekends(),
                allow5ConsecutiveNights(),
                allowOnly1RecoveryDayFollowing3Nights(),
                allowMidweekRDOs(),
                checkRDOs
        );
    }
}
