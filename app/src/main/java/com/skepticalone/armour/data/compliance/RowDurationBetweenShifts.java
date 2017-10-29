package com.skepticalone.armour.data.compliance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.Duration;

import java.util.List;

public final class RowDurationBetweenShifts extends RowDuration {

    private RowDurationBetweenShifts(boolean isChecked, @NonNull Shift.Data shift, @NonNull Shift.Data previousShift) {
        super(isChecked, Duration.between(previousShift.getEnd(), shift.getStart()));
    }

    @Nullable
    static RowDurationBetweenShifts from(@NonNull Configuration configuration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        return previousShifts.isEmpty() ? null : new RowDurationBetweenShifts(configuration.checkDurationBetweenShifts(), shift, previousShifts.get(previousShifts.size() - 1).getShiftData());
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return getDuration().compareTo(Duration.ofHours(AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS)) != -1;
    }

    public static final class Binder extends RowDuration.Binder<RowDurationBetweenShifts> {

        public Binder(@NonNull Callbacks callbacks, @NonNull RowDurationBetweenShifts row) {
            super(callbacks, row);
        }

        @Override
        public int getPrimaryIcon() {
            return R.drawable.ic_sleep_black_24dp;
        }

        @NonNull
        @Override
        public String getFirstLine(@NonNull Context context) {
            return context.getString(R.string.duration_between_shifts);
        }

        @NonNull
        @Override
        String getMessage(@NonNull Context context) {
            return context.getString(R.string.meca_minimum_hours_between_shifts, AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS);
        }
    }
}
