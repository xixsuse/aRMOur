package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.AdditionalShift;

import java.util.List;

public final class AdditionalShiftTotalsAdapter extends PayableTotalsAdapter<AdditionalShift> implements ShiftTotalsAdapterHelper.Callbacks<AdditionalShift> {

    @NonNull
    private final ShiftTotalsAdapterHelper<AdditionalShift> helper;

    public AdditionalShiftTotalsAdapter(@NonNull Context context, int totalItemsTitle, @NonNull Callbacks callbacks) {
        super(context, totalItemsTitle, callbacks);
        helper = new ShiftTotalsAdapterHelper<>(this);
    }

    @Override
    int getRowCount() {
        return ShiftTotalsAdapterHelper.ROW_COUNT;
    }

    @NonNull
    @Override
    public String getThirdLine(@NonNull String totalDuration, @NonNull List<AdditionalShift> shifts) {
        return totalDuration + '\n' + getTotalPayment(shifts);
    }

    @Override
    boolean bindViewHolder(@NonNull List<AdditionalShift> allShifts, @NonNull ItemViewHolder holder, int position) {
        return helper.bindViewHolder(this, allShifts, holder, position);
    }

}
