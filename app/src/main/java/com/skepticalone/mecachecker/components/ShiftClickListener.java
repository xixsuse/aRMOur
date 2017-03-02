package com.skepticalone.mecachecker.components;

interface ShiftClickListener {
    void onRawShiftClicked(long shiftId);

    void onShiftWithComplianceClicked(long shiftId);
}
