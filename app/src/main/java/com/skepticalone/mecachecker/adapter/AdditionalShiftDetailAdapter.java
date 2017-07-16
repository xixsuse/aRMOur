package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.model.AdditionalShift;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class AdditionalShiftDetailAdapter extends ItemDetailAdapter<AdditionalShift> {

    private final ShiftDetailAdapterHelper shiftDetailAdapterHelper;
    private final PayableDetailAdapterHelper payableDetailAdapterHelper;

    public AdditionalShiftDetailAdapter(Callbacks callbacks, ShiftUtil.Calculator calculator) {
        super(callbacks);
        shiftDetailAdapterHelper = new ShiftDetailAdapterHelper(callbacks, calculator);
        payableDetailAdapterHelper = new PayableDetailAdapterHelper(callbacks);
    }

    @Override
    void onItemUpdated(@NonNull AdditionalShift oldShift, @NonNull AdditionalShift newShift) {
        super.onItemUpdated(oldShift, newShift);
        shiftDetailAdapterHelper.onItemUpdated(oldShift, newShift, this);
        payableDetailAdapterHelper.onItemUpdated(oldShift, newShift, this);
    }

    @Override
    boolean bindViewHolder(@NonNull AdditionalShift shift, ItemViewHolder holder, int position) {
        return
                shiftDetailAdapterHelper.bindViewHolder(shift, holder, position) ||
                payableDetailAdapterHelper.bindViewHolder(shift, holder, position) ||
                super.bindViewHolder(shift, holder, position);
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks<AdditionalShift>, ShiftDetailAdapterHelper.Callbacks, PayableDetailAdapterHelper.Callbacks {}

}
