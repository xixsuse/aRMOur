package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.skepticalone.armour.data.model.RosteredShift;

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
            list.add(new DurationBetweenShiftsBinder(callbacks, rosteredShift.getCompliance().getDurationBetweenShifts()));
        }
        list.add(new DurationOverDayBinder(callbacks, rosteredShift.getCompliance().getDurationOverDay()));
        list.add(new DurationOverWeekBinder(callbacks, rosteredShift.getCompliance().getDurationOverWeek()));
        list.add(new DurationOverFortnightBinder(callbacks, rosteredShift.getCompliance().getDurationOverFortnight()));
        if (rosteredShift.getCompliance().getConsecutiveDays() != null) {
            list.add(new ConsecutiveDaysBinder(callbacks, rosteredShift.getCompliance().getConsecutiveDays()));
        }
        if (rosteredShift.getCompliance().getLongDay() != null) {
            list.add(new LongDayBinder(callbacks, rosteredShift.getCompliance().getLongDay()));
        }
        if (rosteredShift.getCompliance().getNight() != null) {
            list.add(new NightBinder(callbacks, rosteredShift.getCompliance().getNight()));
        }
        if (rosteredShift.getCompliance().getRecoveryFollowingNights() != null) {
            list.add(new RecoveryFollowingNightsBinder(callbacks, rosteredShift.getCompliance().getRecoveryFollowingNights()));
        }
        if (rosteredShift.getCompliance().getWeekend() != null) {
            list.add(new WeekendBinder(callbacks, rosteredShift.getCompliance().getWeekend()));
        }
        if (rosteredShift.getCompliance().getRosteredDayOff() != null) {
            list.add(new RosteredDayOffBinder(callbacks, rosteredShift.getCompliance().getRosteredDayOff()));
        }
        return list;
    }

    public interface Callbacks extends DateBinder.Callbacks, ToggleLoggedBinder.Callbacks, ShiftDataBinder.Callbacks, CommentBinder.Callbacks, ComplianceDataBinder.Callbacks {
    }

}
