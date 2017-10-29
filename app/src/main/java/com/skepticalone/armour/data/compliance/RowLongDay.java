package com.skepticalone.armour.data.compliance;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemViewHolder;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.LocalDate;

import java.util.List;

public final class RowLongDay extends Row {

    private final int indexOfLongDay;

    RowLongDay(@NonNull Configuration configuration, @NonNull Shift.Data longDay, @NonNull List<RosteredShift> previousShifts) {
        super(configuration.checkLongDaysPerWeek());
        indexOfLongDay = calculateIndexOfLongDay(longDay, previousShifts);
    }

    private static int calculateIndexOfLongDay(@NonNull Shift.Data longDay, @NonNull List<RosteredShift> previousShifts) {
        int index = 0;
        if (!previousShifts.isEmpty()) {
            final LocalDate oneWeekAgo = longDay.getStart().toLocalDate().minusWeeks(1);
            for (int i = previousShifts.size() - 1; i >= 0; i--) {
                RosteredShift previousShift = previousShifts.get(i);
                if (!previousShift.getShiftData().getStart().toLocalDate().isAfter(oneWeekAgo)) {
                    break;
                } else if (previousShift.getCompliance().getLongDay() != null) {
                    index++;
                }
            }
        }
        return index;
    }

    @Override
    public boolean isCompliantIfChecked() {
        return indexOfLongDay < AppConstants.MAXIMUM_LONG_DAYS_PER_WEEK;
    }

    int getIndexOfLongDay() {
        return indexOfLongDay;
    }

    public static final class Binder extends Row.Binder<RowLongDay> {

        public Binder(@NonNull Callbacks callbacks, @NonNull RowLongDay row) {
            super(callbacks, row);
        }

        @Override
        public int getPrimaryIcon() {
            return R.drawable.ic_long_day_black_24dp;
        }

        @NonNull
        @Override
        public String getFirstLine(@NonNull Context context) {
            return context.getString(R.string.number_of_long_days_in_week);
        }

        @Override
        public String getSecondLine(@NonNull Context context) {
            int longDays = getRow().indexOfLongDay + 1;
            return context.getResources().getQuantityString(R.plurals.long_days, longDays, longDays);
        }

        @NonNull
        @Override
        String getMessage(@NonNull Context context) {
            return context.getString(R.string.meca_maximum_long_days_over_week, AppConstants.MAXIMUM_LONG_DAYS_PER_WEEK, AppConstants.MAXIMUM_HOURS_IN_NORMAL_DAY);
        }

        @Override
        public boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
            return super.areContentsTheSame(other) && getRow().indexOfLongDay == ((Binder) other).getRow().indexOfLongDay;
        }
    }
}
