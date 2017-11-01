package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.ComplianceDataDuration;
import com.skepticalone.armour.util.DateTimeUtils;

abstract class DurationBinder extends ComplianceDataBinder<ComplianceDataDuration> {

    DurationBinder(@NonNull Callbacks callbacks, @NonNull ComplianceDataDuration complianceDataDuration) {
        super(callbacks, complianceDataDuration);
    }

    @Override
    final boolean areContentsTheSame(@NonNull ComplianceDataDuration A, @NonNull ComplianceDataDuration B) {
        return A.getDuration().equals(B.getDuration());
    }

    @Nullable
    @Override
    final String getSecondLine(@NonNull Context context) {
        return DateTimeUtils.getDurationString(context, getData().getDuration());
    }

}
