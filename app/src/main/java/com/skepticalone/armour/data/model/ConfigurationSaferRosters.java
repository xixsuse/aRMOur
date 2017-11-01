package com.skepticalone.armour.data.model;

class ConfigurationSaferRosters extends Configuration {

    private final boolean
            allowFrequentConsecutiveWeekends,
            allow5ConsecutiveNights,
            allowOnly1RecoveryDayFollowing3Nights,
            allowMidweekRDOs,
            checkRDOs;

    ConfigurationSaferRosters(
            boolean checkDurationOverDay,
            boolean checkDurationOverWeek,
            boolean checkDurationOverFortnight,
            boolean checkDurationBetweenShifts,
            boolean checkLongDaysPerWeek,
            boolean checkConsecutiveDays,
            boolean checkConsecutiveNightsWorked,
            boolean checkRecoveryDaysFollowingNights,
            boolean allow1in2Weekends,
            boolean checkFrequencyOfWeekends,
            boolean allowFrequentConsecutiveWeekends,
            boolean allow5ConsecutiveNights,
            boolean allowOnly1RecoveryDayFollowing3Nights,
            boolean allowMidweekRDOs,
            boolean checkRDOs
    ) {
        super(checkDurationOverDay, checkDurationOverWeek, checkDurationOverFortnight, checkDurationBetweenShifts, checkLongDaysPerWeek, checkConsecutiveDays, checkConsecutiveNightsWorked, checkRecoveryDaysFollowingNights, allow1in2Weekends, checkFrequencyOfWeekends);
        this.allowFrequentConsecutiveWeekends = allowFrequentConsecutiveWeekends;
        this.allow5ConsecutiveNights = allow5ConsecutiveNights;
        this.allowOnly1RecoveryDayFollowing3Nights = allowOnly1RecoveryDayFollowing3Nights;
        this.allowMidweekRDOs = allowMidweekRDOs;
        this.checkRDOs = checkRDOs;
    }

    public final boolean allowFrequentConsecutiveWeekends() {
        return allowFrequentConsecutiveWeekends;
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
