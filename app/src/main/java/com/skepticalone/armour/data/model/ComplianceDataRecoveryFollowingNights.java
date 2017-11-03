package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.LocalDate;

import java.util.List;

public final class ComplianceDataRecoveryFollowingNights extends ComplianceData {

    private final int consecutiveNights, recoveryDays;
    private final boolean saferRosters, lenient;

    private ComplianceDataRecoveryFollowingNights(@NonNull ComplianceConfiguration complianceConfiguration, @NonNull Shift.Data dayShift, @NonNull Shift.Data previousNightShift, int indexOfPreviousNightShift) {
        super(complianceConfiguration.checkRecoveryFollowingNights());
        if (complianceConfiguration instanceof ComplianceConfigurationSaferRosters) {
            saferRosters = true;
            lenient = ((ComplianceConfigurationSaferRosters) complianceConfiguration).allowOnly1RecoveryDayFollowing3Nights();
        } else {
            saferRosters = lenient = false;
        }
        consecutiveNights = indexOfPreviousNightShift + 1;
        LocalDate start = dayShift.getStart().toLocalDate(), previousShiftEnd = previousNightShift.getEnd().toLocalDate();
        recoveryDays = start.isEqual(previousShiftEnd) ? 0 : (int) (start.toEpochDay() - previousShiftEnd.toEpochDay() - 1);
    }

    @Nullable
    static ComplianceDataRecoveryFollowingNights from(@NonNull ComplianceConfiguration complianceConfiguration, @NonNull Shift.Data dayShift, @NonNull List<RosteredShift> previousShifts) {
        if (!previousShifts.isEmpty()) {
            RosteredShift previousShift = previousShifts.get(previousShifts.size() - 1);
            ComplianceDataConsecutiveNights previousShiftConsecutiveNights = previousShift.getCompliance().getConsecutiveNights();
            if (previousShiftConsecutiveNights != null) {
                return new ComplianceDataRecoveryFollowingNights(complianceConfiguration, dayShift, previousShift.getShiftData(), previousShiftConsecutiveNights.getIndex());
            }
        }
        return null;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        if (saferRosters) {
            if (consecutiveNights < 2) {
                return true;
            } else if (consecutiveNights == 2) {
                return recoveryDays >= AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_2_NIGHTS;
            } else if (consecutiveNights == 3) {
                return recoveryDays >= (lenient ? AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_LENIENT : AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_STRICT);
            } else {
                return recoveryDays >= AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_4_OR_MORE_NIGHTS;
            }
        } else {
            return consecutiveNights < AppConstants.MINIMUM_NIGHTS_BEFORE_RECOVERY || recoveryDays >= AppConstants.MINIMUM_RECOVERY_DAYS_FOLLOWING_NIGHTS;
        }
    }

    public final int getConsecutiveNights() {
        return consecutiveNights;
    }

    public final int getRecoveryDays() {
        return recoveryDays;
    }

    public final boolean saferRosters() {
        return saferRosters;
    }

    public final boolean isLenient() {
        return lenient;
    }
}
