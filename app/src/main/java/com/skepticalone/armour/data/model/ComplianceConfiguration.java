package com.skepticalone.armour.data.model;

public class ComplianceConfiguration {

    private final boolean
            checkDurationOverDay,
            checkDurationOverWeek,
            checkDurationOverFortnight,
            checkDurationBetweenShifts,
            checkLongDaysPerWeek,
            checkConsecutiveDays,
            checkConsecutiveNights,
            checkRecoveryFollowingNights,
            _1in3Weekends,
            checkConsecutiveWeekends;

    ComplianceConfiguration(
            boolean _1in3Weekends,
            boolean checkDurationBetweenShifts,
            boolean checkDurationOverDay,
            boolean checkDurationOverWeek,
            boolean checkDurationOverFortnight,
            boolean checkLongDaysPerWeek,
            boolean checkConsecutiveDays,
            boolean checkConsecutiveWeekends,
            boolean checkConsecutiveNights,
            boolean checkRecoveryFollowingNights
    ) {
        this._1in3Weekends = _1in3Weekends;
        this.checkDurationBetweenShifts = checkDurationBetweenShifts;
        this.checkDurationOverDay = checkDurationOverDay;
        this.checkDurationOverWeek = checkDurationOverWeek;
        this.checkDurationOverFortnight = checkDurationOverFortnight;
        this.checkLongDaysPerWeek = checkLongDaysPerWeek;
        this.checkConsecutiveDays = checkConsecutiveDays;
        this.checkConsecutiveWeekends = checkConsecutiveWeekends;
        this.checkConsecutiveNights = checkConsecutiveNights;
        this.checkRecoveryFollowingNights = checkRecoveryFollowingNights;
    }

    final boolean _1in3Weekends() {
        return _1in3Weekends;
    }

    final boolean checkDurationBetweenShifts() {
        return checkDurationBetweenShifts;
    }

    final boolean checkDurationOverDay() {
        return checkDurationOverDay;
    }

    final boolean checkDurationOverWeek() {
        return checkDurationOverWeek;
    }

    final boolean checkDurationOverFortnight() {
        return checkDurationOverFortnight;
    }

    final boolean checkLongDaysPerWeek() {
        return checkLongDaysPerWeek;
    }

    final boolean checkConsecutiveDays() {
        return checkConsecutiveDays;
    }

    final boolean checkConsecutiveWeekends() {
        return checkConsecutiveWeekends;
    }

    final boolean checkConsecutiveNights() {
        return checkConsecutiveNights;
    }

    final boolean checkRecoveryFollowingNights() {
        return checkRecoveryFollowingNights;
    }

}
