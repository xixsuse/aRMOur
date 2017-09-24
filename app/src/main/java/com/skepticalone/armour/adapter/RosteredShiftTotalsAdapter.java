package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.RawRosteredShiftEntity;

import java.util.List;

public final class RosteredShiftTotalsAdapter extends ItemTotalsAdapter<RawRosteredShiftEntity> implements ShiftTotalsAdapterHelper.Callbacks<RawRosteredShiftEntity> {

    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final ShiftTotalsAdapterHelper<RawRosteredShiftEntity> helper;

    public RosteredShiftTotalsAdapter(@NonNull Callbacks callbacks) {
        this.callbacks = callbacks;
        helper = new ShiftTotalsAdapterHelper<>(this);
    }

    @Override
    public boolean isFiltered() {
        return !callbacks.includeCompliant() || !callbacks.includeNonCompliant();
    }

    @Override
    public boolean isIncluded(@NonNull RawRosteredShiftEntity shift) {
        return shift.isCompliant() ? callbacks.includeCompliant() : callbacks.includeNonCompliant();
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
    public String getThirdLine(@NonNull String totalDuration, @NonNull List<RawRosteredShiftEntity> shifts, @NonNull ItemViewHolder holder) {
        return totalDuration;
    }

    @Override
    boolean bindViewHolder(@NonNull List<RawRosteredShiftEntity> allShifts, @NonNull ItemViewHolder holder, int position) {
        return helper.bindViewHolder(allShifts, holder, position);
    }

    public interface Callbacks {
        boolean includeCompliant();
        boolean includeNonCompliant();
    }

}
