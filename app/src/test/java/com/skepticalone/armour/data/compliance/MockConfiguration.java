package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

class MockConfiguration extends Configuration {

    private MockConfiguration(boolean checkDurationOverDay, boolean checkDurationOverWeek, boolean checkDurationOverFortnight, boolean checkDurationBetweenShifts, boolean checkLongDaysPerWeek, boolean checkConsecutiveDays, boolean checkConsecutiveNightsWorked, boolean checkRecoveryFollowingNights, boolean allow1in2Weekends, boolean checkFrequencyOfWeekends) {
        super(checkDurationOverDay, checkDurationOverWeek, checkDurationOverFortnight, checkDurationBetweenShifts, checkLongDaysPerWeek, checkConsecutiveDays, checkConsecutiveNightsWorked, checkRecoveryFollowingNights, allow1in2Weekends, checkFrequencyOfWeekends);
    }

    MockConfiguration() {
        this(
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                true,
                false
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfiguration withCheckDurationOverDay(boolean checkDurationOverDay) {
        return new MockConfiguration(
                checkDurationOverDay,
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkDurationBetweenShifts(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveNightsWorked(),
                checkRecoveryFollowingNights(),
                allow1in2Weekends(),
                checkFrequencyOfWeekends()
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfiguration withCheckDurationOverWeek(boolean checkDurationOverWeek) {
        return new MockConfiguration(
                checkDurationOverDay(),
                checkDurationOverWeek,
                checkDurationOverFortnight(),
                checkDurationBetweenShifts(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveNightsWorked(),
                checkRecoveryFollowingNights(),
                allow1in2Weekends(),
                checkFrequencyOfWeekends()
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfiguration withCheckDurationOverFortnight(boolean checkDurationOverFortnight) {
        return new MockConfiguration(
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight,
                checkDurationBetweenShifts(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveNightsWorked(),
                checkRecoveryFollowingNights(),
                allow1in2Weekends(),
                checkFrequencyOfWeekends()
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfiguration withCheckDurationBetweenShifts(boolean checkDurationBetweenShifts) {
        return new MockConfiguration(
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkDurationBetweenShifts,
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveNightsWorked(),
                checkRecoveryFollowingNights(),
                allow1in2Weekends(),
                checkFrequencyOfWeekends()
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfiguration withCheckLongDaysPerWeek(boolean checkLongDaysPerWeek) {
        return new MockConfiguration(
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkDurationBetweenShifts(),
                checkLongDaysPerWeek,
                checkConsecutiveDays(),
                checkConsecutiveNightsWorked(),
                checkRecoveryFollowingNights(),
                allow1in2Weekends(),
                checkFrequencyOfWeekends()
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfiguration withCheckConsecutiveDays(boolean checkConsecutiveDays) {
        return new MockConfiguration(
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkDurationBetweenShifts(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays,
                checkConsecutiveNightsWorked(),
                checkRecoveryFollowingNights(),
                allow1in2Weekends(),
                checkFrequencyOfWeekends()
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfiguration withCheckConsecutiveNightsWorked(boolean checkConsecutiveNightsWorked) {
        return new MockConfiguration(
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkDurationBetweenShifts(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveNightsWorked,
                checkRecoveryFollowingNights(),
                allow1in2Weekends(),
                checkFrequencyOfWeekends()
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfiguration withCheckRecoveryFollowingNights(boolean checkRecoveryFollowingNights) {
        return new MockConfiguration(
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkDurationBetweenShifts(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveNightsWorked(),
                checkRecoveryFollowingNights,
                allow1in2Weekends(),
                checkFrequencyOfWeekends()
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfiguration withAllow1in2Weekends(boolean allow1in2Weekends) {
        return new MockConfiguration(
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkDurationBetweenShifts(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveNightsWorked(),
                checkRecoveryFollowingNights(),
                allow1in2Weekends,
                checkFrequencyOfWeekends()
        );
    }

    @SuppressWarnings("SameParameterValue")
    @NonNull
    MockConfiguration withCheckFrequencyOfWeekends(boolean checkFrequencyOfWeekends) {
        return new MockConfiguration(
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkDurationBetweenShifts(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveNightsWorked(),
                checkRecoveryFollowingNights(),
                allow1in2Weekends(),
                checkFrequencyOfWeekends
        );
    }

    @NonNull
    MockConfigurationSaferRosters toSaferRosters() {
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
                checkFrequencyOfWeekends()
        );
    }

}

