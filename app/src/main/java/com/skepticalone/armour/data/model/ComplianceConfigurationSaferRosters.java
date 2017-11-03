package com.skepticalone.armour.data.model;

class ComplianceConfigurationSaferRosters extends ComplianceConfiguration {

    private final boolean
            allowFrequentConsecutiveWeekends,
            allow5ConsecutiveNights,
            allowOnly1RecoveryDayFollowing3Nights,
            allowMidweekRosteredDaysOff,
            checkRosteredDaysOff;

    ComplianceConfigurationSaferRosters(
            boolean _1in3weekends,
            boolean allowFrequentConsecutiveWeekends,
            boolean allow5ConsecutiveNights,
            boolean allowOnly1RecoveryDayFollowing3Nights,
            boolean allowMidweekRosteredDaysOff,
            boolean checkDurationBetweenShifts,
            boolean checkDurationOverDay,
            boolean checkDurationOverWeek,
            boolean checkDurationOverFortnight,
            boolean checkLongDaysPerWeek,
            boolean checkConsecutiveDays,
            boolean checkConsecutiveWeekends, boolean checkConsecutiveNights,
            boolean checkRecoveryFollowingNights,
            boolean checkRosteredDaysOff
    ) {
        super(_1in3weekends, checkDurationBetweenShifts, checkDurationOverDay, checkDurationOverWeek, checkDurationOverFortnight, checkLongDaysPerWeek, checkConsecutiveDays, checkConsecutiveWeekends, checkConsecutiveNights, checkRecoveryFollowingNights);
        this.allowFrequentConsecutiveWeekends = allowFrequentConsecutiveWeekends;
        this.allow5ConsecutiveNights = allow5ConsecutiveNights;
        this.allowOnly1RecoveryDayFollowing3Nights = allowOnly1RecoveryDayFollowing3Nights;
        this.allowMidweekRosteredDaysOff = allowMidweekRosteredDaysOff;
        this.checkRosteredDaysOff = checkRosteredDaysOff;
    }

    final boolean allowFrequentConsecutiveWeekends() {
        return allowFrequentConsecutiveWeekends;
    }

    final boolean allow5ConsecutiveNights() {
        return allow5ConsecutiveNights;
    }

    final boolean allowOnly1RecoveryDayFollowing3Nights() {
        return allowOnly1RecoveryDayFollowing3Nights;
    }

    final boolean allowMidweekRosteredDaysOff() {
        return allowMidweekRosteredDaysOff;
    }

    final boolean checkRosteredDaysOff() {
        return checkRosteredDaysOff;
    }
}
