package com.skepticalone.armour.ui.dialog;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.entity.LiveShiftTypeCalculator;
import com.skepticalone.armour.data.model.Shift;

import org.threeten.bp.LocalDate;

abstract class ShiftDateDialogFragment<Entity extends Shift> extends DateDialogFragment<Entity> {

    @NonNull
    @Override
    final LocalDate getDateForDisplay(@NonNull Entity shift) {
        return shift.getShiftData().getStart().atZone(LiveShiftTypeCalculator.getInstance(getActivity()).getZoneId(getActivity())).toLocalDate();
    }

}
