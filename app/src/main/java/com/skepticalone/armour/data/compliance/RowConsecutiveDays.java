package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.LocalDate;

import java.util.List;

public final class RowConsecutiveDays extends Row {

    private final int maximumConsecutiveDays, indexOfDayShift;
    @StringRes
    private final int message;

    RowConsecutiveDays(@NonNull Configuration configuration, @NonNull Shift.Data dayShift, @NonNull List<RosteredShift> previousShifts) {
        super(configuration.checkConsecutiveDays());
        maximumConsecutiveDays = configuration instanceof ConfigurationSaferRosters ? AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_DAYS : AppConstants.MAXIMUM_CONSECUTIVE_DAYS;
        indexOfDayShift = calculateIndexOfDayShift(dayShift, previousShifts);
        message = configuration instanceof ConfigurationSaferRosters ? R.string.meca_safer_rosters_maximum_consecutive_days : R.string.meca_maximum_consecutive_days;
    }

    private static int calculateIndexOfDayShift(@NonNull Shift.Data dayShift, @NonNull List<RosteredShift> previousShifts) {
        if (!previousShifts.isEmpty()) {
            RosteredShift previousShift = previousShifts.get(previousShifts.size() - 1);
            RowConsecutiveDays previousShiftConsecutiveDays = previousShift.getCompliance().getConsecutiveDays();
            if (previousShiftConsecutiveDays != null) {
                LocalDate thisDayShiftDate = dayShift.getStart().toLocalDate(), previousDayShiftDate = previousShift.getShiftData().getStart().toLocalDate();
                if (thisDayShiftDate.equals(previousDayShiftDate)) {
                    return previousShiftConsecutiveDays.indexOfDayShift;
                } else if (previousDayShiftDate.plusDays(1).isEqual(thisDayShiftDate)) {
                    return previousShiftConsecutiveDays.indexOfDayShift + 1;
                }
            }
        }
        return 0;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return indexOfDayShift < maximumConsecutiveDays;
    }

    public final int getIndexOfDayShift() {
        return indexOfDayShift;
    }

    @StringRes
    public final int getMessage() {
        return message;
    }

    public final int getMaximumConsecutiveDays() {
        return maximumConsecutiveDays;
    }

    public final boolean isEqual(@NonNull RowConsecutiveDays other) {
        return
                indexOfDayShift == other.indexOfDayShift &&
                        maximumConsecutiveDays == other.maximumConsecutiveDays &&
                        message == other.message &&
                        equalCompliance(other);
    }
}
