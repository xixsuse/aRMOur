package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.LocalDate;

import java.util.List;

final class RowNight extends Row {

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

    final int getIndexOfNightShift() {
        return indexOfNightShift;
    }

}
