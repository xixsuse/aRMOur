package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class AdditionalShiftListAdapter extends ShiftListAdapter<AdditionalShiftEntity> {

    public AdditionalShiftListAdapter(@NonNull Callbacks callbacks, @NonNull ShiftUtil.Calculator calculator) {
        super(callbacks, calculator);
    }

    @Override
    boolean areContentsTheSame(@NonNull AdditionalShiftEntity shift1, @NonNull AdditionalShiftEntity shift2) {
        return super.areContentsTheSame(shift1, shift2) &&
                Comparators.equalPaymentData(shift1.getPaymentData(), shift2.getPaymentData());
    }

    @Override
    int getSecondaryIcon(@NonNull AdditionalShiftEntity shift) {
        return shift.getPaymentData().getIcon();
    }

    @NonNull
    @Override
    String getTimeSpanString(@NonNull AdditionalShiftEntity shift) {
        return DateTimeUtils.getTimeSpanString(shift.getShiftData());
    }

}
