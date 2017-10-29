package com.skepticalone.armour.data.compliance;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import java.util.List;

public final class RowDurationOverDay extends RowDurationOverPeriod {
    RowDurationOverDay(@NonNull Configuration configuration, @NonNull Shift.Data shiftData, @NonNull List<RosteredShift> previousShifts) {
        super(configuration.checkDurationOverDay(), shiftData, shiftData.getEnd().minusDays(1), previousShifts);
    }

    @Override
    int getMaximumHoursOverPeriod() {
        return AppConstants.MAXIMUM_HOURS_OVER_DAY;
    }

    public static final class Binder extends RowDuration.Binder<RowDurationOverDay> {

        public Binder(@NonNull Callbacks callbacks, @NonNull RowDurationOverDay rowDurationOverDay) {
            super(callbacks, rowDurationOverDay);
        }

        @Override
        public int getPrimaryIcon() {
            return R.drawable.ic_duration_black_24dp;
        }

        @NonNull
        @Override
        public String getFirstLine(@NonNull Context context) {
            return context.getString(R.string.duration_worked_over_day);
        }

        @NonNull
        @Override
        String getMessage(@NonNull Context context) {
            return context.getString(R.string.meca_maximum_hours_over_day, AppConstants.MAXIMUM_HOURS_OVER_DAY);
        }
    }
}
