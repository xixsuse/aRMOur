package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.TemporalAdjuster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.threeten.bp.DayOfWeek.FRIDAY;
import static org.threeten.bp.DayOfWeek.MONDAY;
import static org.threeten.bp.DayOfWeek.SATURDAY;
import static org.threeten.bp.DayOfWeek.SUNDAY;
import static org.threeten.bp.DayOfWeek.THURSDAY;
import static org.threeten.bp.DayOfWeek.TUESDAY;
import static org.threeten.bp.DayOfWeek.WEDNESDAY;
import static org.threeten.bp.temporal.TemporalAdjusters.next;
import static org.threeten.bp.temporal.TemporalAdjusters.previous;

final class RosteredDayOff {

    private final static TemporalAdjuster[] WEEKDAY_TEMPORAL_ADJUSTERS = {
            previous(MONDAY),
            previous(TUESDAY),
            previous(WEDNESDAY),
            previous(THURSDAY),
            previous(FRIDAY),
            next(MONDAY),
            next(TUESDAY),
            next(WEDNESDAY),
            next(THURSDAY),
            next(FRIDAY)
    };

    public static void process(@NonNull List<RosteredShift> rosteredShifts, @NonNull ConfigurationSaferRosters configuration) {
        Set<LocalDate> workedDates = new HashSet<>(rosteredShifts.size());
        int weekendShiftCount = 0;
        for (RosteredShift shift : rosteredShifts) {
            workedDates.add(shift.getShiftData().getStart().toLocalDate());
            if (shift.getCompliance().getNight() != null) {
                workedDates.add(shift.getShiftData().getEnd().toLocalDate());
            } else if (shift.getCompliance().getWeekend() != null) {
                weekendShiftCount++;
            }
        }
        Set<LocalDate> weekdayRosteredDaysOff = new HashSet<>(weekendShiftCount);
        for (RosteredShift shift : rosteredShifts) {
            assignRosteredDayOff(shift, workedDates, weekdayRosteredDaysOff, configuration);
        }
    }

    private static void assignRosteredDayOff(@NonNull RosteredShift shift, @NonNull Set<LocalDate> workedDates, @NonNull Set<LocalDate> weekdayRosteredDaysOff, @NonNull ConfigurationSaferRosters configuration) {
        if (shift.getCompliance().getNight() == null && shift.getCompliance().getWeekend() != null) {
            shift.getCompliance().setRosteredDayOff(new ComplianceDataRosteredDayOff(configuration, getRosteredDayOff(shift.getShiftData().getStart().toLocalDate(), workedDates, weekdayRosteredDaysOff, configuration.allowMidweekRDOs())));
        }
    }

    @Nullable
    private static LocalDate getRosteredDayOff(@NonNull final LocalDate weekendShiftDate, @NonNull Set<LocalDate> workedDates, @NonNull Set<LocalDate> weekdayRosteredDaysOff, boolean allowMidweekRDOs) {
        for (TemporalAdjuster weekdayTemporalAdjuster : WEEKDAY_TEMPORAL_ADJUSTERS) {
            LocalDate proposedWeekdayRdo = weekendShiftDate.with(weekdayTemporalAdjuster);
            if (workedDates.contains(proposedWeekdayRdo)) continue;
            if (allowMidweekRDOs ? isSingletonRDO(proposedWeekdayRdo, workedDates) : isSeparatedFromWeekend(proposedWeekdayRdo, workedDates))
                continue;
            if (weekdayRosteredDaysOff.add(proposedWeekdayRdo)) return proposedWeekdayRdo;
        }
        return null;
    }

    private static boolean isSeparatedFromWeekend(@NonNull LocalDate proposedWeekdayRdo, @NonNull Set<LocalDate> workedDates) {
        return isSeparatedFromPreviousWeekend(proposedWeekdayRdo, workedDates) && isSeparatedFromNextWeekend(proposedWeekdayRdo, workedDates);
    }

    private static boolean isSeparatedFromPreviousWeekend(@NonNull LocalDate proposedWeekdayRdo, @NonNull Set<LocalDate> workedDates) {
        for (LocalDate currentDay = proposedWeekdayRdo.minusDays(1); currentDay.getDayOfWeek() != SATURDAY; currentDay = currentDay.minusDays(1)) {
            if (workedDates.contains(currentDay)) return true;
        }
        return false;
    }

    private static boolean isSeparatedFromNextWeekend(@NonNull LocalDate proposedWeekdayRdo, @NonNull Set<LocalDate> workedDates) {
        for (LocalDate currentDay = proposedWeekdayRdo.plusDays(1); currentDay.getDayOfWeek() != SUNDAY; currentDay = currentDay.plusDays(1)) {
            if (workedDates.contains(currentDay)) return true;
        }
        return false;
    }

    private static boolean isSingletonRDO(@NonNull LocalDate proposedWeekdayRdo, @NonNull Set<LocalDate> workedDates) {
        return workedDates.contains(proposedWeekdayRdo.minusDays(1)) && workedDates.contains(proposedWeekdayRdo.plusDays(1));
    }

}
