package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.skepticalone.armour.data.compliance.Row;
import com.skepticalone.armour.data.compliance.RowConsecutiveDays;
import com.skepticalone.armour.data.compliance.RowDurationBetweenShifts;
import com.skepticalone.armour.data.compliance.RowDurationOverDay;
import com.skepticalone.armour.data.compliance.RowDurationOverFortnight;
import com.skepticalone.armour.data.compliance.RowDurationOverWeek;
import com.skepticalone.armour.data.compliance.RowLongDay;
import com.skepticalone.armour.data.compliance.RowNight;
import com.skepticalone.armour.data.compliance.RowRecoveryFollowingNights;
import com.skepticalone.armour.data.compliance.RowRosteredDayOff;
import com.skepticalone.armour.data.compliance.RowWeekend;
import com.skepticalone.armour.data.model.CommentBinder;
import com.skepticalone.armour.data.model.DateBinder;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.ShiftDataBinder;
import com.skepticalone.armour.data.model.ShiftTypeBinder;
import com.skepticalone.armour.data.model.ToggleLoggedBinder;

import java.util.ArrayList;
import java.util.List;

public final class RosteredShiftDetailAdapter extends ObservableAdapter<RosteredShift> {

    @NonNull
    private final Callbacks callbacks;
    @Nullable
    private List<ItemViewHolder.Binder> mList;

    public RosteredShiftDetailAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context);
        this.callbacks = callbacks;
    }

    private static int getItemCount(@Nullable List<ItemViewHolder.Binder> list) {
        return list == null ? 0 : list.size();
    }

    @Override
    public final void onChanged(@Nullable RosteredShift rosteredShift) {
        final List<ItemViewHolder.Binder> newList = rosteredShift == null ? null : getNewList(rosteredShift);
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return getItemCount(mList);
            }

            @Override
            public int getNewListSize() {
                return getItemCount(newList);
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                //noinspection ConstantConditions
                return mList.get(oldItemPosition).areItemsTheSame(newList.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                //noinspection ConstantConditions
                return mList.get(oldItemPosition).areContentsTheSame(newList.get(newItemPosition));
            }

        }, false).dispatchUpdatesTo(this);
        mList = newList;
    }

    @Override
    public int getItemCount() {
        return getItemCount(mList);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        mList.get(position).onBindViewHolder(holder);
    }

    @NonNull
    private List<ItemViewHolder.Binder> getNewList(@NonNull RosteredShift rosteredShift) {
        List<ItemViewHolder.Binder> list = new ArrayList<>();
        list.add(new DateBinder(callbacks, rosteredShift.getShiftData().getStart().toLocalDate()));
        list.add(new ShiftDataBinder(callbacks, rosteredShift.getShiftData(), true, false));
        list.add(new ShiftDataBinder(callbacks, rosteredShift.getShiftData(), false, false));
        list.add(new ShiftTypeBinder(rosteredShift.getShiftType(), rosteredShift.getShiftData().getDuration()));
        list.add(new ToggleLoggedBinder(callbacks, rosteredShift.getLoggedShiftData()));
        if (rosteredShift.getLoggedShiftData() != null) {
            list.add(new ShiftDataBinder(callbacks, rosteredShift.getLoggedShiftData(), true, true));
            list.add(new ShiftDataBinder(callbacks, rosteredShift.getLoggedShiftData(), false, true));
        }
        list.add(new CommentBinder(callbacks, rosteredShift.getComment()));
        if (rosteredShift.getCompliance().getDurationBetweenShifts() != null) {
            list.add(new RowDurationBetweenShifts.Binder(callbacks, rosteredShift.getCompliance().getDurationBetweenShifts()));
        }
        list.add(new RowDurationOverDay.Binder(callbacks, rosteredShift.getCompliance().getDurationOverDay()));
        list.add(new RowDurationOverWeek.Binder(callbacks, rosteredShift.getCompliance().getDurationOverWeek()));
        list.add(new RowDurationOverFortnight.Binder(callbacks, rosteredShift.getCompliance().getDurationOverFortnight()));
        if (rosteredShift.getCompliance().getConsecutiveDays() != null) {
            list.add(new RowConsecutiveDays.Binder(callbacks, rosteredShift.getCompliance().getConsecutiveDays()));
        }
        if (rosteredShift.getCompliance().getLongDay() != null) {
            list.add(new RowLongDay.Binder(callbacks, rosteredShift.getCompliance().getLongDay()));
        }
        if (rosteredShift.getCompliance().getNight() != null) {
            list.add(new RowNight.Binder(callbacks, rosteredShift.getCompliance().getNight()));
        }
        if (rosteredShift.getCompliance().getRecoveryFollowingNights() != null) {
            list.add(new RowRecoveryFollowingNights.Binder(callbacks, rosteredShift.getCompliance().getRecoveryFollowingNights()));
        }
        if (rosteredShift.getCompliance().getWeekend() != null) {
            list.add(new RowWeekend.Binder(callbacks, rosteredShift.getCompliance().getWeekend()));
        }
        if (rosteredShift.getCompliance().getRosteredDayOff() != null) {
            list.add(new RowRosteredDayOff.Binder(callbacks, rosteredShift.getCompliance().getRosteredDayOff()));
        }
        return list;
    }

    public interface Callbacks extends DateBinder.Callbacks, ToggleLoggedBinder.Callbacks, ShiftDataBinder.Callbacks, CommentBinder.Callbacks, Row.Binder.Callbacks {
    }

}
