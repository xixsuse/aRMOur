package com.skepticalone.mecachecker.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.model.Shift;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftUtil;

abstract class ShiftListAdapter<ItemType extends Shift> extends ItemListAdapter<ItemType> {

    private final ShiftUtil.Calculator calculator;

    ShiftListAdapter(Callbacks callbacks, ShiftUtil.Calculator calculator) {
        super(callbacks);
        this.calculator = calculator;
    }

    @Override
    @CallSuper
    boolean areContentsTheSame(@NonNull ItemType shift1, @NonNull ItemType shift2) {
        return super.areContentsTheSame(shift1, shift2) &&
                Comparators.equalShiftData(shift1.getShiftData(), shift2.getShiftData());
    }

    @Override
    final void bindViewHolder(@NonNull ItemType shift, ItemViewHolder holder) {
        holder.primaryIcon.setImageResource(ShiftUtil.getShiftIcon(calculator.getShiftType(shift.getShiftData())));
        holder.secondaryIcon.setImageResource(getSecondaryIcon(shift));
        holder.setText(
                DateTimeUtils.getFullDateString(shift.getShiftData().getStart().toLocalDate()),
                getTimeSpanString(shift),
                shift.getComment()
        );
    }

    @DrawableRes
    abstract int getSecondaryIcon(@NonNull ItemType shift);

    @NonNull
    abstract String getTimeSpanString(@NonNull ItemType shift);

}
