package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.model.Shift;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftUtil;

abstract class ShiftDetailAdapterHelper {

    private final Callbacks callbacks;
    private final ShiftUtil.Calculator calculator;

    ShiftDetailAdapterHelper(Callbacks callbacks, ShiftUtil.Calculator calculator) {
        this.callbacks = callbacks;
        this.calculator = calculator;
    }

    void onItemUpdated(@NonNull Shift oldShift, @NonNull Shift newShift, RecyclerView.Adapter adapter) {
        if (!oldShift.getShift().getStart().toLocalDate().isEqual(newShift.getShift().getStart().toLocalDate())) {
            adapter.notifyItemChanged(getRowNumberDate());
        }
        if (
                !oldShift.getShift().getStart().toLocalTime().isEqual(newShift.getShift().getStart().toLocalTime()) ||
                !oldShift.getShift().getEnd().toLocalTime().isEqual(newShift.getShift().getEnd().toLocalTime())
        ) {
            adapter.notifyItemChanged(getRowNumberStart());
            adapter.notifyItemChanged(getRowNumberEnd());
            adapter.notifyItemChanged(getRowNumberShiftType());
        }
    }

    boolean bindViewHolder(@NonNull final Shift item, ItemViewHolder holder, int position) {
        if (position == getRowNumberDate()) {
            holder.setupPlain(R.drawable.ic_calendar_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeDate(item.getId(), item.getShift());
                }
            });
            holder.setText(holder.getText(R.string.date), DateTimeUtils.getFullDateString(item.getShift().getStart().toLocalDate()));
            return true;
        } else if (position == getRowNumberStart()) {
            holder.setupPlain(R.drawable.ic_play_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTime(item.getId(), true, item.getShift());
                }
            });
            holder.setText(holder.getText(R.string.start), DateTimeUtils.getStartTimeString(item.getShift().getStart().toLocalTime()));
            return true;
        } else if (position == getRowNumberEnd()) {
            holder.setupPlain(R.drawable.ic_stop_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTime(item.getId(), false, item.getShift());
                }
            });
            holder.setText(holder.getText(R.string.end), DateTimeUtils.getEndTimeString(item.getShift().getEnd(), item.getShift().getStart().toLocalDate()));
            return true;
        } else if (position == getRowNumberShiftType()) {
            ShiftUtil.ShiftType shiftType = calculator.getShiftType(item.getShift());
            holder.setupPlain(ShiftUtil.getShiftIcon(shiftType), null);
            holder.setText(holder.getText(R.string.shift_type), holder.getText(ShiftUtil.getShiftTitle(shiftType)));
            return true;
        } else return false;
    }

    abstract int getRowNumberDate();
    abstract int getRowNumberStart();
    abstract int getRowNumberEnd();
    abstract int getRowNumberShiftType();

    public interface Callbacks {
        void changeDate(long id, @NonNull ShiftData shiftData);
        void changeTime(long id, boolean isStart, @NonNull ShiftData shiftData);
    }
}
