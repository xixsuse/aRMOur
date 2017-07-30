package com.skepticalone.mecachecker.data.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.db.Contract;
import com.skepticalone.mecachecker.data.model.RosteredShift;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.util.AppConstants;

import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.ReadableInstant;

import java.util.List;

@Entity(tableName = Contract.RosteredShifts.TABLE_NAME, indices = {@Index(name = Contract.RosteredShifts.INDEX, value = {Contract.COLUMN_NAME_SHIFT_START})})
public final class RosteredShiftEntity extends ItemEntity implements RosteredShift {

    @NonNull
    @Embedded
    private final ShiftData shiftData;
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
            @SuppressWarnings("SameParameterValue") @Nullable ShiftData loggedShiftData,
            @SuppressWarnings("SameParameterValue") @Nullable String comment
    ) {
        super(comment);
        this.shiftData = shiftData;
        this.loggedShiftData = loggedShiftData;
    }

    @NonNull
    @Override
    public ShiftData getShiftData() {
        return shiftData;
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

    @DrawableRes
    public int getComplianceIcon(){
        return getComplianceIcon(compliant);
    }

    @DrawableRes
    public static int getComplianceIcon(boolean compliant) {
        return compliant ? R.drawable.ic_check_black_24dp : R.drawable.ic_cancel_red_24dp;
    }

    private static Duration getDurationSince(@NonNull List<RosteredShiftEntity> shifts, int currentIndex, @NonNull ReadableInstant cutOff) {
        Duration totalDuration = Duration.ZERO;
        do {
            RosteredShiftEntity shift = shifts.get(currentIndex);
            if (!shift.shiftData.getEnd().isAfter(cutOff)) break;
            totalDuration = totalDuration.plus(shift.shiftData.getStart().isBefore(cutOff) ? new Duration(cutOff, shift.shiftData.getEnd()) : shift.shiftData.getDuration());
        } while (--currentIndex >= 0);
        return totalDuration;
    }

    private void setupWeekends(@NonNull List<RosteredShiftEntity> shifts, int currentIndex) {
        currentWeekend = shiftData.getWeekend();
        if (currentWeekend != null) {
            LocalDate previousWeekend = currentWeekend.minusWeeks(1);
            while (--currentIndex >= 0) {
                RosteredShiftEntity shift = shifts.get(currentIndex);
                LocalDate weekendWorked = shift.shiftData.getWeekend();
                if (weekendWorked != null && !currentWeekend.isEqual(weekendWorked)) {
                    lastWeekendWorked = weekendWorked;
                    consecutiveWeekendsWorked = lastWeekendWorked.isEqual(previousWeekend);
                    return;
                }
            }
        }
        lastWeekendWorked = null;
        consecutiveWeekendsWorked = false;
    }

    public final void setup(@NonNull List<RosteredShiftEntity> shifts, int currentIndex) {
        durationOverDay = getDurationSince(shifts, currentIndex, shiftData.getEnd().minusDays(1));
        durationOverWeek = getDurationSince(shifts, currentIndex, shiftData.getEnd().minusWeeks(1));
        durationOverFortnight = getDurationSince(shifts, currentIndex, shiftData.getEnd().minusWeeks(1));
        durationBetweenShifts = currentIndex == 0 ? null : new Duration(shifts.get(currentIndex - 1).shiftData.getEnd(), shiftData.getStart());
        setupWeekends(shifts, currentIndex);
        exceedsMaximumDurationOverDay = AppConstants.exceedsMaximumDurationOverDay(durationOverDay);
        exceedsMaximumDurationOverWeek = AppConstants.exceedsMaximumDurationOverWeek(durationOverWeek);
        exceedsMaximumDurationOverFortnight = AppConstants.exceedsMaximumDurationOverFortnight(durationOverFortnight);
        insufficientDurationBetweenShifts = durationBetweenShifts != null && AppConstants.insufficientDurationBetweenShifts(durationBetweenShifts);
        compliant =
                !exceedsMaximumDurationOverDay &&
                !exceedsMaximumDurationOverWeek &&
                !exceedsMaximumDurationOverFortnight &&
                !insufficientDurationBetweenShifts &&
                !consecutiveWeekendsWorked;
    }
}
