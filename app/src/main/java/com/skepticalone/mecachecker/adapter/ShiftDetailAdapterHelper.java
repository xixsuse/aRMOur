package com.skepticalone.mecachecker.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Shift;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.LocalDate;

abstract class ShiftDetailAdapterHelper<Entity extends Shift> extends DateDetailAdapterHelper<Entity> {

    @NonNull
    private final ShiftUtil.Calculator calculator;

    ShiftDetailAdapterHelper(@NonNull Callbacks callbacks, @NonNull ShiftUtil.Calculator calculator) {
        super(callbacks);
        this.calculator = calculator;
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
        boolean startTimeChanged = !oldShift.getShiftData().getStart().toLocalTime().isEqual(newShift.getShiftData().getStart().toLocalTime()),
                endTimeChanged = !oldShift.getShiftData().getEnd().toLocalTime().isEqual(newShift.getShiftData().getEnd().toLocalTime());
        if (startTimeChanged) {
            adapter.notifyItemChanged(getRowNumberStart());
        }
        if (endTimeChanged || !oldShift.getShiftData().getStart().toLocalDate().isEqual(newShift.getShiftData().getStart().toLocalDate())) {
            adapter.notifyItemChanged(getRowNumberEnd());
        }
        if (startTimeChanged || endTimeChanged || !oldShift.getShiftData().getDuration().isEqual(newShift.getShiftData().getDuration())) {
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
            holder.setText(holder.getText(R.string.start), DateTimeUtils.getStartTimeString(shift.getShiftData().getStart().toLocalTime()));
            return true;
        } else if (position == getRowNumberEnd()) {
            holder.setupPlain(R.drawable.ic_stop_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeTime(false);
                }
            });
            holder.setText(holder.getText(R.string.end), DateTimeUtils.getEndTimeString(shift.getShiftData().getEnd(), shift.getShiftData().getStart().toLocalDate()));
            return true;
        } else if (position == getRowNumberShiftType()) {
            ShiftUtil.ShiftType shiftType = calculator.getSingleShiftType(shift.getShiftData());
            holder.setupPlain(ShiftUtil.getShiftIcon(shiftType), null);
            holder.setText(holder.getText(ShiftUtil.getShiftTitle(shiftType)), DateTimeUtils.getDurationString(shift.getShiftData().getDuration()));
            return true;
        } else return super.bindViewHolder(shift, holder, position);
    }

}
