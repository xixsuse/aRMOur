package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.AdditionalShift;

public final class AdditionalShiftListAdapter extends ShiftListAdapter<AdditionalShift> {

    public AdditionalShiftListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    boolean areContentsTheSame(@NonNull AdditionalShift shift1, @NonNull AdditionalShift shift2) {
        return super.areContentsTheSame(shift1, shift2) &&
                shift1.getPaymentData().getIcon() == shift2.getPaymentData().getIcon();
    }

    @Override
    int getSecondaryIcon(@NonNull AdditionalShift shift) {
        return shift.getPaymentData().getIcon();
    }

}
