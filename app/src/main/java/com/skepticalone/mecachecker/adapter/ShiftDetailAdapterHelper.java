package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Shift;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftUtil;

abstract class ShiftDetailAdapterHelper<ShiftType extends Shift> {

    private final ShiftUtil.Calculator calculator;

    ShiftDetailAdapterHelper(ShiftUtil.Calculator calculator) {
        this.calculator = calculator;
    }

    void onItemUpdated(@NonNull ShiftType oldShift, @NonNull ShiftType newShift, RecyclerView.Adapter adapter) {
        if (!oldShift.getShiftData().getStart().toLocalDate().isEqual(newShift.getShiftData().getStart().toLocalDate())) {
            adapter.notifyItemChanged(getRowNumberDate());
        }
        if (
                !oldShift.getShiftData().getStart().toLocalTime().isEqual(newShift.getShiftData().getStart().toLocalTime()) ||
                !oldShift.getShiftData().getEnd().toLocalTime().isEqual(newShift.getShiftData().getEnd().toLocalTime())
        ) {
            adapter.notifyItemChanged(getRowNumberStart());
            adapter.notifyItemChanged(getRowNumberEnd());
            adapter.notifyItemChanged(getRowNumberShiftType());
        }
    }

    boolean bindViewHolder(@NonNull final ShiftType item, ItemViewHolder holder, int position) {
        if (position == getRowNumberDate()) {
            holder.setupPlain(R.drawable.ic_calendar_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeDate(item);
                }
            });
            holder.setText(holder.getText(R.string.date), DateTimeUtils.getFullDateString(item.getShiftData().getStart().toLocalDate()));
            return true;
        } else if (position == getRowNumberStart()) {
            holder.setupPlain(R.drawable.ic_play_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeTime(item, true);
                }
            });
            holder.setText(holder.getText(R.string.start), DateTimeUtils.getStartTimeString(item.getShiftData().getStart().toLocalTime()));
            return true;
        } else if (position == getRowNumberEnd()) {
            holder.setupPlain(R.drawable.ic_stop_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeTime(item, false);
                }
            });
            holder.setText(holder.getText(R.string.end), DateTimeUtils.getEndTimeString(item.getShiftData().getEnd(), item.getShiftData().getStart().toLocalDate()));
            return true;
        } else if (position == getRowNumberShiftType()) {
            ShiftUtil.ShiftType shiftType = calculator.getShiftType(item.getShiftData());
            holder.setupPlain(ShiftUtil.getShiftIcon(shiftType), null);
            holder.setText(holder.getText(R.string.shift_type), holder.getText(ShiftUtil.getShiftTitle(shiftType)));
            return true;
        } else return false;
    }

    abstract int getRowNumberDate();
    abstract int getRowNumberStart();
    abstract int getRowNumberEnd();
    abstract int getRowNumberShiftType();
    abstract void changeDate(@NonNull ShiftType shift);
    abstract void changeTime(@NonNull ShiftType shift, boolean isStart);
}
