package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RosteredShift;

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

public final class RDO {

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
    @NonNull
    private final Set<LocalDate> workedDates, weekdayRosteredDaysOff;
    @NonNull
    private final ConfigurationSaferRosters configuration;

    public RDO(@NonNull List<RosteredShift> rosteredShifts, @NonNull ConfigurationSaferRosters configuration) {
        this.configuration = configuration;
        workedDates = new HashSet<>(rosteredShifts.size());
        int weekendShiftCount = 0;
        for (RosteredShift shift : rosteredShifts) {
            workedDates.add(shift.getShiftData().getStart().toLocalDate());
            if (shift.getCompliance().getNight() != null) {
                workedDates.add(shift.getShiftData().getEnd().toLocalDate());
            } else if (shift.getCompliance().getWeekend() != null) {
                weekendShiftCount++;
            }
        }
        weekdayRosteredDaysOff = new HashSet<>(weekendShiftCount);
        for (RosteredShift shift : rosteredShifts) {
            assignRosteredDayOff(shift);
        }
    }

    private void assignRosteredDayOff(@NonNull RosteredShift shift) {
        if (shift.getCompliance().getNight() == null && shift.getCompliance().getWeekend() != null) {
            shift.getCompliance().setRosteredDayOff(new RowRosteredDayOff(configuration, getRosteredDayOff(shift.getShiftData().getStart().toLocalDate())));
        }
    }

    @Nullable
    private LocalDate getRosteredDayOff(@NonNull final LocalDate weekendShiftDate) {
        for (TemporalAdjuster weekdayTemporalAdjuster : WEEKDAY_TEMPORAL_ADJUSTERS) {
            LocalDate proposedWeekdayRdo = weekendShiftDate.with(weekdayTemporalAdjuster);
            if (workedDates.contains(proposedWeekdayRdo)) continue;
            if (configuration.allowMidweekRDOs() ? isSingletonRDO(proposedWeekdayRdo) : isSeparatedFromWeekend(proposedWeekdayRdo))
                continue;
            if (weekdayRosteredDaysOff.add(proposedWeekdayRdo)) return proposedWeekdayRdo;
        }
        return null;
    }

    private boolean isSeparatedFromWeekend(@NonNull LocalDate proposedWeekdayRdo) {
        return isSeparatedFromPreviousWeekend(proposedWeekdayRdo) && isSeparatedFromNextWeekend(proposedWeekdayRdo);
    }

    private boolean isSeparatedFromPreviousWeekend(@NonNull LocalDate proposedWeekdayRdo) {
        for (LocalDate currentDay = proposedWeekdayRdo.minusDays(1); currentDay.getDayOfWeek() != SATURDAY; currentDay = currentDay.minusDays(1)) {
            if (workedDates.contains(currentDay)) return true;
        }
        return false;
    }

    private boolean isSeparatedFromNextWeekend(@NonNull LocalDate proposedWeekdayRdo) {
        for (LocalDate currentDay = proposedWeekdayRdo.plusDays(1); currentDay.getDayOfWeek() != SUNDAY; currentDay = currentDay.plusDays(1)) {
            if (workedDates.contains(currentDay)) return true;
        }
        return false;
    }

    private boolean isSingletonRDO(@NonNull LocalDate proposedWeekdayRdo) {
        return workedDates.contains(proposedWeekdayRdo.minusDays(1)) && workedDates.contains(proposedWeekdayRdo.plusDays(1));
    }

    @NonNull
    Set<LocalDate> getWeekdayRosteredDaysOff() {
        return weekdayRosteredDaysOff;
    }

    @NonNull
    Set<LocalDate> getWorkedDates() {
        return workedDates;
    }
}
