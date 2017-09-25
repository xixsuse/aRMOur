package com.skepticalone.armour.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.LocalDate;

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
        return shift.getShiftData().getStart().toLocalDate();
    }

    @Override
    @CallSuper
    void onItemUpdated(@NonNull Entity oldShift, @NonNull Entity newShift, @NonNull RecyclerView.Adapter adapter) {
        super.onItemUpdated(oldShift, newShift, adapter);
        final boolean startTimeChanged = !oldShift.getShiftData().getStart().toLocalTime().equals(newShift.getShiftData().getStart().toLocalTime()),
                endTimeChanged = !oldShift.getShiftData().getEnd().toLocalTime().equals(newShift.getShiftData().getEnd().toLocalTime());
        if (startTimeChanged) {
            adapter.notifyItemChanged(getRowNumberStart());
        }
        if (endTimeChanged || !oldShift.getShiftData().getStart().toLocalDate().isEqual(newShift.getShiftData().getStart().toLocalDate())) {
            adapter.notifyItemChanged(getRowNumberEnd());
        }
        if (startTimeChanged || endTimeChanged || !oldShift.getShiftData().getDuration().equals(newShift.getShiftData().getDuration())) {
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
            holder.setText(holder.getText(R.string.start), DateTimeUtils.getTimeString(shift.getShiftData().getStart().toLocalTime()));
            return true;
        } else if (position == getRowNumberEnd()) {
            holder.setupPlain(R.drawable.ic_stop_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeTime(false);
                }
            });
            holder.setText(holder.getText(R.string.end), DateTimeUtils.getEndTimeString(shift.getShiftData().getEnd().toLocalDateTime(), shift.getShiftData().getStart().toLocalDate()));
            return true;
        } else if (position == getRowNumberShiftType()) {
            holder.setupPlain(shift.getShiftType().getIcon(), null);
            holder.setText(holder.getText(shift.getShiftType().getSingularTitle()), DateTimeUtils.getDurationString(shift.getShiftData().getDuration()));
            return true;
        } else return super.bindViewHolder(shift, holder, position);
    }

}
