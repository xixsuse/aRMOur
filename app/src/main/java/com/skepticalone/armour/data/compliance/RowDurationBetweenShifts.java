package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.Duration;

final class RowDurationBetweenShifts extends Row {

    @NonNull
    private final Duration duration;

    RowDurationBetweenShifts(boolean isChecked, @NonNull Shift.Data shift, @NonNull Shift.Data previousShift) {
        super(isChecked);
        duration = Duration.between(previousShift.getEnd(), shift.getStart());
    }

    @NonNull
    public final Duration getDuration() {
        return duration;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return duration.compareTo(Duration.ofHours(AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS)) != -1;
    }

}
