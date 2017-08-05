package com.skepticalone.mecachecker.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.model.Shift;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftUtil;

abstract class ShiftListAdapter<Entity extends Shift> extends ItemListAdapter<Entity> {

    @NonNull
    private final ShiftUtil.Calculator calculator;

    ShiftListAdapter(@NonNull Callbacks callbacks, @NonNull ShiftUtil.Calculator calculator) {
        super(callbacks);
        this.calculator = calculator;
    }

    @Override
    @CallSuper
    boolean areContentsTheSame(@NonNull Entity shift1, @NonNull Entity shift2) {
        return super.areContentsTheSame(shift1, shift2) &&
                Comparators.equalShiftData(shift1.getShiftData(), shift2.getShiftData());
    }

    @Override
    final void bindViewHolder(@NonNull Entity shift, ItemViewHolder holder) {
        holder.primaryIcon.setImageResource(ShiftUtil.getShiftIcon(calculator.getSingleShiftType(shift.getShiftData())));
        holder.secondaryIcon.setImageResource(getSecondaryIcon(shift));
        holder.setText(
                DateTimeUtils.getFullDateString(shift.getShiftData().getStart().toLocalDate()),
                DateTimeUtils.getTimeSpanString(shift.getShiftData()),
                getThirdLine(shift)
        );
    }

    @DrawableRes
    abstract int getSecondaryIcon(@NonNull Entity shift);

    @Nullable
    abstract String getThirdLine(@NonNull Entity shift);

}
