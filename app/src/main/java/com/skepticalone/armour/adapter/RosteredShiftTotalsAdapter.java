package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;

import java.util.List;

public final class RosteredShiftTotalsAdapter extends FilteredItemTotalsAdapter<RosteredShift> {

    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final ShiftTotalsAdapterHelper<RosteredShift> helper;

    public RosteredShiftTotalsAdapter(@NonNull Context context, int totalItemsTitle, @NonNull Callbacks callbacks) {
        super(context, totalItemsTitle);
        this.callbacks = callbacks;
        helper = new ShiftTotalsAdapterHelper<RosteredShift>() {
            @NonNull
            @Override
            String getThirdLine(@NonNull String totalDuration, @NonNull List<RosteredShift> shifts) {
                return totalDuration;
            }
        };
    }

    @Override
    public boolean isFiltered() {
        return !callbacks.includeCompliant() || !callbacks.includeNonCompliant();
    }

    @Override
    public boolean isIncluded(@NonNull RosteredShift shift) {
        return shift.getCompliance().isCompliant() ? callbacks.includeCompliant() : callbacks.includeNonCompliant();
    }

    @Override
    int getFixedRowCount() {
        return ShiftTotalsAdapterHelper.ROW_COUNT;
    }

    @Override
    void onBindViewHolder(@NonNull List<RosteredShift> allShifts, int position, @NonNull ItemViewHolder holder) {
        helper.onBindViewHolder(allShifts, position, holder, this);
    }

    public interface Callbacks {
        boolean includeCompliant();
        boolean includeNonCompliant();
    }

}
