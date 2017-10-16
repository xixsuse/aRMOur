package com.skepticalone.armour.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.LocalDate;

abstract class ShiftDetailAdapterHelper<FinalItem extends Shift> extends DateDetailAdapterHelper<FinalItem> {

    ShiftDetailAdapterHelper(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    abstract int getRowNumberStart();
    abstract int getRowNumberEnd();
    abstract int getRowNumberShiftType();
    abstract void changeTime(boolean start);

    @NonNull
    @Override
    final LocalDate getDate(@NonNull FinalItem shift) {
        return shift.getShiftData().getStart().toLocalDate();
    }

    @Override
    @CallSuper
    void onItemUpdated(@NonNull FinalItem oldShift, @NonNull FinalItem newShift, @NonNull RecyclerView.Adapter adapter) {
        super.onItemUpdated(oldShift, newShift, adapter);
        if (!oldShift.getShiftData().getStart().toLocalTime().equals(newShift.getShiftData().getStart().toLocalTime())) {
            adapter.notifyItemChanged(getRowNumberStart());
        }
        if (!oldShift.getShiftData().getEnd().toLocalTime().equals(newShift.getShiftData().getEnd().toLocalTime()) || (oldShift.getShiftData().getStart().toLocalDate().isEqual(oldShift.getShiftData().getEnd().toLocalDate()) ? !newShift.getShiftData().getStart().toLocalDate().isEqual(newShift.getShiftData().getEnd().toLocalDate()) : (newShift.getShiftData().getStart().toLocalDate().isEqual(newShift.getShiftData().getEnd().toLocalDate()) || !oldShift.getShiftData().getEnd().getDayOfWeek().equals(newShift.getShiftData().getEnd().getDayOfWeek())))) {
            adapter.notifyItemChanged(getRowNumberEnd());
        }
        if (oldShift.getShiftType() != newShift.getShiftType() || !oldShift.getShiftData().getDuration().equals(newShift.getShiftData().getDuration())) {
            adapter.notifyItemChanged(getRowNumberShiftType());
        }
    }

    @Override
    @CallSuper
    boolean bindViewHolder(@NonNull FinalItem shift, int position, ItemViewHolder holder, @NonNull ObservableAdapter adapter) {
        if (position == getRowNumberStart()) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_play_black_24dp);
            holder.setText(adapter.getContext().getString(R.string.start), DateTimeUtils.getTimeString(shift.getShiftData().getStart().toLocalTime()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeTime(true);
                }
            });
            return true;
        } else if (position == getRowNumberEnd()) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_stop_black_24dp);
            holder.setText(adapter.getContext().getString(R.string.end), DateTimeUtils.getEndTimeString(shift.getShiftData().getEnd().toLocalDateTime(), shift.getShiftData().getStart().toLocalDate()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeTime(false);
                }
            });
            return true;
        } else if (position == getRowNumberShiftType()) {
            holder.setupPlain();
            holder.setPrimaryIcon(shift.getShiftType().getIcon());
            holder.setText(adapter.getContext().getString(shift.getShiftType().getSingularTitle()), DateTimeUtils.getDurationString(adapter.getContext(), shift.getShiftData().getDuration()));
            holder.itemView.setOnClickListener(null);
            return true;
        } else return super.bindViewHolder(shift, position, holder, adapter);
    }

}
