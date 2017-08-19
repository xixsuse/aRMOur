package com.skepticalone.armour.data.newData;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.Duration;
import org.joda.time.LocalDate;

public final class RosteredShift extends Shift {

    boolean
            exceedsMaximumDurationOverDay,
            exceedsMaximumDurationOverWeek,
            exceedsMaximumDurationOverFortnight,
            insufficientDurationBetweenShifts,
            consecutiveWeekendsWorked,
            compliant;
    Duration durationOverDay, durationOverWeek, durationOverFortnight;
    @Nullable
    Duration durationBetweenShifts;
    @Nullable
    LocalDate currentWeekend, lastWeekendWorked;

    private RosteredShift(long start, long end, @NonNull ShiftTypeCalculator shiftTypeCalculator) {
        super(start, end, shiftTypeCalculator);
    }

    static RosteredShift from(RawShift shift, @NonNull ShiftTypeCalculator shiftTypeCalculator) {
        return new RosteredShift(shift.start, shift.end, shiftTypeCalculator);
    }

    @Override
    public String toString() {
        return super.toString() + ", compliant: " + compliant;
    }

    //
//    RosteredShift(long start, long end, ) {
//        super(start, end);
//    }
//
//    public FullRosteredShift(@NonNull ShiftData shiftData, @Nullable ShiftData loggedShiftData, @Nullable String comment) {
//        super(shiftData, loggedShiftData, comment);
//    }

}
