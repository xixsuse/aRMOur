package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class RosteredShiftListAdapter extends ShiftListAdapter<RosteredShiftEntity> {

    public RosteredShiftListAdapter(@NonNull Callbacks callbacks, @NonNull ShiftUtil.Calculator calculator) {
        super(callbacks, calculator);
    }

    @Override
    boolean areContentsTheSame(@NonNull RosteredShiftEntity shift1, @NonNull RosteredShiftEntity shift2) {
        return super.areContentsTheSame(shift1, shift2) &&
                (shift1.getLoggedShiftData() == null ? shift2.getLoggedShiftData() != null : (shift2.getLoggedShiftData() == null || Comparators.equalShiftData(shift1.getLoggedShiftData(), shift2.getLoggedShiftData())));
    }

    @Override
    int getSecondaryIcon(@NonNull RosteredShiftEntity shift) {
        return shift.getComplianceIcon();
    }

    @NonNull
    @Override
    String getTimeSpanString(@NonNull RosteredShiftEntity shift) {
        return DateTimeUtils.getTimeSpanString(shift.getShiftData(), shift.getLoggedShiftData());
    }

}