package com.skepticalone.armour.data.compliance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemViewHolder;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.LocalDate;

import java.util.List;

public final class RowRecoveryFollowingNights extends Row {

    private final int consecutiveNights, recoveryDays;
    private final boolean saferRosters, lenient;

    private RowRecoveryFollowingNights(@NonNull Configuration configuration, @NonNull Shift.Data dayShift, @NonNull Shift.Data previousNightShift, int indexOfPreviousNightShift) {
        super(configuration.checkRecoveryFollowingNights());
        if (configuration instanceof ConfigurationSaferRosters) {
            saferRosters = true;
            lenient = ((ConfigurationSaferRosters) configuration).allowOnly1RecoveryDayFollowing3Nights();
        } else {
            saferRosters = lenient = false;
        }
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
                return new RowRecoveryFollowingNights(configuration, dayShift, previousShift.getShiftData(), previousShiftConsecutiveNights.getIndexOfNightShift());
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

    final int getConsecutiveNights() {
        return consecutiveNights;
    }

    final int getRecoveryDays() {
        return recoveryDays;
    }

    public static final class Binder extends Row.Binder<RowRecoveryFollowingNights> {

        public Binder(@NonNull Callbacks callbacks, @NonNull RowRecoveryFollowingNights row) {
            super(callbacks, row);
        }

        @Override
        public int getPrimaryIcon() {
            return R.drawable.ic_recovery_days_following_nights;
        }

        @NonNull
        @Override
        public String getFirstLine(@NonNull Context context) {
            return context.getString(R.string.recovery_days_following_nights);
        }

        @Override
        public String getSecondLine(@NonNull Context context) {
            return context.getString(R.string.n_days_following_n_nights, context.getResources().getQuantityString(R.plurals.days, getRow().recoveryDays, getRow().recoveryDays), context.getResources().getQuantityString(R.plurals.nights, getRow().consecutiveNights, getRow().consecutiveNights));
        }

        @NonNull
        @Override
        String getMessage(@NonNull Context context) {
            if (getRow().saferRosters) {
                if (getRow().consecutiveNights < 2) {
                    return context.getString(R.string.recovery_days_only_required_following_n_nights, context.getResources().getQuantityString(R.plurals.nights, 2, 2));
                } else if (getRow().consecutiveNights == 2) {
                    return context.getString(R.string.meca_safer_rosters_minimum_recovery_after_fewer_consecutive_nights, 2, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_2_NIGHTS);
                } else if (getRow().consecutiveNights == 3) {
                    return getRow().lenient ? context.getString(R.string.meca_safer_rosters_minimum_recovery_after_fewer_consecutive_nights, 3, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_LENIENT) : context.getString(R.string.meca_safer_rosters_minimum_recovery_after_more_consecutive_nights, 3, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_STRICT);
                } else {
                    return context.getString(R.string.meca_safer_rosters_minimum_recovery_after_more_consecutive_nights, 4, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_4_OR_MORE_NIGHTS);
                }
            } else {
                return context.getString(R.string.meca_minimum_recovery_after_consecutive_nights, AppConstants.MINIMUM_RECOVERY_DAYS_FOLLOWING_NIGHTS, AppConstants.MINIMUM_NIGHTS_BEFORE_RECOVERY);
            }
        }

        @Override
        public boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
            if (!super.areContentsTheSame(other)) return false;
            Binder newBinder = (Binder) other;
            return
                    getRow().consecutiveNights == newBinder.getRow().consecutiveNights &&
                            getRow().saferRosters ?
                            (newBinder.getRow().saferRosters && (getRow().lenient == newBinder.getRow().lenient)) :
                            !newBinder.getRow().saferRosters;
        }
    }
}
