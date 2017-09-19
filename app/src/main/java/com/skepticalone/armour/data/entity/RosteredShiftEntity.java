package com.skepticalone.armour.data.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

@Entity(tableName = Contract.RosteredShifts.TABLE_NAME, indices = {@Index(value = {Contract.COLUMN_NAME_SHIFT_START}), @Index(value = {Contract.COLUMN_NAME_SHIFT_END})})
public class RosteredShiftEntity extends ShiftEntity implements RosteredShift {

    @Nullable
    @Embedded(prefix = Contract.RosteredShifts.LOGGED_PREFIX)
    private final ShiftData loggedShiftData;
    @Ignore
    private Duration durationOverDay, durationOverWeek, durationOverFortnight;
    @Nullable
    @Ignore
    private Duration durationBetweenShifts;
    @Nullable
    @Ignore
    private LocalDate currentWeekend, lastWeekendWorked;
    @Ignore
    private boolean
            exceedsMaximumDurationOverDay,
            exceedsMaximumDurationOverWeek,
            exceedsMaximumDurationOverFortnight,
            insufficientDurationBetweenShifts,
            consecutiveWeekendsWorked,
            compliant;

    public RosteredShiftEntity(
            @NonNull ShiftData shiftData,
            @Nullable ShiftData loggedShiftData,
            @Nullable String comment
    ) {
        super(shiftData, comment);
        this.loggedShiftData = loggedShiftData;
    }

    @DrawableRes
    public static int getComplianceIcon(boolean compliant) {
        return compliant ? R.drawable.compliant_black_24dp : R.drawable.non_compliant_red_24dp;
    }

    @Nullable
    @Override
    public ShiftData getLoggedShiftData() {
        return loggedShiftData;
    }

    @NonNull
    @Override
    public Duration getDurationOverDay() {
        return durationOverDay;
    }

    @NonNull
    @Override
    public Duration getDurationOverWeek() {
        return durationOverWeek;
    }

    @NonNull
    @Override
    public Duration getDurationOverFortnight() {
        return durationOverFortnight;
    }

    @Nullable
    @Override
    public Duration getDurationBetweenShifts() {
        return durationBetweenShifts;
    }

    @Override
    public boolean exceedsMaximumDurationOverDay() {
        return exceedsMaximumDurationOverDay;
    }

    @Override
    public boolean exceedsMaximumDurationOverWeek() {
        return exceedsMaximumDurationOverWeek;
    }

    @Override
    public boolean exceedsMaximumDurationOverFortnight() {
        return exceedsMaximumDurationOverFortnight;
    }

    @Override
    public boolean insufficientDurationBetweenShifts() {
        return insufficientDurationBetweenShifts;
    }

    @Nullable
    @Override
    public LocalDate getCurrentWeekend() {
        return currentWeekend;
    }

    @Nullable
    @Override
    public LocalDate getLastWeekendWorked() {
        return lastWeekendWorked;
    }

    @Override
    public boolean consecutiveWeekendsWorked() {
        return consecutiveWeekendsWorked;
    }

    @Override
    public boolean isCompliant() {
        return compliant;
    }

