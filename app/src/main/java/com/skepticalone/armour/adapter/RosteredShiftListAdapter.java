package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

public final class RosteredShiftListAdapter extends ShiftListAdapter<RosteredShift> {
    public RosteredShiftListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    boolean areContentsTheSame(@NonNull RosteredShift shift1, @NonNull RosteredShift shift2) {
        return super.areContentsTheSame(shift1, shift2) &&
                shift1.getCompliance().isCompliant() == shift2.getCompliance().isCompliant() &&
                (shift1.getLoggedShiftData() == null ? shift2.getLoggedShiftData() == null : (shift2.getLoggedShiftData() != null && Comparators.equalShiftData(shift1.getLoggedShiftData(), shift2.getLoggedShiftData())));
    }

    @Override
    int getSecondaryIcon(@NonNull RosteredShift shift) {
        return RosteredShift.Compliance.getComplianceIcon(shift.getCompliance().isCompliant());
    }

    @Nullable
    @Override
    String getThirdLine(@NonNull RosteredShift shift) {
        return shift.getLoggedShiftData() == null ? null : DateTimeUtils.getTimeSpanString(shift.getLoggedShiftData().getStart().toLocalDateTime(), shift.getLoggedShiftData().getEnd().toLocalDateTime());
    }

}