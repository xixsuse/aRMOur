package com.skepticalone.armour.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

abstract class ShiftListAdapter<Entity extends Shift> extends ItemListAdapter<Entity> {

    ShiftListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    @CallSuper
    boolean areContentsTheSame(@NonNull Entity shift1, @NonNull Entity shift2) {
        return super.areContentsTheSame(shift1, shift2) &&
                Comparators.equalShiftData(shift1.getShiftData(), shift2.getShiftData());
    }

    @Override
    final void bindViewHolder(@NonNull Entity shift, ItemViewHolder holder) {
        holder.primaryIcon.setImageResource(shift.getShiftType().getIcon());
        holder.secondaryIcon.setImageResource(getSecondaryIcon(shift));
        final ZoneId zoneId = ZoneId.systemDefault();
        final ZonedDateTime
                start = shift.getShiftData().getStart().atZone(zoneId),
                end = shift.getShiftData().getEnd().atZone(zoneId);
        holder.setText(
                DateTimeUtils.getFullDateString(start.toLocalDate()),
                DateTimeUtils.getTimeSpanString(start.toLocalDateTime(), end.toLocalDateTime()),
                getThirdLine(shift)
        );
    }

    @DrawableRes
    abstract int getSecondaryIcon(@NonNull Entity shift);

    @Nullable
    abstract String getThirdLine(@NonNull Entity shift);

}
