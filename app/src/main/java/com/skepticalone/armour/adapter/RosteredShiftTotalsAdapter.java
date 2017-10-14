package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.RosteredShift;

import java.util.List;

public final class RosteredShiftTotalsAdapter extends ItemTotalsAdapter<RosteredShift> implements ShiftTotalsAdapterHelper.Callbacks<RosteredShift> {

    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final ShiftTotalsAdapterHelper<RosteredShift> helper;

    public RosteredShiftTotalsAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context);
        this.callbacks = callbacks;
        helper = new ShiftTotalsAdapterHelper<>(this);
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
    int getRowCount() {
        return ShiftTotalsAdapterHelper.ROW_COUNT;
    }

    @Override
    public int getAllShiftsTitle() {
        return R.string.all_rostered_shifts;
    }

    @NonNull
    @Override
    public String getThirdLine(@NonNull String totalDuration, @NonNull List<RosteredShift> shifts) {
        return totalDuration;
    }

    @Override
    boolean bindViewHolder(@NonNull List<RosteredShift> allShifts, @NonNull ItemViewHolder holder, int position) {
        return helper.bindViewHolder(this, allShifts, holder, position);
    }

    public interface Callbacks {
        boolean includeCompliant();
        boolean includeNonCompliant();
    }

}
