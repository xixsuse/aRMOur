package com.skepticalone.armour.data.compliance;

final class SaferRostersConfiguration extends Configuration {

    private final boolean
            allow5ConsecutiveNights,
            allowOnly1RecoveryDayFollowing3Nights,
            allowMidweekRDOs,
            checkRDOs;

    SaferRostersConfiguration(
            boolean checkDurationOverDay, boolean checkDurationOverWeek, boolean checkDurationOverFortnight, boolean checkDurationBetweenShifts, boolean checkLongDaysPerWeek, boolean checkConsecutiveDays, boolean checkConsecutiveNightsWorked, boolean checkRecoveryDaysFollowingNights,
            boolean allow5ConsecutiveNights,
            boolean allowOnly1RecoveryDayFollowing3Nights,
            boolean allowMidweekRDOs,
            boolean checkRDOs
    ) {
        super(checkDurationOverDay, checkDurationOverWeek, checkDurationOverFortnight, checkDurationBetweenShifts, checkLongDaysPerWeek, checkConsecutiveDays, checkConsecutiveNightsWorked, checkRecoveryDaysFollowingNights);
        this.allow5ConsecutiveNights = allow5ConsecutiveNights;
        this.allowOnly1RecoveryDayFollowing3Nights = allowOnly1RecoveryDayFollowing3Nights;
        this.allowMidweekRDOs = allowMidweekRDOs;
        this.checkRDOs = checkRDOs;
    }

    public final boolean allow5ConsecutiveNights() {
        return allow5ConsecutiveNights;
    }

    public final boolean allowOnly1RecoveryDayFollowing3Nights() {
        return allowOnly1RecoveryDayFollowing3Nights;
    }

    public final boolean allowMidweekRDOs() {
        return allowMidweekRDOs;
    }

    public final boolean checkRDOs() {
        return checkRDOs;
    }
}
