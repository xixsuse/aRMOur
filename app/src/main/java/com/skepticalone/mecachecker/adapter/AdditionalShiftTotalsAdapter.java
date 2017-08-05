package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.util.ShiftUtil;

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
    public String getSecondLine(@NonNull List<AdditionalShiftEntity> shifts, @NonNull ItemViewHolder holder) {
        return getTotalNumber(shifts, holder);
    }

    @NonNull
    @Override
    public String getThirdLine(@NonNull List<AdditionalShiftEntity> shifts, @NonNull ItemViewHolder holder) {
        return getTotalPayment(shifts, holder);
    }

    @Override
    boolean bindViewHolder(@NonNull List<AdditionalShiftEntity> allShifts, @NonNull ItemViewHolder holder, int position) {
        return helper.bindViewHolder(allShifts, holder, position);
    }

}
