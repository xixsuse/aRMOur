package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.Duration;

import java.util.List;

final class RowDurationBetweenShifts extends Row {

    @NonNull
    private final Duration duration;

    private RowDurationBetweenShifts(boolean isChecked, @NonNull Shift.Data shift, @NonNull Shift.Data previousShift) {
        super(isChecked);
        duration = Duration.between(previousShift.getEnd(), shift.getStart());
    }

    @Nullable
    static RowDurationBetweenShifts from(@NonNull Configuration configuration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        return previousShifts.isEmpty() ? null : new RowDurationBetweenShifts(configuration.checkDurationBetweenShifts(), shift, previousShifts.get(previousShifts.size() - 1).getShiftData());
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
