package com.skepticalone.armour.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

abstract class ShiftListAdapter<Entity extends Shift> extends ItemListAdapter<Entity> {

    ShiftListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    @CallSuper
    boolean areContentsTheSame(@NonNull Entity shift1, @NonNull Entity shift2) {
        return shift1.getShiftType() == shift2.getShiftType() &&
                shift1.getShiftData().getStart().toLocalDateTime().isEqual(shift2.getShiftData().getStart().toLocalDateTime()) &&
                shift1.getShiftData().getEnd().toLocalDateTime().isEqual(shift2.getShiftData().getEnd().toLocalDateTime()) &&
                Comparators.equalStrings(shift1.getComment(), shift2.getComment());
    }

    @Override
    final void bindViewHolder(@NonNull Entity shift, ItemViewHolder holder, boolean selected) {
        holder.primaryIcon.setImageResource(selected ? R.drawable.ic_check_circle_24dp : shift.getShiftType().getIcon());
        holder.secondaryIcon.setImageResource(getSecondaryIcon(shift));
        holder.setText(
                DateTimeUtils.getFullDateString(shift.getShiftData().getStart().toLocalDate()),
                DateTimeUtils.getTimeSpanString(shift.getShiftData().getStart().toLocalDateTime(), shift.getShiftData().getEnd().toLocalDateTime()),
                getThirdLine(shift)
        );
    }

    @DrawableRes
    abstract int getSecondaryIcon(@NonNull Entity shift);

    @Nullable
    @CallSuper
    String getThirdLine(@NonNull Entity shift) {
        return shift.getComment();
    }

}
