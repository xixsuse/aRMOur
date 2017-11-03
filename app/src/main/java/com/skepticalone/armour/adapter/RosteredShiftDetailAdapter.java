package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RosteredShift;

import java.util.ArrayList;
import java.util.List;

public final class RosteredShiftDetailAdapter extends ItemDetailAdapter<RosteredShift> {

    @NonNull
    private final Callbacks callbacks;

    public RosteredShiftDetailAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context);
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    List<ItemViewHolder.Binder> getNewList(@NonNull RosteredShift shift) {
        List<ItemViewHolder.Binder> list = new ArrayList<>();
        list.add(new DateBinder(callbacks, shift.getShiftData().getStart().toLocalDate()));
        list.add(new ShiftDataBinder(callbacks, shift.getShiftData(), true, false));
        list.add(new ShiftDataBinder(callbacks, shift.getShiftData(), false, false));
        list.add(new ShiftTypeBinder(shift.getShiftType(), shift.getShiftData().getDuration()));
        list.add(new ToggleLoggedBinder(callbacks, shift.getLoggedShiftData()));
        if (shift.getLoggedShiftData() != null) {
            list.add(new ShiftDataBinder(callbacks, shift.getLoggedShiftData(), true, true));
            list.add(new ShiftDataBinder(callbacks, shift.getLoggedShiftData(), false, true));
        }
        list.add(new CommentBinder(callbacks, shift.getComment()));
        if (shift.getCompliance().getDurationBetweenShifts() != null) {
            list.add(new DurationBetweenShiftsBinder(callbacks, shift.getCompliance().getDurationBetweenShifts()));
        }
        list.add(new DurationOverDayBinder(callbacks, shift.getCompliance().getDurationOverDay()));
        list.add(new DurationOverWeekBinder(callbacks, shift.getCompliance().getDurationOverWeek()));
        list.add(new DurationOverFortnightBinder(callbacks, shift.getCompliance().getDurationOverFortnight()));
        if (shift.getCompliance().getLongDaysPerWeek() != null) {
            list.add(new LongDaysPerWeekBinder(callbacks, shift.getCompliance().getLongDaysPerWeek()));
        }
        if (shift.getCompliance().getConsecutiveDays() != null) {
            list.add(new ConsecutiveDaysBinder(callbacks, shift.getCompliance().getConsecutiveDays()));
        }
        if (shift.getCompliance().getConsecutiveWeekends() != null) {
            list.add(new ConsecutiveWeekendsBinder(callbacks, shift.getCompliance().getConsecutiveWeekends()));
        }
        if (shift.getCompliance().getConsecutiveNights() != null) {
            list.add(new ConsecutiveNightsBinder(callbacks, shift.getCompliance().getConsecutiveNights()));
        }
        if (shift.getCompliance().getRecoveryFollowingNights() != null) {
            list.add(new RecoveryFollowingNightsBinder(callbacks, shift.getCompliance().getRecoveryFollowingNights()));
        }
        if (shift.getCompliance().getRosteredDayOff() != null) {
            list.add(new RosteredDayOffBinder(callbacks, shift.getCompliance().getRosteredDayOff()));
        }
        return list;
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks, DateBinder.Callbacks, ToggleLoggedBinder.Callbacks, ShiftDataBinder.Callbacks, ComplianceDataBinder.Callbacks {
    }

}
