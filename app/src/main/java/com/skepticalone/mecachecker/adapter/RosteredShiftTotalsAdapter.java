package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.util.ShiftUtil;

import java.util.List;

public final class RosteredShiftTotalsAdapter extends ItemTotalsAdapter<RosteredShiftEntity> implements ShiftTotalsAdapterHelper.Callbacks<RosteredShiftEntity> {

    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final ShiftTotalsAdapterHelper<RosteredShiftEntity> helper;

    public RosteredShiftTotalsAdapter(@NonNull Callbacks callbacks, @NonNull ShiftUtil.Calculator calculator) {
        this.callbacks = callbacks;
        helper = new ShiftTotalsAdapterHelper<>(this, calculator);
    }

    @Override
    public boolean isFiltered() {
        return !callbacks.includeCompliant() || !callbacks.includeNonCompliant();
    }

    @Override
    public boolean isIncluded(@NonNull RosteredShiftEntity shift) {
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
    public String getThirdLine(@NonNull String totalDuration, @NonNull List<RosteredShiftEntity> shifts, @NonNull ItemViewHolder holder) {
        return totalDuration;
    }

    @Override
    boolean bindViewHolder(@NonNull List<RosteredShiftEntity> allShifts, @NonNull ItemViewHolder holder, int position) {
        return helper.bindViewHolder(allShifts, holder, position);
    }

    public interface Callbacks {
        boolean includeCompliant();
        boolean includeNonCompliant();
    }

}
