package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.ComplianceDataConsecutiveDays;

final class ConsecutiveDaysBinder extends ComplianceDataBinder<ComplianceDataConsecutiveDays> {

    ConsecutiveDaysBinder(@NonNull Callbacks callbacks, @NonNull ComplianceDataConsecutiveDays complianceDataConsecutiveDays) {
        super(callbacks, complianceDataConsecutiveDays);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_consecutive_shifts_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.consecutive_days);
    }

    @NonNull
    @Override
    String getSecondLine(@NonNull Context context) {
        int days = getData().getIndex() + 1;
        return context.getResources().getQuantityString(R.plurals.days, days, days);
    }

    @NonNull
    @Override
    String getMessage(@NonNull Context context) {
        return context.getString(getData().isSaferRosters() ? R.string.meca_safer_rosters_maximum_consecutive_days : R.string.meca_maximum_consecutive_days, getData().getMaximumConsecutiveDays());
    }

    @Override
    boolean areContentsTheSame(@NonNull ComplianceDataConsecutiveDays A, @NonNull ComplianceDataConsecutiveDays B) {
        return
                A.getIndex() == B.getIndex() &&
                        A.getMaximumConsecutiveDays() == B.getMaximumConsecutiveDays() &&
                        A.isSaferRosters() == B.isSaferRosters();
    }

}
