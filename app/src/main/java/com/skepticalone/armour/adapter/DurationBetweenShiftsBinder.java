package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.ComplianceDataDuration;
import com.skepticalone.armour.util.AppConstants;

final class DurationBetweenShiftsBinder extends DurationBinder {

    DurationBetweenShiftsBinder(@NonNull Callbacks callbacks, @NonNull ComplianceDataDuration durationBetweenShifts) {
        super(callbacks, durationBetweenShifts);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_sleep_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.duration_between_shifts);
    }

    @NonNull
    @Override
    String getMessage(@NonNull Context context) {
        return context.getString(R.string.meca_minimum_hours_between_shifts, AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS);
    }

}
