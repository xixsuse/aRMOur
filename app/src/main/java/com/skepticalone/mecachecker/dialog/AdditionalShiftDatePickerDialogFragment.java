package com.skepticalone.mecachecker.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public final class AdditionalShiftDatePickerDialogFragment extends ShiftDatePickerDialogFragment {

    public static RosteredShiftDatePickerDialogFragment newInstance(long id, @NonNull ShiftData shiftData) {
        Bundle args = getArgs(id, shiftData);
        RosteredShiftDatePickerDialogFragment fragment = new RosteredShiftDatePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RosteredShiftTimeSetter callbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (RosteredShiftTimeSetter) getTargetFragment();
    }

    @Override
    void saveDate(@NonNull LocalDate newDate, @NonNull LocalTime start, @NonNull LocalTime end) {
        callbacks.setShiftTimes(getItemId(), newDate, start, end);
    }
}
