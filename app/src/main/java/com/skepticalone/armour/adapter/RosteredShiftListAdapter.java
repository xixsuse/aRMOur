package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.compliance.Compliance;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.util.DateTimeUtils;

public final class RosteredShiftListAdapter extends ShiftListAdapter<RosteredShift> {

    public RosteredShiftListAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context, callbacks);
    }

    @Override
    boolean areContentsTheSame(@NonNull RosteredShift shift1, @NonNull RosteredShift shift2) {
        return super.areContentsTheSame(shift1, shift2) &&
                (shift1.getLoggedShiftData() == null ? shift2.getLoggedShiftData() == null : (shift2.getLoggedShiftData() != null && shift1.getLoggedShiftData().getStart().toLocalDateTime().isEqual(shift2.getLoggedShiftData().getStart().toLocalDateTime()) && shift1.getLoggedShiftData().getEnd().toLocalDateTime().isEqual(shift2.getLoggedShiftData().getEnd().toLocalDateTime()))) &&
                shift1.getCompliance().isCompliant() == shift2.getCompliance().isCompliant();
    }

    @Override
    int getSecondaryIcon(@NonNull RosteredShift shift) {
        return Compliance.getComplianceIcon(shift.getCompliance().isCompliant());
    }

    @NonNull
    @Override
    String getSecondLine(@NonNull RosteredShift shift) {
        String rosteredShiftSpan = super.getSecondLine(shift);
        if (shift.getLoggedShiftData() == null) {
            return rosteredShiftSpan;
        }
        return getContext().getString(R.string.logged_format, rosteredShiftSpan, DateTimeUtils.getTimeSpanString(shift.getLoggedShiftData().getStart().toLocalDateTime(), shift.getLoggedShiftData().getEnd().toLocalDateTime()));
    }

}