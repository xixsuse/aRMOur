package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

final class MockComplianceConfigurationSaferRosters extends ComplianceConfigurationSaferRosters {

    private MockComplianceConfigurationSaferRosters(boolean _1in3weekends, boolean allowFrequentConsecutiveWeekends, boolean allow5ConsecutiveNights, boolean allowOnly1RecoveryDayFollowing3Nights, boolean allowMidweekRosteredDaysOff, boolean checkDurationBetweenShifts, boolean checkDurationOverDay, boolean checkDurationOverWeek, boolean checkDurationOverFortnight, boolean checkLongDaysPerWeek, boolean checkConsecutiveDays, boolean checkConsecutiveWeekends, boolean checkConsecutiveNights, boolean checkRecoveryFollowingNights, boolean checkRosteredDaysOff) {
        super(_1in3weekends, allowFrequentConsecutiveWeekends, allow5ConsecutiveNights, allowOnly1RecoveryDayFollowing3Nights, allowMidweekRosteredDaysOff, checkDurationBetweenShifts, checkDurationOverDay, checkDurationOverWeek, checkDurationOverFortnight, checkLongDaysPerWeek, checkConsecutiveDays, checkConsecutiveWeekends, checkConsecutiveNights, checkRecoveryFollowingNights, checkRosteredDaysOff);
    }

    MockComplianceConfigurationSaferRosters(boolean _1in3weekends, boolean checkDurationBetweenShifts, boolean checkDurationOverDay, boolean checkDurationOverWeek, boolean checkDurationOverFortnight, boolean checkLongDaysPerWeek, boolean checkConsecutiveDays, boolean checkConsecutiveWeekends, boolean checkConsecutiveNights, boolean checkRecoveryFollowingNights) {
        this(
                _1in3weekends,
                false,
                false,
                false,
                false,
                checkDurationBetweenShifts,
                checkDurationOverDay,
                checkDurationOverWeek,
                checkDurationOverFortnight,
                checkLongDaysPerWeek,
                checkConsecutiveDays,
                checkConsecutiveWeekends,
                checkConsecutiveNights,
                checkRecoveryFollowingNights,
                false
        );
    }

    @NonNull
    MockComplianceConfigurationSaferRosters withAllowFrequentConsecutiveWeekends(boolean allowFrequentConsecutiveWeekends) {
        return new MockComplianceConfigurationSaferRosters(
                _1in3Weekends(),
                allowFrequentConsecutiveWeekends,
                allow5ConsecutiveNights(),
                allowOnly1RecoveryDayFollowing3Nights(),
                allowMidweekRosteredDaysOff(),
                checkDurationBetweenShifts(),
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveWeekends(),
                checkConsecutiveNights(),
                checkRecoveryFollowingNights(),
                checkRosteredDaysOff()
        );
    }

    @NonNull
    MockComplianceConfigurationSaferRosters withAllow5ConsecutiveNights(boolean allow5ConsecutiveNights) {
        return new MockComplianceConfigurationSaferRosters(
                _1in3Weekends(),
                allowFrequentConsecutiveWeekends(),
                allow5ConsecutiveNights,
                allowOnly1RecoveryDayFollowing3Nights(),
                allowMidweekRosteredDaysOff(),
                checkDurationBetweenShifts(),
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveWeekends(),
                checkConsecutiveNights(),
                checkRecoveryFollowingNights(),
                checkRosteredDaysOff()
        );
    }

    @NonNull
    MockComplianceConfigurationSaferRosters withAllowOnly1RecoveryDayFollowing3Nights(@SuppressWarnings("SameParameterValue") boolean allowOnly1RecoveryDayFollowing3Nights) {
        return new MockComplianceConfigurationSaferRosters(
                _1in3Weekends(),
                allowFrequentConsecutiveWeekends(),
                allow5ConsecutiveNights(),
                allowOnly1RecoveryDayFollowing3Nights,
                allowMidweekRosteredDaysOff(),
                checkDurationBetweenShifts(),
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveWeekends(),
                checkConsecutiveNights(),
                checkRecoveryFollowingNights(),
                checkRosteredDaysOff()
        );
    }

    @NonNull
    MockComplianceConfigurationSaferRosters withAllowMidweekRosteredDaysOff(boolean allowMidweekRosteredDaysOff) {
        return new MockComplianceConfigurationSaferRosters(
                _1in3Weekends(),
                allowFrequentConsecutiveWeekends(),
                allow5ConsecutiveNights(),
                allowOnly1RecoveryDayFollowing3Nights(),
                allowMidweekRosteredDaysOff,
                checkDurationBetweenShifts(),
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveWeekends(),
                checkConsecutiveNights(),
                checkRecoveryFollowingNights(),
                checkRosteredDaysOff()
        );
    }

    @NonNull
    MockComplianceConfigurationSaferRosters withCheckRosteredDaysOff(@SuppressWarnings("SameParameterValue") boolean checkRosteredDaysOff) {
        return new MockComplianceConfigurationSaferRosters(
                _1in3Weekends(),
                allowFrequentConsecutiveWeekends(),
                allow5ConsecutiveNights(),
                allowOnly1RecoveryDayFollowing3Nights(),
                allowMidweekRosteredDaysOff(),
                checkDurationBetweenShifts(),
                checkDurationOverDay(),
                checkDurationOverWeek(),
                checkDurationOverFortnight(),
                checkLongDaysPerWeek(),
                checkConsecutiveDays(),
                checkConsecutiveWeekends(),
                checkConsecutiveNights(),
                checkRecoveryFollowingNights(),
                checkRosteredDaysOff
        );
    }
}
