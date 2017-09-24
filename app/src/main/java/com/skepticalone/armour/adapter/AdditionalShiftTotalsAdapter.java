package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.RawAdditionalShiftEntity;

import java.util.List;

public final class AdditionalShiftTotalsAdapter extends PayableTotalsAdapter<RawAdditionalShiftEntity> implements ShiftTotalsAdapterHelper.Callbacks<RawAdditionalShiftEntity> {

    @NonNull
    private final ShiftTotalsAdapterHelper<RawAdditionalShiftEntity> helper;

    public AdditionalShiftTotalsAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
        helper = new ShiftTotalsAdapterHelper<>(this);
    }

    @Override
    int getRowCount() {
        return ShiftTotalsAdapterHelper.ROW_COUNT;
    }

    @Override
    public int getAllShiftsTitle() {
        return R.string.all_additional_shifts;
    }

    @NonNull
    @Override
    public String getThirdLine(@NonNull String totalDuration, @NonNull List<RawAdditionalShiftEntity> shifts, @NonNull ItemViewHolder holder) {
        return totalDuration + '\n' + getTotalPayment(shifts, holder);
    }

    @Override
    boolean bindViewHolder(@NonNull List<RawAdditionalShiftEntity> allShifts, @NonNull ItemViewHolder holder, int position) {
        return helper.bindViewHolder(allShifts, holder, position);
    }

}
