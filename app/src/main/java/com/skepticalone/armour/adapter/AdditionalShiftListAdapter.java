package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RawAdditionalShiftEntity;
import com.skepticalone.armour.data.model.LiveShiftConfig;
import com.skepticalone.armour.util.Comparators;

import org.threeten.bp.ZoneId;

public final class AdditionalShiftListAdapter extends ShiftListAdapter<RawAdditionalShiftEntity> {

    public AdditionalShiftListAdapter(@NonNull Callbacks callbacks, @NonNull LiveShiftConfig liveShiftConfig) {
        super(callbacks, liveShiftConfig);
    }

    @Override
    boolean areContentsTheSame(@NonNull RawAdditionalShiftEntity shift1, @NonNull RawAdditionalShiftEntity shift2) {
        return super.areContentsTheSame(shift1, shift2) &&
                Comparators.equalPaymentData(shift1.getPaymentData(), shift2.getPaymentData());
    }

    @Override
    int getSecondaryIcon(@NonNull RawAdditionalShiftEntity shift) {
        return shift.getPaymentData().getIcon();
    }

    @Nullable
    @Override
    String getThirdLine(@NonNull RawAdditionalShiftEntity shift, @NonNull ZoneId zoneId) {
        return shift.getComment();
    }

}
