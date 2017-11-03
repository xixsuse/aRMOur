package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.ComplianceDataDuration;
import com.skepticalone.armour.util.AppConstants;

final class DurationOverWeekBinder extends DurationBinder {

    DurationOverWeekBinder(@NonNull Callbacks callbacks, @NonNull ComplianceDataDuration durationOverWeek) {
        super(callbacks, durationOverWeek);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_week_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.duration_over_week);
    }

    @NonNull
    @Override
    String getMessage(@NonNull Context context) {
        return context.getString(R.string.meca_maximum_hours_over_week, AppConstants.MAXIMUM_HOURS_OVER_WEEK);
    }
}
