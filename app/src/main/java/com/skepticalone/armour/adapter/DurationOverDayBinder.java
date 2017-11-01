package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.ComplianceDataDuration;
import com.skepticalone.armour.util.AppConstants;

final class DurationOverDayBinder extends DurationBinder {

    DurationOverDayBinder(@NonNull Callbacks callbacks, @NonNull ComplianceDataDuration durationOverDay) {
        super(callbacks, durationOverDay);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_duration_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.duration_worked_over_day);
    }

    @NonNull
    @Override
    String getMessage(@NonNull Context context) {
        return context.getString(R.string.meca_maximum_hours_over_day, AppConstants.MAXIMUM_HOURS_OVER_DAY);
    }
}
