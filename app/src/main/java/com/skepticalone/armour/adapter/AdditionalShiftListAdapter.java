package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.entity.AdditionalShiftEntity;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.ShiftUtil;

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

    @Nullable
    @Override
    String getThirdLine(@NonNull AdditionalShiftEntity shift) {
        return shift.getComment();
    }

}
