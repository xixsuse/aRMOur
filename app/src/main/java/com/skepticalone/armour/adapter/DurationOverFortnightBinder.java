package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.ComplianceDataDuration;
import com.skepticalone.armour.util.AppConstants;

final class DurationOverFortnightBinder extends DurationBinder {

    DurationOverFortnightBinder(@NonNull Callbacks callbacks, @NonNull ComplianceDataDuration durationOverFortnight) {
        super(callbacks, durationOverFortnight);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_weeks_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.duration_over_fortnight);
    }

    @NonNull
    @Override
    String getMessage(@NonNull Context context) {
        return context.getString(R.string.meca_maximum_hours_over_fortnight, AppConstants.MAXIMUM_HOURS_OVER_FORTNIGHT);
    }
}
