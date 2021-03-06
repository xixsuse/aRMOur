package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

abstract class ShiftListAdapter<FinalItem extends Shift> extends ItemListAdapter<FinalItem> {

    ShiftListAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    @CallSuper
    boolean areContentsTheSame(@NonNull FinalItem shift1, @NonNull FinalItem shift2) {
        return shift1.getShiftType() == shift2.getShiftType() &&
                shift1.getShiftData().getStart().toLocalDateTime().isEqual(shift2.getShiftData().getStart().toLocalDateTime()) &&
                shift1.getShiftData().getEnd().toLocalDateTime().isEqual(shift2.getShiftData().getEnd().toLocalDateTime()) &&
                Comparators.equalStrings(shift1.getComment(), shift2.getComment());
    }

    @Override
    final int getPrimaryIcon(@NonNull FinalItem shift) {
        return shift.getShiftType().getIcon();
    }

    @NonNull
    @Override
    final String getFirstLine(@NonNull FinalItem shift) {
        return DateTimeUtils.getFullDateString(shift.getShiftData().getStart().toLocalDate());
    }

    @NonNull
    @Override
    @CallSuper
    String getSecondLine(@NonNull FinalItem shift) {
        return DateTimeUtils.getTimeSpanString(shift.getShiftData().getStart().toLocalDateTime(), shift.getShiftData().getEnd().toLocalDateTime());
    }

}
