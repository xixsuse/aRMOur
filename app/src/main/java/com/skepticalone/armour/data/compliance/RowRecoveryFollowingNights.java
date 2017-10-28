package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.LocalDate;

import java.util.List;

public class RowRecoveryFollowingNights extends Row {

    private final int consecutiveNights, recoveryDays;
    private final boolean lenient;

    RowRecoveryFollowingNights(@NonNull Configuration configuration, @NonNull Shift.Data dayShift, @NonNull Shift.Data previousNightShift, int indexOfPreviousNightShift, boolean lenient) {
        super(configuration.checkRecoveryFollowingNights());
        this.lenient = lenient;
        consecutiveNights = indexOfPreviousNightShift + 1;
        LocalDate start = dayShift.getStart().toLocalDate(), previousShiftEnd = previousNightShift.getEnd().toLocalDate();
        recoveryDays = start.isEqual(previousShiftEnd) ? 0 : (int) (start.toEpochDay() - previousShiftEnd.toEpochDay() - 1);
    }

    @Nullable
    static RowRecoveryFollowingNights from(@NonNull Configuration configuration, @NonNull Shift.Data dayShift, @NonNull List<RosteredShift> previousShifts) {
        if (!previousShifts.isEmpty()) {
            RosteredShift previousShift = previousShifts.get(previousShifts.size() - 1);
            RowNight previousShiftConsecutiveNights = previousShift.getCompliance().getNight();
            if (previousShiftConsecutiveNights != null) {
                return new RowRecoveryFollowingNights(configuration, dayShift, previousShift.getShiftData(), previousShiftConsecutiveNights.getIndexOfNightShift(), false);
            }
        }
        return null;
    }

    public final boolean isLenient() {
        return lenient;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return isCompliant(consecutiveNights, recoveryDays);
    }

    boolean isCompliant(int consecutiveNights, int recoveryDays) {
        return consecutiveNights < AppConstants.MINIMUM_NIGHTS_BEFORE_RECOVERY || recoveryDays >= AppConstants.MINIMUM_RECOVERY_DAYS_FOLLOWING_NIGHTS;
    }

    public final int getConsecutiveNights() {
        return consecutiveNights;
    }

    public final int getRecoveryDays() {
        return recoveryDays;
    }

    public final boolean isEqual(@NonNull RowRecoveryFollowingNights other) {
        return
                consecutiveNights == other.consecutiveNights &&
                        recoveryDays == other.recoveryDays &&
                        equalCompliance(other);
    }
}
