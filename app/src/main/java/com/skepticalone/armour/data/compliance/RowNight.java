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

public final class RowNight extends Row {

    private final int maximumConsecutiveNights, indexOfNightShift;

    RowNight(@NonNull Configuration configuration, @NonNull Shift.Data nightShift, @NonNull List<RosteredShift> previousShifts) {
        super(configuration.checkConsecutiveNightsWorked());
        maximumConsecutiveNights = configuration instanceof ConfigurationSaferRosters ? ((ConfigurationSaferRosters) configuration).allow5ConsecutiveNights() ? AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_NIGHTS_LENIENT : AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_NIGHTS_STRICT : AppConstants.MAXIMUM_CONSECUTIVE_NIGHTS;
        indexOfNightShift = calculateIndexOfNightShift(nightShift, previousShifts);
    }

    private static int calculateIndexOfNightShift(@NonNull Shift.Data nightShift, @NonNull List<RosteredShift> previousShifts) {
        if (!previousShifts.isEmpty()) {
            RosteredShift previousShift = previousShifts.get(previousShifts.size() - 1);
            RowNight previousShiftNights = previousShift.getCompliance().getNight();
            if (previousShiftNights != null) {
                LocalDate thisNightShiftDate = nightShift.getEnd().toLocalDate(), previousNightShiftDate = previousShift.getShiftData().getEnd().toLocalDate();
                if (thisNightShiftDate.equals(previousNightShiftDate)) {
                    return previousShiftNights.indexOfNightShift;
                } else if (previousNightShiftDate.plusDays(1).isEqual(thisNightShiftDate)) {
                    return previousShiftNights.indexOfNightShift + 1;
                }
            }
        }
        return 0;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return indexOfNightShift < maximumConsecutiveNights;
    }

    public final int getMaximumConsecutiveNights() {
        return maximumConsecutiveNights;
    }

    final int getIndexOfNightShift() {
        return indexOfNightShift;
    }

    public static final class Binder extends Row.Binder<RowNight> {

        public Binder(@NonNull Callbacks callbacks, @NonNull RowNight row) {
            super(callbacks, row);
        }

        @Override
        public int getPrimaryIcon() {
            return R.drawable.ic_consecutive_shifts_black_24dp;
        }

        @NonNull
        @Override
        public String getFirstLine(@NonNull Context context) {
            return context.getString(R.string.number_of_consecutive_nights_worked);
        }

        @Override
        public String getSecondLine(@NonNull Context context) {
            int nights = getRow().indexOfNightShift + 1;
            return context.getResources().getQuantityString(R.plurals.nights, nights, nights);
        }

        @NonNull
        @Override
        String getMessage(@NonNull Context context) {
            return context.getString(R.string.meca_maximum_consecutive_nights, getRow().maximumConsecutiveNights);
        }

        @Override
        public boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
            if (!super.areContentsTheSame(other)) return false;
            Binder newBinder = (Binder) other;
            return
                    getRow().indexOfNightShift == newBinder.getRow().indexOfNightShift &&
                            getRow().maximumConsecutiveNights == newBinder.getRow().maximumConsecutiveNights;
        }
    }
}
