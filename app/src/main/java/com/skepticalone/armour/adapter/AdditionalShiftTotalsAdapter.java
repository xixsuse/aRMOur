package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.entity.AdditionalShiftEntity;
import com.skepticalone.armour.util.ShiftUtil;

import java.util.List;

public final class AdditionalShiftTotalsAdapter extends PayableTotalsAdapter<AdditionalShiftEntity> implements ShiftTotalsAdapterHelper.Callbacks<AdditionalShiftEntity> {

    @NonNull
    private final ShiftTotalsAdapterHelper<AdditionalShiftEntity> helper;

    public AdditionalShiftTotalsAdapter(@NonNull Callbacks callbacks, @NonNull ShiftUtil.Calculator calculator) {
        super(callbacks);
        helper = new ShiftTotalsAdapterHelper<>(this, calculator);
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
    public String getThirdLine(@NonNull String totalDuration, @NonNull List<AdditionalShiftEntity> shifts, @NonNull ItemViewHolder holder) {
        return totalDuration + '\n' + getTotalPayment(shifts, holder);
    }

    @Override
    boolean bindViewHolder(@NonNull List<AdditionalShiftEntity> allShifts, @NonNull ItemViewHolder holder, int position) {
        return helper.bindViewHolder(allShifts, holder, position);
    }

}
