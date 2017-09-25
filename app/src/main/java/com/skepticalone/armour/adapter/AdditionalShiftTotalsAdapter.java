package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.AdditionalShift;

import java.util.List;

public final class AdditionalShiftTotalsAdapter extends PayableTotalsAdapter<AdditionalShift> implements ShiftTotalsAdapterHelper.Callbacks<AdditionalShift> {

    @NonNull
    private final ShiftTotalsAdapterHelper<AdditionalShift> helper;

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
    public String getThirdLine(@NonNull String totalDuration, @NonNull List<AdditionalShift> shifts, @NonNull ItemViewHolder holder) {
        return totalDuration + '\n' + getTotalPayment(shifts, holder);
    }

    @Override
    boolean bindViewHolder(@NonNull List<AdditionalShift> allShifts, @NonNull ItemViewHolder holder, int position) {
        return helper.bindViewHolder(allShifts, holder, position);
    }

}
