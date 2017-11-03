package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

public final class ComplianceDataConsecutiveWeekends extends ComplianceData {

    @NonNull
    private final List<LocalDate> weekendsWorkedInPeriod = new ArrayList<>();
    private final int periodInWeeks;
    private final boolean saferRosters;
    private final int consecutiveWeekendCount;

    private ComplianceDataConsecutiveWeekends(@NonNull ComplianceConfiguration complianceConfiguration, @NonNull LocalDate currentWeekend, @NonNull List<RosteredShift> previousShifts) {
        super(complianceConfiguration.checkConsecutiveWeekends());
        if (complianceConfiguration instanceof ComplianceConfigurationSaferRosters) {
            saferRosters = true;
            ComplianceConfigurationSaferRosters configurationSaferRosters = (ComplianceConfigurationSaferRosters) complianceConfiguration;
            periodInWeeks = configurationSaferRosters._1in3Weekends() ? (configurationSaferRosters.allowFrequentConsecutiveWeekends() ? AppConstants.SAFER_ROSTERS_CONSECUTIVE_WEEKEND_STRICT_PERIOD_IN_WEEKS_LENIENT : AppConstants.SAFER_ROSTERS_CONSECUTIVE_WEEKEND_STRICT_PERIOD_IN_WEEKS_STRICT) : (configurationSaferRosters.allowFrequentConsecutiveWeekends() ? AppConstants.SAFER_ROSTERS_CONSECUTIVE_WEEKEND_LENIENT_PERIOD_IN_WEEKS_LENIENT : AppConstants.SAFER_ROSTERS_CONSECUTIVE_WEEKEND_LENIENT_PERIOD_IN_WEEKS_STRICT);
        } else {
            saferRosters = false;
            periodInWeeks = complianceConfiguration._1in3Weekends() ? AppConstants.CONSECUTIVE_WEEKEND_STRICT_PERIOD_IN_WEEKS : AppConstants.CONSECUTIVE_WEEKEND_LENIENT_PERIOD_IN_WEEKS;
        }
        weekendsWorkedInPeriod.add(currentWeekend);
        int consecutiveWeekendCount = 0;
        for (int i = previousShifts.size() - 1; i >= 0; i--) {
            ComplianceDataConsecutiveWeekends weekend = previousShifts.get(i).getCompliance().getConsecutiveWeekends();
            if (weekend != null) {
                LocalDate cutOff = currentWeekend.minusWeeks(periodInWeeks);
                LocalDate lastWeekendProcessed = currentWeekend;
                for (int j = weekend.weekendsWorkedInPeriod.size() - 1; j >= 0; j--) {
                    LocalDate weekendWorked = weekend.weekendsWorkedInPeriod.get(j);
                    if (!weekendWorked.isAfter(cutOff)) break;
                    else if (lastWeekendProcessed.isEqual(weekendWorked)) continue;
                    else if (
//                            true
                            lastWeekendProcessed.minusWeeks(1).isEqual(weekendWorked)
                            ) consecutiveWeekendCount++;
                    weekendsWorkedInPeriod.add(0, weekendWorked);
                    lastWeekendProcessed = weekendWorked;
                }
                break;
            }
        }
        this.consecutiveWeekendCount = consecutiveWeekendCount;
    }

    @Nullable
    static ComplianceDataConsecutiveWeekends from(@NonNull ComplianceConfiguration complianceConfiguration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        LocalDate currentWeekend = calculateCurrentWeekend(shift);
        return currentWeekend == null ? null : new ComplianceDataConsecutiveWeekends(complianceConfiguration, currentWeekend, previousShifts);
    }

    @Nullable
    private static LocalDate calculateCurrentWeekend(@NonNull Shift.Data shift) {
        ZonedDateTime weekendStart = shift.getStart().with(DayOfWeek.SATURDAY).with(LocalTime.MIN);
        return weekendStart.isBefore(shift.getEnd()) && shift.getStart().isBefore(weekendStart.plusDays(2)) ? weekendStart.toLocalDate() : null;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return saferRosters ? (consecutiveWeekendCount == 0 || (consecutiveWeekendCount == 1 && weekendsWorkedInPeriod.size() == 2)) : (weekendsWorkedInPeriod.size() == 1);
    }

    public final int getPeriodInWeeks() {
        return periodInWeeks;
    }

    public boolean saferRosters() {
        return saferRosters;
    }

    @NonNull
    public final List<LocalDate> getWeekendsWorkedInPeriod() {
        return weekendsWorkedInPeriod;
    }

    int getConsecutiveWeekendCount() {
        return consecutiveWeekendCount;
    }
}
