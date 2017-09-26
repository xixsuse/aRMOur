package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.util.DateTimeUtils;

public final class RosteredShiftListAdapter extends ShiftListAdapter<RosteredShift> {
    public RosteredShiftListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    boolean areContentsTheSame(@NonNull RosteredShift shift1, @NonNull RosteredShift shift2) {
        return super.areContentsTheSame(shift1, shift2) &&
                (shift1.getLoggedShiftData() == null ? shift2.getLoggedShiftData() == null : (shift2.getLoggedShiftData() != null && shift1.getLoggedShiftData().getStart().toLocalDateTime().isEqual(shift2.getLoggedShiftData().getStart().toLocalDateTime()) && shift1.getLoggedShiftData().getEnd().toLocalDateTime().isEqual(shift2.getLoggedShiftData().getEnd().toLocalDateTime()))) &&
                shift1.getCompliance().isCompliant() == shift2.getCompliance().isCompliant();
    }

    @Override
    int getSecondaryIcon(@NonNull RosteredShift shift) {
        return RosteredShift.Compliance.getComplianceIcon(shift.getCompliance().isCompliant());
    }

    @Nullable
    @Override
    String getThirdLine(@NonNull RosteredShift shift) {
        String comment = super.getThirdLine(shift);
        if (shift.getLoggedShiftData() == null) return comment;
        String loggedShiftString = DateTimeUtils.getTimeSpanString(shift.getLoggedShiftData().getStart().toLocalDateTime(), shift.getLoggedShiftData().getEnd().toLocalDateTime());
        if (comment == null) return loggedShiftString;
        return loggedShiftString + '\n' + comment;
    }

}