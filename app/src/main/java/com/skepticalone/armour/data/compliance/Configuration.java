package com.skepticalone.armour.data.compliance;

public class Configuration {

    private final boolean
            checkDurationOverDay,
            checkDurationOverWeek,
            checkDurationOverFortnight,
            checkDurationBetweenShifts,
            checkLongDaysPerWeek,
            checkConsecutiveDays,
            checkConsecutiveNightsWorked,
            checkRecoveryDaysFollowingNights,
            allow1in2Weekends,
            checkFrequencyOfWeekends;

    Configuration(
            boolean checkDurationOverDay,
            boolean checkDurationOverWeek,
            boolean checkDurationOverFortnight,
            boolean checkDurationBetweenShifts,
            boolean checkLongDaysPerWeek,
            boolean checkConsecutiveDays,
            boolean checkConsecutiveNightsWorked,
            boolean checkRecoveryDaysFollowingNights,
            boolean allow1in2Weekends,
            boolean checkFrequencyOfWeekends
    ) {
        this.checkDurationOverDay = checkDurationOverDay;
        this.checkDurationOverWeek = checkDurationOverWeek;
        this.checkDurationOverFortnight = checkDurationOverFortnight;
        this.checkDurationBetweenShifts = checkDurationBetweenShifts;
        this.checkLongDaysPerWeek = checkLongDaysPerWeek;
        this.checkConsecutiveDays = checkConsecutiveDays;
        this.checkConsecutiveNightsWorked = checkConsecutiveNightsWorked;
        this.checkRecoveryDaysFollowingNights = checkRecoveryDaysFollowingNights;
        this.allow1in2Weekends = allow1in2Weekends;
        this.checkFrequencyOfWeekends = checkFrequencyOfWeekends;
    }

    public final boolean checkDurationOverDay() {
        return checkDurationOverDay;
    }

    public final boolean checkDurationOverWeek() {
        return checkDurationOverWeek;
    }

    public final boolean checkDurationOverFortnight() {
        return checkDurationOverFortnight;
    }

    public final boolean checkDurationBetweenShifts() {
        return checkDurationBetweenShifts;
    }

    public final boolean checkLongDaysPerWeek() {
        return checkLongDaysPerWeek;
    }

    public final boolean checkConsecutiveDays() {
        return checkConsecutiveDays;
    }

    public final boolean checkConsecutiveNightsWorked() {
        return checkConsecutiveNightsWorked;
    }

    public final boolean checkRecoveryFollowingNights() {
        return checkRecoveryDaysFollowingNights;
    }

    public final boolean allow1in2Weekends() {
        return allow1in2Weekends;
    }

    public final boolean checkFrequencyOfWeekends() {
        return checkFrequencyOfWeekends;
    }
}
