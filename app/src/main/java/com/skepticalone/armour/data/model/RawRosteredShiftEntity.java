package com.skepticalone.armour.data.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.Contract;

@Entity(tableName = Contract.RosteredShifts.TABLE_NAME, indices = {@Index(value = {Contract.COLUMN_NAME_SHIFT_START}), @Index(value = {Contract.COLUMN_NAME_SHIFT_END})})
public final class RawRosteredShiftEntity extends RawShift {

    @Nullable
    @Embedded(prefix = Contract.RosteredShifts.LOGGED_PREFIX)
    private final ShiftData loggedShiftData;
//
//    @Ignore
//    private Duration durationOverDay, durationOverWeek, durationOverFortnight;
//    @Nullable
//    @Ignore
//    private Duration durationBetweenShifts;
//    @Nullable
//    @Ignore
//    private LocalDate currentWeekend, lastWeekendWorked;
//    @Ignore
//    private boolean
//            exceedsMaximumDurationOverDay,
//            exceedsMaximumDurationOverWeek,
//            exceedsMaximumDurationOverFortnight,
//            insufficientDurationBetweenShifts,
//            consecutiveWeekendsWorked,
//            compliant;

    RawRosteredShiftEntity(
            long id,
            @Nullable String comment,
            @NonNull ShiftData shiftData,
            @Nullable ShiftData loggedShiftData
    ) {
        super(id, comment, shiftData);
        this.loggedShiftData = loggedShiftData;
    }


    @Nullable
    public ShiftData getLoggedShiftData() {
        return loggedShiftData;
    }

//
//    public RawRosteredShiftEntity(
//            long id,
//            @NonNull RawShift.ShiftData shiftData,
//            @Nullable RawShift.ShiftData loggedShiftData,
//            @Nullable String comment
//    ) {
//        super(id, shiftData, comment);
//        this.loggedShiftData = loggedShiftData;
//    }
//
//    @DrawableRes
//    public static int getComplianceIcon(boolean compliant) {
//        return compliant ? R.drawable.compliant_black_24dp : R.drawable.non_compliant_red_24dp;
//    }
//
//    @NonNull
//    @Override
//    public Duration getDurationOverDay() {
//        return durationOverDay;
//    }
//
//    @NonNull
//    @Override
//    public Duration getDurationOverWeek() {
//        return durationOverWeek;
//    }
//
//    @NonNull
//    @Override
//    public Duration getDurationOverFortnight() {
//        return durationOverFortnight;
//    }
//
//    @Nullable
//    @Override
//    public Duration getDurationBetweenShifts() {
//        return durationBetweenShifts;
//    }
//
//    @Override
//    public boolean exceedsMaximumDurationOverDay() {
//        return exceedsMaximumDurationOverDay;
//    }
//
//    @Override
//    public boolean exceedsMaximumDurationOverWeek() {
//        return exceedsMaximumDurationOverWeek;
//    }
//
//    @Override
//    public boolean exceedsMaximumDurationOverFortnight() {
//        return exceedsMaximumDurationOverFortnight;
//    }
//
//    @Override
//    public boolean insufficientDurationBetweenShifts() {
//        return insufficientDurationBetweenShifts;
//    }
//
//    @Nullable
//    @Override
//    public LocalDate getCurrentWeekend() {
//        return currentWeekend;
//    }
//
//    @Nullable
//    @Override
//    public LocalDate getLastWeekendWorked() {
//        return lastWeekendWorked;
//    }
//
//    @Override
//    public boolean consecutiveWeekendsWorked() {
//        return consecutiveWeekendsWorked;
//    }
//
//    @Override
//    public boolean isCompliant() {
//        return compliant;
//    }
//
//    @NonNull
//    RawRosteredShiftEntity copy() {
//        return new RawRosteredShiftEntity(getId(), getShiftData(), loggedShiftData, getComment());
//    }
//
//    public static final class ComplianceConfig implements Function<List<RawRosteredShiftEntity>, List<RawRosteredShiftEntity>> {
//
//        private final boolean
//                checkDurationOverDay,
//                checkDurationOverWeek,
//                checkDurationOverFortnight,
//                checkDurationBetweenShifts,
//                checkConsecutiveWeekends;
//
//        public ComplianceConfig(
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
//        private static Duration getDurationSince(@NonNull List<RawRosteredShiftEntity> shifts, int currentIndex, @NonNull ReadableInstant cutOff) {
//            Duration totalDuration = Duration.ZERO;
//            do {
//                RawRosteredShiftEntity shift = shifts.get(currentIndex);
//                if (!shift.shiftData.end.isAfter(cutOff)) break;
//                totalDuration = totalDuration.plus(shift.shiftData.start.isBefore(cutOff) ? new Duration(cutOff, shift.shiftData.end) : shift.shiftData.getDuration());
//            } while (--currentIndex >= 0);
//            return totalDuration;
//        }
//
//        @SuppressWarnings("ConstantConditions")
//        @Override
//        public List<RawRosteredShiftEntity> apply(List<RawRosteredShiftEntity> shifts) {
//            Log.d("Checker", "checking...");
//            @Nullable LocalDate lastElapsedWeekendWorked = null;
//            @Nullable LocalDate lastWeekendProcessed = null;
//            for (int currentIndex = 0, count = shifts.size(); currentIndex < count; currentIndex++) {
//                RawRosteredShiftEntity shift = shifts.get(currentIndex);
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

}
