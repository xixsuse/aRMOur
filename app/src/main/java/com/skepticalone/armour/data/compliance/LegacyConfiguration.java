package com.skepticalone.armour.data.compliance;

public final class LegacyConfiguration extends Configuration {
    private final boolean checkConsecutiveWeekends;

    LegacyConfiguration(
            boolean checkDurationOverDay, boolean checkDurationOverWeek, boolean checkDurationOverFortnight, boolean checkDurationBetweenShifts, boolean checkLongDaysPerWeek, boolean checkConsecutiveDays, boolean checkConsecutiveNightsWorked, boolean checkRecoveryDaysFollowingNights,
            boolean checkConsecutiveWeekends
    ) {
        super(checkDurationOverDay, checkDurationOverWeek, checkDurationOverFortnight, checkDurationBetweenShifts, checkLongDaysPerWeek, checkConsecutiveDays, checkConsecutiveNightsWorked, checkRecoveryDaysFollowingNights);
        this.checkConsecutiveWeekends = checkConsecutiveWeekends;
    }

    public final boolean checkConsecutiveWeekends() {
        return checkConsecutiveWeekends;
    }

}
