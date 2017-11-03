package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

class MockComplianceConfiguration extends ComplianceConfiguration {
    private MockComplianceConfiguration(boolean _1in3Weekends, boolean checkDurationBetweenShifts, boolean checkDurationOverDay, boolean checkDurationOverWeek, boolean checkDurationOverFortnight, boolean checkLongDaysPerWeek, boolean checkConsecutiveDays, boolean checkConsecutiveWeekends, boolean checkConsecutiveNights, boolean checkRecoveryFollowingNights) {
        super(_1in3Weekends, checkDurationBetweenShifts, checkDurationOverDay, checkDurationOverWeek, checkDurationOverFortnight, checkLongDaysPerWeek, checkConsecutiveDays, checkConsecutiveWeekends, checkConsecutiveNights, checkRecoveryFollowingNights);
    }

    MockComplianceConfiguration() {
        this(
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false
        );
    }

    @NonNull
    MockComplianceConfiguration withCheckDurationBetweenShifts(@SuppressWarnings("SameParameterValue") boolean checkDurationBetweenShifts) {
        return new MockComplianceConfiguration(
                _1in3Weekends(),
                checkDurationBetweenShifts,
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveWeekends(),
                checkConsecutiveNights(),
                checkRecoveryFollowingNights()
        );
    }

    @NonNull
    MockComplianceConfiguration with1in3Weekends(boolean _1in3Weekends) {
        return new MockComplianceConfiguration(
                _1in3Weekends,
                checkDurationBetweenShifts(),
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveWeekends(),
                checkConsecutiveNights(),
                checkRecoveryFollowingNights()
        );
    }

    @NonNull
    MockComplianceConfiguration withCheckDurationOverDay(@SuppressWarnings("SameParameterValue") boolean checkDurationOverDay) {
        return new MockComplianceConfiguration(
                _1in3Weekends(),
                checkDurationBetweenShifts(),
                checkDurationOverDay,
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveWeekends(),
                checkConsecutiveNights(),
                checkRecoveryFollowingNights()
        );
    }

    @NonNull
    MockComplianceConfiguration withCheckDurationOverWeek(@SuppressWarnings("SameParameterValue") boolean checkDurationOverWeek) {
        return new MockComplianceConfiguration(
                _1in3Weekends(),
                checkDurationBetweenShifts(),
                checkDurationOverDay(),
                checkDurationOverWeek,
                checkDurationOverFortnight(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveWeekends(),
                checkConsecutiveNights(),
                checkRecoveryFollowingNights()
        );
    }

    @NonNull
    MockComplianceConfiguration withCheckDurationOverFortnight(@SuppressWarnings("SameParameterValue") boolean checkDurationOverFortnight) {
        return new MockComplianceConfiguration(
                _1in3Weekends(),
                checkDurationBetweenShifts(),
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight,
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveWeekends(),
                checkConsecutiveNights(),
                checkRecoveryFollowingNights()
        );
    }

    @NonNull
    MockComplianceConfiguration withCheckLongDaysPerWeek(@SuppressWarnings("SameParameterValue") boolean checkLongDaysPerWeek) {
        return new MockComplianceConfiguration(
                _1in3Weekends(),
                checkDurationBetweenShifts(),
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkLongDaysPerWeek,
                checkConsecutiveDays(),
                checkConsecutiveWeekends(),
                checkConsecutiveNights(),
                checkRecoveryFollowingNights()
        );
    }

    @NonNull
    MockComplianceConfiguration withCheckConsecutiveDays(@SuppressWarnings("SameParameterValue") boolean checkConsecutiveDays) {
        return new MockComplianceConfiguration(
                _1in3Weekends(),
                checkDurationBetweenShifts(),
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays,
                checkConsecutiveWeekends(),
                checkConsecutiveNights(),
                checkRecoveryFollowingNights()
        );
    }

    @NonNull
    MockComplianceConfiguration withCheckConsecutiveWeekends(@SuppressWarnings("SameParameterValue") boolean checkConsecutiveWeekends) {
        return new MockComplianceConfiguration(
                _1in3Weekends(),
                checkDurationBetweenShifts(),
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveWeekends,
                checkConsecutiveNights(),
                checkRecoveryFollowingNights()
        );
    }

    @NonNull
    MockComplianceConfiguration withCheckConsecutiveNights(@SuppressWarnings("SameParameterValue") boolean checkConsecutiveNights) {
        return new MockComplianceConfiguration(
                _1in3Weekends(),
                checkDurationBetweenShifts(),
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveWeekends(),
                checkConsecutiveNights,
                checkRecoveryFollowingNights()
        );
    }

    @NonNull
    MockComplianceConfiguration withCheckRecoveryFollowingNights(@SuppressWarnings("SameParameterValue") boolean checkRecoveryFollowingNights) {
        return new MockComplianceConfiguration(
                _1in3Weekends(),
                checkDurationBetweenShifts(),
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveWeekends(),
                checkConsecutiveNights(),
                checkRecoveryFollowingNights
        );
    }


    @NonNull
    MockComplianceConfigurationSaferRosters toSaferRosters() {
        return new MockComplianceConfigurationSaferRosters(
                _1in3Weekends(), checkDurationBetweenShifts(), checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveWeekends(), checkConsecutiveNights(),
                checkRecoveryFollowingNights()
        );
    }

}

