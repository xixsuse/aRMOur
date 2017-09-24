package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.Iterator;

public final class RosteredShift extends Shift {

    @Nullable
    private final ShiftData loggedShiftData;

    private Compliance compliance;

    RosteredShift(
            @NonNull RawRosteredShiftEntity rawShift,
            @NonNull ZoneId zoneId,
            @NonNull ShiftType.Configuration configuration
    ) {
        super(rawShift, zoneId, configuration);
        loggedShiftData = rawShift.getLoggedShiftData() == null ? null : new ShiftData(rawShift.getLoggedShiftData(), zoneId);
    }

    public void setCompliance(@NonNull Configuration configuration, @NonNull Iterable<ShiftData> previousShiftsExclusive, @Nullable LocalDate lastElapsedWeekendWorked, @Nullable LocalDate lastWeekendProcessed) {
        compliance = new Compliance(configuration, previousShiftsExclusive, lastElapsedWeekendWorked, lastWeekendProcessed);
    }

    @Nullable
    public ShiftData getLoggedShiftData() {
        return loggedShiftData;
    }

    public Compliance getCompliance() {
        return compliance;
    }

    final class Compliance {

        @NonNull
        private final Duration durationOverDay, durationOverWeek, durationOverFortnight;
        @Nullable
        private final Duration durationBetweenShifts;
        @Nullable
        private final LocalDate currentWeekend, lastWeekendWorked;
        private final boolean
                exceedsMaximumDurationOverDay,
                exceedsMaximumDurationOverWeek,
                exceedsMaximumDurationOverFortnight,
                insufficientDurationBetweenShifts,
                consecutiveWeekendsWorked,
                compliant;

        Compliance(@NonNull Configuration configuration, @NonNull Iterable<ShiftData> previousShiftsExclusive, @Nullable LocalDate lastElapsedWeekendWorked, @Nullable LocalDate lastWeekendProcessed) {
            durationOverDay = getDurationSince(getShiftData().getEnd().minusDays(1), previousShiftsExclusive);
            exceedsMaximumDurationOverDay = configuration.checkDurationOverDay && AppConstants.exceedsMaximumDurationOverDay(durationOverDay);
            durationOverWeek = getDurationSince(getShiftData().getEnd().minusWeeks(1), previousShiftsExclusive);
            exceedsMaximumDurationOverWeek = configuration.checkDurationOverWeek && AppConstants.exceedsMaximumDurationOverWeek(durationOverWeek);
            durationOverFortnight = getDurationSince(getShiftData().getEnd().minusWeeks(2), previousShiftsExclusive);
            exceedsMaximumDurationOverFortnight = configuration.checkDurationOverFortnight && AppConstants.exceedsMaximumDurationOverFortnight(durationOverFortnight);
            Iterator<ShiftData> iterator = previousShiftsExclusive.iterator();
            if (iterator.hasNext()) {
                durationBetweenShifts = Duration.between(iterator.next().getEnd(), getShiftData().getStart());
                insufficientDurationBetweenShifts = configuration.checkDurationBetweenShifts && AppConstants.insufficientDurationBetweenShifts(durationBetweenShifts);
            } else {
                durationBetweenShifts = null;
                insufficientDurationBetweenShifts = false;
            }
            final ZonedDateTime weekendStart = getShiftData().getStart().with(DayOfWeek.SATURDAY).with(LocalTime.MIN),
                    weekendEnd = weekendStart.plusDays(2);
            if (weekendStart.isBefore(getShiftData().getEnd()) && getShiftData().getStart().isBefore(weekendEnd)) {
                // I am working this weekend
                currentWeekend = weekendStart.toLocalDate();
                if (lastWeekendProcessed != null && !currentWeekend.isEqual(lastWeekendProcessed)) {
                    // the last weekend processed was different to this one
                    lastElapsedWeekendWorked = lastWeekendProcessed;
                }
                lastWeekendWorked = lastElapsedWeekendWorked;
                consecutiveWeekendsWorked = configuration.checkConsecutiveWeekends && lastWeekendWorked != null && !lastWeekendWorked.isEqual(currentWeekend) && lastWeekendWorked.isEqual(currentWeekend.minusWeeks(1));
                lastWeekendProcessed = currentWeekend;
            } else {
                currentWeekend = lastWeekendWorked = null;
                consecutiveWeekendsWorked = false;
            }
            compliant =
                    !exceedsMaximumDurationOverDay &&
                    !exceedsMaximumDurationOverWeek &&
                    !exceedsMaximumDurationOverFortnight &&
                    !insufficientDurationBetweenShifts &&
                    !consecutiveWeekendsWorked;
        }

        @NonNull
        Duration getDurationOverDay() {
            return durationOverDay;
        }
        boolean exceedsMaximumDurationOverDay() {
            return exceedsMaximumDurationOverDay;
        }
        @NonNull
        Duration getDurationOverWeek() {
            return durationOverWeek;
        }
        boolean exceedsMaximumDurationOverWeek() {
            return exceedsMaximumDurationOverWeek;
        }
        @NonNull
        Duration getDurationOverFortnight() {
            return durationOverFortnight;
        }
        boolean exceedsMaximumDurationOverFortnight() {
            return exceedsMaximumDurationOverFortnight;
        }
        @Nullable
        Duration getDurationBetweenShifts() {
            return durationBetweenShifts;
        }
        boolean insufficientDurationBetweenShifts() {
            return insufficientDurationBetweenShifts;
        }
        @Nullable
        LocalDate getCurrentWeekend() {
            return currentWeekend;
        }
        @Nullable
        LocalDate getLastWeekendWorked() {
            return lastWeekendWorked;
        }
        boolean consecutiveWeekendsWorked() {
            return consecutiveWeekendsWorked;
        }
        boolean isCompliant() {
            return compliant;
        }

    }

