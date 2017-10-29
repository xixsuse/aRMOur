package com.skepticalone.armour.data.compliance;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import java.util.List;

public final class RowDurationOverFortnight extends RowDurationOverPeriod {
    RowDurationOverFortnight(@NonNull Configuration configuration, @NonNull Shift.Data shiftData, @NonNull List<RosteredShift> previousShifts) {
        super(configuration.checkDurationOverFortnight(), shiftData, shiftData.getEnd().minusWeeks(2), previousShifts);
    }

    @Override
    int getMaximumHoursOverPeriod() {
        return AppConstants.MAXIMUM_HOURS_OVER_FORTNIGHT;
    }

    public static final class Binder extends RowDuration.Binder<RowDurationOverFortnight> {

        public Binder(@NonNull Callbacks callbacks, @NonNull RowDurationOverFortnight rowDurationOverFortnight) {
            super(callbacks, rowDurationOverFortnight);
        }

        @Override
        public int getPrimaryIcon() {
            return R.drawable.ic_weeks_black_24dp;
        }

        @NonNull
        @Override
        public String getFirstLine(@NonNull Context context) {
            return context.getString(R.string.duration_worked_over_fortnight);
        }

        @NonNull
        @Override
        String getMessage(@NonNull Context context) {
            return context.getString(R.string.meca_maximum_hours_over_fortnight, AppConstants.MAXIMUM_HOURS_OVER_FORTNIGHT);
        }
    }
}
