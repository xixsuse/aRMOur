package com.skepticalone.armour.data.model;

public class Configuration {

    private final boolean
            checkDurationOverDay,
            checkDurationOverWeek,
            checkDurationOverFortnight,
            checkDurationBetweenShifts,
            checkLongDaysPerWeek,
            checkConsecutiveDays,
            checkConsecutiveNightsWorked,
            checkRecoveryFollowingNights,
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
            boolean checkRecoveryFollowingNights,
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
        this.checkRecoveryFollowingNights = checkRecoveryFollowingNights;
        this.allow1in2Weekends = allow1in2Weekends;
        this.checkFrequencyOfWeekends = checkFrequencyOfWeekends;
    }

    public final boolean checkDurationOverDay() {
        return checkDurationOverDay;
    }

    final boolean checkDurationOverWeek() {
        return checkDurationOverWeek;
    }

    final boolean checkDurationOverFortnight() {
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
        return checkRecoveryFollowingNights;
    }

    public final boolean allow1in2Weekends() {
        return allow1in2Weekends;
    }

    public final boolean checkFrequencyOfWeekends() {
        return checkFrequencyOfWeekends;
    }

}
