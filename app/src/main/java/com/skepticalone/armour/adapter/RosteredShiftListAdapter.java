package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.skepticalone.armour.data.entity.RosteredShiftEntity;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

public final class RosteredShiftListAdapter extends ShiftListAdapter<RosteredShiftEntity> {

    public RosteredShiftListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    boolean areContentsTheSame(@NonNull RosteredShiftEntity shift1, @NonNull RosteredShiftEntity shift2) {
        Log.i("Compare", String.format("id %d: starts: %s, wasCompliant: %s, isCompliant: %s", shift1.getId(), shift1.getShiftData().getStart().toString(), shift1.isCompliant(), shift2.isCompliant()));
        return super.areContentsTheSame(shift1, shift2) &&
                shift1.isCompliant() == shift2.isCompliant() &&
                (shift1.getLoggedShiftData() == null ? shift2.getLoggedShiftData() == null : (shift2.getLoggedShiftData() != null && Comparators.equalShiftData(shift1.getLoggedShiftData(), shift2.getLoggedShiftData())));
    }

    @Override
    int getSecondaryIcon(@NonNull RosteredShiftEntity shift) {
        return RosteredShiftEntity.getComplianceIcon(shift.isCompliant());
    }

    @Nullable
    @Override
    String getThirdLine(@NonNull RosteredShiftEntity shift) {
        return shift.getLoggedShiftData() == null ? null : DateTimeUtils.getTimeSpanString(shift.getLoggedShiftData());
    }

}