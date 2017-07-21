package com.skepticalone.mecachecker.data.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.db.Contract;
import com.skepticalone.mecachecker.data.model.RosteredShift;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.util.AppConstants;

import org.joda.time.Duration;
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
    @Ignore
    private boolean
            exceedsMaximumDurationOverDay,
            exceedsMaximumDurationOverWeek,
            exceedsMaximumDurationOverFortnight,
            insufficientDurationBetweenShifts,
            compliant;

    public RosteredShiftEntity(
            @NonNull ShiftData shiftData,
            @Nullable ShiftData loggedShiftData,
            @Nullable String comment
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

    @Override
    public boolean isCompliant() {
        return compliant;
    }

    private static Duration getDurationSince(@NonNull List<RosteredShiftEntity> shifts, int currentIndex, @NonNull ReadableInstant cutOff) {
        Duration totalDuration = Duration.ZERO;
        do {
            RosteredShiftEntity shift = shifts.get(currentIndex);
            if (!shift.shiftData.getEnd().isAfter(cutOff)) break;
            totalDuration = totalDuration.plus(new Duration(shift.shiftData.getStart().isBefore(cutOff) ? cutOff : shift.shiftData.getStart(), shift.shiftData.getEnd()));
        } while (--currentIndex >= 0);
        return totalDuration;
    }

    public final void setup(@NonNull List<RosteredShiftEntity> shifts, int currentIndex) {
        durationOverDay = getDurationSince(shifts, currentIndex, shiftData.getEnd().minusDays(1));
        durationOverWeek = getDurationSince(shifts, currentIndex, shiftData.getEnd().minusWeeks(1));
        durationOverFortnight = getDurationSince(shifts, currentIndex, shiftData.getEnd().minusWeeks(1));
        durationBetweenShifts = currentIndex == 0 ? null : new Duration(shifts.get(currentIndex - 1).shiftData.getEnd(), shiftData.getStart());
        exceedsMaximumDurationOverDay = AppConstants.exceedsMaximumDurationOverDay(durationOverDay);
        exceedsMaximumDurationOverWeek = AppConstants.exceedsMaximumDurationOverWeek(durationOverWeek);
        exceedsMaximumDurationOverFortnight = AppConstants.exceedsMaximumDurationOverFortnight(durationOverFortnight);
        insufficientDurationBetweenShifts = durationBetweenShifts != null && AppConstants.insufficientDurationBetweenShifts(durationBetweenShifts);
        compliant =
                !exceedsMaximumDurationOverDay &&
                !exceedsMaximumDurationOverWeek &&
                !exceedsMaximumDurationOverFortnight &&
                !insufficientDurationBetweenShifts;
    }
}