    @NonNull
    RosteredShiftEntity copy() {
        RosteredShiftEntity shift = new RosteredShiftEntity(getShiftData(), loggedShiftData, getComment());
        shift.setId(getId());
        return shift;
    }

//    public static final class ComplianceChecker implements Function<List<RosteredShiftEntity>, List<RosteredShiftEntity>> {
//
//        private final boolean
//                checkDurationOverDay,
//                checkDurationOverWeek,
//                checkDurationOverFortnight,
//                checkDurationBetweenShifts,
//                checkConsecutiveWeekends;
//
//        public ComplianceChecker(
//                boolean checkDurationOverDay,
//                boolean checkDurationOverWeek,
//                boolean checkDurationOverFortnight,
//                boolean checkDurationBetweenShifts,
//                boolean checkConsecutiveWeekends
//        ) {
//            this.checkDurationOverDay = checkDurationOverDay;
//            this.checkDurationOverWeek = checkDurationOverWeek;
//            this.checkDurationOverFortnight = checkDurationOverFortnight;
//            this.checkDurationBetweenShifts = checkDurationBetweenShifts;
//            this.checkConsecutiveWeekends = checkConsecutiveWeekends;
//        }
//
//        private static Duration getDurationSince(@NonNull List<RosteredShiftEntity> shifts, int currentIndex, @NonNull ReadableInstant cutOff) {
//            Duration totalDuration = Duration.ZERO;
//            do {
//                RosteredShiftEntity shift = shifts.get(currentIndex);
//                if (!shift.shiftData.end.isAfter(cutOff)) break;
//                totalDuration = totalDuration.plus(shift.shiftData.start.isBefore(cutOff) ? new Duration(cutOff, shift.shiftData.end) : shift.shiftData.getDuration());
//            } while (--currentIndex >= 0);
//            return totalDuration;
//        }
//
//        @SuppressWarnings("ConstantConditions")
//        @Override
//        public List<RosteredShiftEntity> apply(List<RosteredShiftEntity> shifts) {
//            Log.d("Checker", "checking...");
//            @Nullable LocalDate lastElapsedWeekendWorked = null;
//            @Nullable LocalDate lastWeekendProcessed = null;
//            for (int currentIndex = 0, count = shifts.size(); currentIndex < count; currentIndex++) {
//                RosteredShiftEntity shift = shifts.get(currentIndex);
//                shift.durationOverDay = getDurationSince(shifts, currentIndex, shift.shiftData.end.minusDays(1));
//                shift.exceedsMaximumDurationOverDay = checkDurationOverDay && AppConstants.exceedsMaximumDurationOverDay(shift.durationOverDay);
//                shift.durationOverWeek = getDurationSince(shifts, currentIndex, shift.shiftData.end.minusWeeks(1));
//                shift.exceedsMaximumDurationOverWeek = checkDurationOverWeek && AppConstants.exceedsMaximumDurationOverWeek(shift.durationOverWeek);
//                shift.durationOverFortnight = getDurationSince(shifts, currentIndex, shift.shiftData.end.minusWeeks(2));
//                shift.exceedsMaximumDurationOverFortnight = checkDurationOverFortnight && AppConstants.exceedsMaximumDurationOverFortnight(shift.durationOverFortnight);
//                shift.durationBetweenShifts = currentIndex == 0 ? null : new Duration(shifts.get(currentIndex - 1).shiftData.end, shift.shiftData.start);
//                shift.insufficientDurationBetweenShifts = shift.durationBetweenShifts != null && checkDurationBetweenShifts && AppConstants.insufficientDurationBetweenShifts(shift.durationBetweenShifts);
//                DateTime weekendStart = shift.shiftData.start.withDayOfWeek(DateTimeConstants.SATURDAY).withTimeAtStartOfDay();
//                if (weekendStart.isBefore(shift.shiftData.end) && shift.shiftData.start.isBefore(weekendStart.plusDays(2))) {
//                    // I am working this weekend
//                    shift.currentWeekend = weekendStart.toLocalDate();
//                    if (lastWeekendProcessed != null && !shift.currentWeekend.isEqual(lastWeekendProcessed)) {
//                        // the last weekend processed was different to this one
//                        lastElapsedWeekendWorked = lastWeekendProcessed;
//                    }
//                    shift.lastWeekendWorked = lastElapsedWeekendWorked;
//                    //noinspection ConstantConditions
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
//            return shifts;
//        }
//    }

    static final class ComplianceConfiguration implements ComplianceChecker {

        private final boolean
                checkDurationOverDay,
                checkDurationOverWeek,
                checkDurationOverFortnight,
                checkDurationBetweenShifts,
                checkConsecutiveWeekends;

