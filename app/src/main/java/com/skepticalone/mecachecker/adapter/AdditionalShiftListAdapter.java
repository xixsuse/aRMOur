package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.model.AdditionalShift;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class AdditionalShiftListAdapter extends ShiftListAdapter<AdditionalShift> {

    AdditionalShiftListAdapter(Callbacks callbacks, ShiftUtil.Calculator calculator) {
        super(callbacks, calculator);
    }

    @Override
    boolean areContentsTheSame(@NonNull AdditionalShift shift1, @NonNull AdditionalShift shift2) {
        return super.areContentsTheSame(shift1, shift2) &&
                Comparators.equalPaymentData(shift1.getPaymentData(), shift2.getPaymentData());
    }

    @Override
    int getSecondaryIcon(@NonNull AdditionalShift shift) {
        return shift.getPaymentData().getIcon();
    }

    @NonNull
    @Override
    String getTimeSpanString(@NonNull AdditionalShift shift) {
        return DateTimeUtils.getTimeSpanString(shift.getShiftData());
    }

}