    private static Duration getDurationSince(@NonNull ZonedDateTime cutOff, @NonNull Iterable<ShiftData> previousShiftsInclusive) {
        Duration totalDuration = Duration.ZERO;
        for (ShiftData shift : previousShiftsInclusive) {
            if (!shift.getEnd().isAfter(cutOff)) break;
            totalDuration = totalDuration.plus(shift.getStart().isBefore(cutOff) ? Duration.between(cutOff, shift.getEnd()) : shift.getDuration());
        }
        return totalDuration;
    }

    static final class Configuration {

        private final boolean
                checkDurationOverDay,
                checkDurationOverWeek,
                checkDurationOverFortnight,
                checkDurationBetweenShifts,
                checkConsecutiveWeekends;

        Configuration(
                boolean checkDurationOverDay,
                boolean checkDurationOverWeek,
                boolean checkDurationOverFortnight,
                boolean checkDurationBetweenShifts,
                boolean checkConsecutiveWeekends
        ) {
            this.checkDurationOverDay = checkDurationOverDay;
            this.checkDurationOverWeek = checkDurationOverWeek;
            this.checkDurationOverFortnight = checkDurationOverFortnight;
            this.checkDurationBetweenShifts = checkDurationBetweenShifts;
            this.checkConsecutiveWeekends = checkConsecutiveWeekends;
        }
    }

//
//        @SuppressWarnings("ConstantConditions")
//        @Override
//        public void process(@NonNull List<RawRosteredShiftEntity> shifts, @NonNull ZoneId zoneId) {
//            @Nullable LocalDate lastElapsedWeekendWorked = null;
//            @Nullable LocalDate lastWeekendProcessed = null;
//            for (int currentIndex = 0, count = shifts.size(); currentIndex < count; currentIndex++) {
//                final RawRosteredShiftEntity shift = shifts.get(currentIndex);
//                final ZonedDateTime start = shift.getShiftData().getStart().atZone(zoneId),
//                        end = shift.getShiftData().getEnd().atZone(zoneId);
////                shift.durationOverDay = getDurationSince(shifts, currentIndex, end.minusDays(1).toInstant());
////                shift.exceedsMaximumDurationOverDay = checkDurationOverDay && AppConstants.exceedsMaximumDurationOverDay(shift.durationOverDay);
////                shift.durationOverWeek = getDurationSince(shifts, currentIndex, end.minusWeeks(1).toInstant());
////                shift.exceedsMaximumDurationOverWeek = checkDurationOverWeek && AppConstants.exceedsMaximumDurationOverWeek(shift.durationOverWeek);
////                shift.durationOverFortnight = getDurationSince(shifts, currentIndex, end.minusWeeks(2).toInstant());
////                shift.exceedsMaximumDurationOverFortnight = checkDurationOverFortnight && AppConstants.exceedsMaximumDurationOverFortnight(shift.durationOverFortnight);
////                if (currentIndex == 0) {
////                    shift.durationBetweenShifts = null;
////                    shift.insufficientDurationBetweenShifts = false;
////                } else {
////                    shift.durationBetweenShifts = Duration.between(shifts.get(currentIndex - 1).getShiftData().getEnd(), shift.getShiftData().getStart());
////                    shift.insufficientDurationBetweenShifts = checkDurationBetweenShifts && AppConstants.insufficientDurationBetweenShifts(shift.durationBetweenShifts);
////                }
//                final ZonedDateTime weekendStart = start.with(DayOfWeek.SATURDAY).with(LocalTime.MIN),
//                        weekendEnd = weekendStart.plusDays(2);
//                if (weekendStart.isBefore(end) && start.isBefore(weekendEnd)) {
//                    // I am working this weekend
//                    shift.currentWeekend = weekendStart.toLocalDate();
//                    if (lastWeekendProcessed != null && !shift.currentWeekend.isEqual(lastWeekendProcessed)) {
//                        // the last weekend processed was different to this one
//                        lastElapsedWeekendWorked = lastWeekendProcessed;
//                    }
//                    shift.lastWeekendWorked = lastElapsedWeekendWorked;
//                    shift.consecutiveWeekendsWorked = checkConsecutiveWeekends && shift.lastWeekendWorked != null && !shift.lastWeekendWorked.isEqual(shift.currentWeekend) && shift.lastWeekendWorked.isEqual(shift.currentWeekend.minusWeeks(1));
//                    lastWeekendProcessed = shift.currentWeekend;
//                } else {
//                    shift.currentWeekend = shift.lastWeekendWorked = null;
//                    shift.consecutiveWeekendsWorked = false;
//                }
//                shift.compliant =
//                        !shift.exceedsMaximumDurationOverDay &&
//                                !shift.exceedsMaximumDurationOverWeek &&
//                                !shift.exceedsMaximumDurationOverFortnight &&
//                                !shift.insufficientDurationBetweenShifts &&
//                                !shift.consecutiveWeekendsWorked;
//            }
//        }
//    }
}