        ComplianceConfiguration(
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

        private static Duration getDurationSince(@NonNull List<RosteredShiftEntity> shifts, int currentIndex, @NonNull Instant cutOff) {
            Duration totalDuration = Duration.ZERO;
            do {
                ShiftData shiftData = shifts.get(currentIndex).getShiftData();
                if (!shiftData.getEnd().isAfter(cutOff)) break;
                totalDuration = totalDuration.plus(Duration.between(shiftData.getStart().isBefore(cutOff) ? cutOff : shiftData.getStart(), shiftData.getEnd()));
            } while (--currentIndex >= 0);
            return totalDuration;
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public void process(@NonNull List<RosteredShiftEntity> shifts, @NonNull ZoneId zoneId) {
            @Nullable LocalDate lastElapsedWeekendWorked = null;
            @Nullable LocalDate lastWeekendProcessed = null;
            for (int currentIndex = 0, count = shifts.size(); currentIndex < count; currentIndex++) {
                final RosteredShiftEntity shift = shifts.get(currentIndex);
                final ZonedDateTime start = shift.getShiftData().getStart().atZone(zoneId),
                        end = shift.getShiftData().getEnd().atZone(zoneId);
                shift.durationOverDay = getDurationSince(shifts, currentIndex, end.minusDays(1).toInstant());
                shift.exceedsMaximumDurationOverDay = checkDurationOverDay && AppConstants.exceedsMaximumDurationOverDay(shift.durationOverDay);
                shift.durationOverWeek = getDurationSince(shifts, currentIndex, end.minusWeeks(1).toInstant());
                shift.exceedsMaximumDurationOverWeek = checkDurationOverWeek && AppConstants.exceedsMaximumDurationOverWeek(shift.durationOverWeek);
                shift.durationOverFortnight = getDurationSince(shifts, currentIndex, end.minusWeeks(2).toInstant());
                shift.exceedsMaximumDurationOverFortnight = checkDurationOverFortnight && AppConstants.exceedsMaximumDurationOverFortnight(shift.durationOverFortnight);
                if (currentIndex == 0) {
                    shift.durationBetweenShifts = null;
                    shift.insufficientDurationBetweenShifts = false;
                } else {
                    shift.durationBetweenShifts = Duration.between(shifts.get(currentIndex - 1).getShiftData().getEnd(), shift.getShiftData().getStart());
                    shift.insufficientDurationBetweenShifts = checkDurationBetweenShifts && AppConstants.insufficientDurationBetweenShifts(shift.durationBetweenShifts);
                }
                final ZonedDateTime weekendStart = start.with(DayOfWeek.SATURDAY).with(LocalTime.MIN),
                        weekendEnd = weekendStart.plusDays(2);
                if (weekendStart.isBefore(end) && start.isBefore(weekendEnd)) {
                    // I am working this weekend
                    shift.currentWeekend = weekendStart.toLocalDate();
                    if (lastWeekendProcessed != null && !shift.currentWeekend.isEqual(lastWeekendProcessed)) {
                        // the last weekend processed was different to this one
                        lastElapsedWeekendWorked = lastWeekendProcessed;
                    }
                    shift.lastWeekendWorked = lastElapsedWeekendWorked;
                    shift.consecutiveWeekendsWorked = checkConsecutiveWeekends && shift.lastWeekendWorked != null && !shift.lastWeekendWorked.isEqual(shift.currentWeekend) && shift.lastWeekendWorked.isEqual(shift.currentWeekend.minusWeeks(1));
                    lastWeekendProcessed = shift.currentWeekend;
                } else {
                    shift.currentWeekend = shift.lastWeekendWorked = null;
                    shift.consecutiveWeekendsWorked = false;
                }
                shift.compliant =
                        !shift.exceedsMaximumDurationOverDay &&
                                !shift.exceedsMaximumDurationOverWeek &&
                                !shift.exceedsMaximumDurationOverFortnight &&
                                !shift.insufficientDurationBetweenShifts &&
                                !shift.consecutiveWeekendsWorked;
            }
        }
    }
}
