package com.skepticalone.armour.data.compliance;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import java.util.List;

final class RowRecoveryFollowingNightsSaferRosters extends RowRecoveryFollowingNights {

    private RowRecoveryFollowingNightsSaferRosters(@NonNull ConfigurationSaferRosters configuration, @NonNull Shift.Data dayShift, @NonNull Shift.Data previousNightShift, int indexOfPreviousNightShift) {
        super(configuration, dayShift, previousNightShift, indexOfPreviousNightShift, configuration.allowOnly1RecoveryDayFollowing3Nights());
    }

    @Nullable
    static RowRecoveryFollowingNightsSaferRosters from(@NonNull ConfigurationSaferRosters configuration, @NonNull Shift.Data dayShift, @NonNull List<RosteredShift> previousShifts) {
        if (!previousShifts.isEmpty()) {
            RosteredShift previousShift = previousShifts.get(previousShifts.size() - 1);
            RowNight previousShiftConsecutiveNights = previousShift.getCompliance().getNight();
            if (previousShiftConsecutiveNights != null) {
                return new RowRecoveryFollowingNightsSaferRosters(configuration, dayShift, previousShift.getShiftData(), previousShiftConsecutiveNights.getIndexOfNightShift());
            }
        }
        return null;
    }

    @Override
    boolean isCompliant(int consecutiveNights, int recoveryDays, boolean lenient) {
        if (consecutiveNights < 2) {
            return true;
        } else if (consecutiveNights == 2) {
            return recoveryDays >= AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_2_NIGHTS;
        } else if (consecutiveNights == 3) {
            return recoveryDays >= (lenient ? AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_LENIENT : AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_STRICT);
        } else {
            return recoveryDays >= AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_4_OR_MORE_NIGHTS;
        }
    }

}
