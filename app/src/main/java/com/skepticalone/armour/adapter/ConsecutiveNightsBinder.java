package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.ComplianceDataConsecutiveNights;

final class ConsecutiveNightsBinder extends ComplianceDataBinder<ComplianceDataConsecutiveNights> {

    ConsecutiveNightsBinder(@NonNull Callbacks callbacks, @NonNull ComplianceDataConsecutiveNights complianceDataConsecutiveNights) {
        super(callbacks, complianceDataConsecutiveNights);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_consecutive_shifts_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.consecutive_nights);
    }

    @NonNull
    @Override
    String getSecondLine(@NonNull Context context) {
        int nights = getData().getIndex() + 1;
        return context.getResources().getQuantityString(R.plurals.nights, nights, nights);
    }

    @NonNull
    @Override
    String getMessage(@NonNull Context context) {
        return context.getString(R.string.meca_maximum_consecutive_nights, getData().getMaximumConsecutiveNights());
    }

    @Override
    boolean areContentsTheSame(@NonNull ComplianceDataConsecutiveNights A, @NonNull ComplianceDataConsecutiveNights B) {
        return A.getIndex() == B.getIndex() && A.getMaximumConsecutiveNights() == B.getMaximumConsecutiveNights();
    }

}
