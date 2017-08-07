package com.skepticalone.mecachecker.ui.dialog;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.model.Shift;

import org.joda.time.LocalDate;

abstract class ShiftDateDialogFragment<Entity extends Shift> extends DateDialogFragment<Entity> {

    @NonNull
    @Override
    final LocalDate getDateForDisplay(@NonNull Entity shift) {
        return shift.getShiftData().getStart().toLocalDate();
    }

}
