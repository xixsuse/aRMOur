package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.AdditionalShift;

import java.util.List;

public final class AdditionalShiftTotalsAdapter extends PayableTotalsAdapter<AdditionalShift> {

    @NonNull
    private final ShiftTotalsAdapterHelper<AdditionalShift> helper;

    public AdditionalShiftTotalsAdapter(@NonNull Context context, int totalItemsTitle, @NonNull Callbacks callbacks) {
        super(context, totalItemsTitle, callbacks);
        helper = new ShiftTotalsAdapterHelper<AdditionalShift>() {
            @NonNull
            @Override
            String getThirdLine(@NonNull String totalDuration, @NonNull List<AdditionalShift> shifts) {
                return totalDuration + '\n' + getTotalPayment(shifts);
            }
        };
    }

    @Override
    int getFixedItemCount() {
        return ShiftTotalsAdapterHelper.ROW_COUNT;
    }

    @Override
    void onBindViewHolder(@NonNull List<AdditionalShift> allShifts, int position, @NonNull ItemViewHolder holder) {
        helper.onBindViewHolder(allShifts, position, holder, this);
    }

}
