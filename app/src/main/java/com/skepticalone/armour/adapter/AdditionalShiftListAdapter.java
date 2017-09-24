package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.entity.AdditionalShiftEntity;
import com.skepticalone.armour.data.entity.LiveShiftConfig;
import com.skepticalone.armour.util.Comparators;

import org.threeten.bp.ZoneId;

public final class AdditionalShiftListAdapter extends ShiftListAdapter<AdditionalShiftEntity> {

    public AdditionalShiftListAdapter(@NonNull Callbacks callbacks, @NonNull LiveShiftConfig liveShiftConfig) {
        super(callbacks, liveShiftConfig);
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
    String getThirdLine(@NonNull AdditionalShiftEntity shift, @NonNull ZoneId zoneId) {
        return shift.getComment();
    }

}
