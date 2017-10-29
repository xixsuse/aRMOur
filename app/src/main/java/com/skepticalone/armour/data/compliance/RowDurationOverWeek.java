package com.skepticalone.armour.data.compliance;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import java.util.List;

public final class RowDurationOverWeek extends RowDurationOverPeriod {
    RowDurationOverWeek(@NonNull Configuration configuration, @NonNull Shift.Data shiftData, @NonNull List<RosteredShift> previousShifts) {
        super(configuration.checkDurationOverWeek(), shiftData, shiftData.getEnd().minusWeeks(1), previousShifts);
    }

    @Override
    int getMaximumHoursOverPeriod() {
        return AppConstants.MAXIMUM_HOURS_OVER_WEEK;
    }

    public static final class Binder extends RowDuration.Binder<RowDurationOverWeek> {

        public Binder(@NonNull Callbacks callbacks, @NonNull RowDurationOverWeek rowDurationOverWeek) {
            super(callbacks, rowDurationOverWeek);
        }

        @Override
        public int getPrimaryIcon() {
            return R.drawable.ic_week_black_24dp;
        }

        @NonNull
        @Override
        public String getFirstLine(@NonNull Context context) {
            return context.getString(R.string.duration_worked_over_week);
        }

        @NonNull
        @Override
        String getMessage(@NonNull Context context) {
            return context.getString(R.string.meca_maximum_hours_over_week, AppConstants.MAXIMUM_HOURS_OVER_WEEK);
        }
    }
}
