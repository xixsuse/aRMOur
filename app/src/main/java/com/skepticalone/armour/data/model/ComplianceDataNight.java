package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.List;

public final class ComplianceDataNight extends ComplianceDataIndex {

    private static final LocalTime EARLIEST_DAY_SHIFT_START = LocalTime.of(6, 0);

    private final int maximumConsecutiveNights;

    private ComplianceDataNight(boolean isChecked, int indexOfNightShift, int maximumConsecutiveNights) {
        super(isChecked, indexOfNightShift);
        this.maximumConsecutiveNights = maximumConsecutiveNights;
    }

    private static int calculateIndexOfNightShift(@NonNull Shift.Data nightShift, @NonNull List<RosteredShift> previousShifts) {
        if (!previousShifts.isEmpty()) {
            RosteredShift previousShift = previousShifts.get(previousShifts.size() - 1);
            ComplianceDataNight previousShiftNights = previousShift.getCompliance().getNight();
            if (previousShiftNights != null) {
                LocalDate thisNightShiftDate = nightShift.getEnd().toLocalDate(), previousNightShiftDate = previousShift.getShiftData().getEnd().toLocalDate();
                if (thisNightShiftDate.equals(previousNightShiftDate)) {
                    return previousShiftNights.getIndex();
                } else if (previousNightShiftDate.plusDays(1).isEqual(thisNightShiftDate)) {
                    return previousShiftNights.getIndex() + 1;
                }
            }
        }
        return 0;
    }

    private static int calculateMaximumConsecutiveNights(@NonNull Configuration configuration) {
        return configuration instanceof ConfigurationSaferRosters ? ((ConfigurationSaferRosters) configuration).allow5ConsecutiveNights() ? AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_NIGHTS_LENIENT : AppConstants.SAFER_ROSTERS_MAXIMUM_CONSECUTIVE_NIGHTS_STRICT : AppConstants.MAXIMUM_CONSECUTIVE_NIGHTS;
    }

    private static boolean calculateIsNightShift(Shift.Data shift) {
        return !shift.getStart().toLocalDate().isEqual(shift.getEnd().toLocalDate()) || shift.getStart().toLocalTime().isBefore(EARLIEST_DAY_SHIFT_START);
    }

    @Nullable
    static ComplianceDataNight from(@NonNull Configuration configuration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        return calculateIsNightShift(shift) ? new ComplianceDataNight(
                configuration.checkConsecutiveNightsWorked(),
                calculateIndexOfNightShift(shift, previousShifts), calculateMaximumConsecutiveNights(configuration)
        ) : null;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return getIndex() < maximumConsecutiveNights;
    }

    public final int getMaximumConsecutiveNights() {
        return maximumConsecutiveNights;
    }

}
