package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.ComplianceDataRecoveryFollowingNights;
import com.skepticalone.armour.util.AppConstants;

final class RecoveryFollowingNightsBinder extends ComplianceDataBinder<ComplianceDataRecoveryFollowingNights> {

    RecoveryFollowingNightsBinder(@NonNull Callbacks callbacks, @NonNull ComplianceDataRecoveryFollowingNights complianceDataRecoveryFollowingNights) {
        super(callbacks, complianceDataRecoveryFollowingNights);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_recovery_following_nights;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.recovery_following_nights);
    }

    @NonNull
    @Override
    String getSecondLine(@NonNull Context context) {
        return context.getString(R.string.x_following_y, context.getResources().getQuantityString(R.plurals.days, getData().getRecoveryDays(), getData().getRecoveryDays()), context.getResources().getQuantityString(R.plurals.nights, getData().getConsecutiveNights(), getData().getConsecutiveNights()));
    }

    @NonNull
    @Override
    String getMessage(@NonNull Context context) {
        if (getData().saferRosters()) {
            if (getData().getConsecutiveNights() < 2) {
                return context.getString(R.string.recovery_days_only_required_following_n_nights, context.getResources().getQuantityString(R.plurals.nights, 2, 2));
            } else if (getData().getConsecutiveNights() == 2) {
                return context.getString(R.string.meca_safer_rosters_minimum_recovery_after_fewer_consecutive_nights, 2, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_2_NIGHTS);
            } else if (getData().getConsecutiveNights() == 3) {
                return getData().isLenient() ? context.getString(R.string.meca_safer_rosters_minimum_recovery_after_fewer_consecutive_nights, 3, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_LENIENT) : context.getString(R.string.meca_safer_rosters_minimum_recovery_after_more_consecutive_nights, 3, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_STRICT);
            } else {
                return context.getString(R.string.meca_safer_rosters_minimum_recovery_after_more_consecutive_nights, 4, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_4_OR_MORE_NIGHTS);
            }
        } else {
            return context.getString(R.string.meca_minimum_recovery_after_consecutive_nights, AppConstants.MINIMUM_RECOVERY_DAYS_FOLLOWING_NIGHTS, AppConstants.MINIMUM_NIGHTS_BEFORE_RECOVERY);
        }
    }

    @Override
    boolean areContentsTheSame(@NonNull ComplianceDataRecoveryFollowingNights A, @NonNull ComplianceDataRecoveryFollowingNights B) {
        return
                A.getRecoveryDays() == B.getRecoveryDays() &&
                        A.getConsecutiveNights() == B.getConsecutiveNights() &&
                        (A.saferRosters() ? (B.saferRosters() && (A.isLenient() == B.isLenient())) : !B.saferRosters());
    }

}
