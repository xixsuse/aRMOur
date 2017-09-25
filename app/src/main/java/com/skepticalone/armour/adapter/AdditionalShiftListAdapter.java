package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.AdditionalShift;
import com.skepticalone.armour.util.Comparators;

public final class AdditionalShiftListAdapter extends ShiftListAdapter<AdditionalShift> {

    public AdditionalShiftListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
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

    @Nullable
    @Override
    String getThirdLine(@NonNull AdditionalShift shift) {
        return shift.getComment();
    }

}
