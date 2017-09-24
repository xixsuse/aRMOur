package com.skepticalone.armour.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.LiveShiftConfig;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

abstract class ShiftListAdapter<Entity extends Shift> extends ItemListAdapter<Entity> {

    @NonNull
    private final LiveShiftConfig liveShiftConfig;

    ShiftListAdapter(@NonNull Callbacks callbacks, @NonNull LiveShiftConfig liveShiftConfig) {
        super(callbacks);
        this.liveShiftConfig = liveShiftConfig;
    }

    @Override
    @CallSuper
    boolean areContentsTheSame(@NonNull Entity shift1, @NonNull Entity shift2) {
        return super.areContentsTheSame(shift1, shift2) &&
                Comparators.equalShiftData(shift1.getRawShiftData(), shift2.getRawShiftData());
    }

    @Override
    final void bindViewHolder(@NonNull Entity shift, ItemViewHolder holder) {
        holder.primaryIcon.setImageResource(shift.getShiftType().getIcon());
        holder.secondaryIcon.setImageResource(getSecondaryIcon(shift));
        final ZoneId zoneId = liveShiftConfig.getFreshZoneId(holder.getContext());
        final ZonedDateTime
                start = shift.getRawShiftData().getStart().atZone(zoneId),
                end = shift.getRawShiftData().getEnd().atZone(zoneId);
        holder.setText(
                DateTimeUtils.getFullDateString(start.toLocalDate()),
                DateTimeUtils.getTimeSpanString(start.toLocalDateTime(), end.toLocalDateTime()),
                getThirdLine(shift, zoneId)
        );
    }

    @DrawableRes
    abstract int getSecondaryIcon(@NonNull Entity shift);

    @Nullable
    abstract String getThirdLine(@NonNull Entity shift, @NonNull ZoneId zoneId);

}
