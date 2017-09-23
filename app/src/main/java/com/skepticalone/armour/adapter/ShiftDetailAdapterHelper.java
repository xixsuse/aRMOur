package com.skepticalone.armour.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

abstract class ShiftDetailAdapterHelper<Entity extends Shift> extends DateDetailAdapterHelper<Entity> {

    ShiftDetailAdapterHelper(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    abstract int getRowNumberStart();
    abstract int getRowNumberEnd();
    abstract int getRowNumberShiftType();
    abstract void changeTime(boolean start);

    @NonNull
    @Override
    final LocalDate getDate(@NonNull Entity shift) {
        return shift.getShiftData().getStart().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    @CallSuper
    void onItemUpdated(@NonNull Entity oldShift, @NonNull Entity newShift, @NonNull RecyclerView.Adapter adapter) {
        super.onItemUpdated(oldShift, newShift, adapter);
        final ZoneId zoneId = ZoneId.systemDefault();
        final ZonedDateTime
                oldStart = oldShift.getShiftData().getStart().atZone(zoneId),
                oldEnd = oldShift.getShiftData().getEnd().atZone(zoneId),
                newStart = newShift.getShiftData().getStart().atZone(zoneId),
                newEnd = newShift.getShiftData().getEnd().atZone(zoneId);
        final boolean startTimeChanged = !oldStart.toLocalTime().equals(newStart.toLocalTime()),
                endTimeChanged = !oldEnd.toLocalTime().equals(newEnd.toLocalTime());
        if (startTimeChanged) {
            adapter.notifyItemChanged(getRowNumberStart());
        }
        if (endTimeChanged || !oldStart.toLocalDate().isEqual(newStart.toLocalDate())) {
            adapter.notifyItemChanged(getRowNumberEnd());
        }
        if (startTimeChanged || endTimeChanged || !Duration.between(oldShift.getShiftData().getStart(), oldShift.getShiftData().getEnd()).equals(Duration.between(newShift.getShiftData().getStart(), newShift.getShiftData().getEnd()))) {
            adapter.notifyItemChanged(getRowNumberShiftType());
        }
    }

    @Override
    @CallSuper
    boolean bindViewHolder(@NonNull Entity shift, ItemViewHolder holder, int position) {
        if (position == getRowNumberStart()) {
            holder.setupPlain(R.drawable.ic_play_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeTime(true);
                }
            });
            holder.setText(holder.getText(R.string.start), DateTimeUtils.getTimeString(shift.getShiftData().getStart().atZone(ZoneId.systemDefault()).toLocalTime()));
            return true;
        } else if (position == getRowNumberEnd()) {
            holder.setupPlain(R.drawable.ic_stop_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeTime(false);
                }
            });
            holder.setText(holder.getText(R.string.end), DateTimeUtils.getEndTimeString(shift.getShiftData().getEnd().atZone(ZoneId.systemDefault()).toLocalDateTime(), shift.getShiftData().getStart().atZone(ZoneId.systemDefault()).toLocalDate()));
            return true;
        } else if (position == getRowNumberShiftType()) {
            holder.setupPlain(shift.getShiftType().getIcon(), null);
            holder.setText(holder.getText(shift.getShiftType().getSingularTitle()), DateTimeUtils.getDurationString(Duration.between(shift.getShiftData().getStart(), shift.getShiftData().getEnd())));
            return true;
        } else return super.bindViewHolder(shift, holder, position);
    }

}
