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

public final class ComplianceDataWeekend extends ComplianceData {

    @NonNull
    private final List<LocalDate> weekendsWorkedInPeriod = new ArrayList<>();
    private final int periodInWeeks;
    private final boolean saferRosters;
    private final boolean includesConsecutiveWeekends;

    private ComplianceDataWeekend(@NonNull Configuration configuration, @NonNull LocalDate currentWeekend, @NonNull List<RosteredShift> previousShifts) {
        super(configuration.checkFrequencyOfWeekends());
        if (configuration instanceof ConfigurationSaferRosters) {
            saferRosters = true;
            ConfigurationSaferRosters configurationSaferRosters = (ConfigurationSaferRosters) configuration;
            periodInWeeks = configurationSaferRosters.allow1in2Weekends() ? (configurationSaferRosters.allowFrequentConsecutiveWeekends() ? 5 : 6) : (configurationSaferRosters.allowFrequentConsecutiveWeekends() ? 8 : 9);
        } else {
            saferRosters = false;
            periodInWeeks = configuration.allow1in2Weekends() ? AppConstants.MAXIMUM_CONSECUTIVE_WEEKEND_FREQUENCY_PERIOD_IN_WEEKS_LENIENT : AppConstants.MAXIMUM_CONSECUTIVE_WEEKEND_FREQUENCY_PERIOD_IN_WEEKS_STRICT;
        }
        weekendsWorkedInPeriod.add(currentWeekend);
        boolean consecutiveWeekends = false;
        for (int i = previousShifts.size() - 1; i >= 0; i--) {
            ComplianceDataWeekend weekend = previousShifts.get(i).getCompliance().getWeekend();
            if (weekend != null) {
                LocalDate cutOff = currentWeekend.minusWeeks(periodInWeeks);
                for (LocalDate weekendWorked : weekend.weekendsWorkedInPeriod) {
                    if (!weekendWorked.isAfter(cutOff)) break;
                    else if (currentWeekend.isEqual(weekendWorked)) continue;
                    consecutiveWeekends |= currentWeekend.minusWeeks(1).isEqual(weekendWorked);
                    weekendsWorkedInPeriod.add(weekendWorked);
                }
                break;
            }
        }
        includesConsecutiveWeekends = consecutiveWeekends;
    }

    @Nullable
    static ComplianceDataWeekend from(@NonNull Configuration configuration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        LocalDate currentWeekend = calculateCurrentWeekend(shift);
        return currentWeekend == null ? null : new ComplianceDataWeekend(configuration, currentWeekend, previousShifts);
    }

    @Nullable
    private static LocalDate calculateCurrentWeekend(@NonNull Shift.Data shift) {
        ZonedDateTime weekendStart = shift.getStart().with(DayOfWeek.SATURDAY).with(LocalTime.MIN);
        return weekendStart.isBefore(shift.getEnd()) && shift.getStart().isBefore(weekendStart.plusDays(2)) ? weekendStart.toLocalDate() : null;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return saferRosters ? weekendsWorkedInPeriod.size() <= 2 || !includesConsecutiveWeekends : weekendsWorkedInPeriod.size() <= 1;
    }

    public final int getPeriodInWeeks() {
        return periodInWeeks;
    }

    public boolean saferRosters() {
        return saferRosters;
    }

    public boolean includesConsecutiveWeekends() {
        return includesConsecutiveWeekends;
    }

    @NonNull
    public final List<LocalDate> getWeekendsWorkedInPeriod() {
        return weekendsWorkedInPeriod;
    }

}
