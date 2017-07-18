package com.skepticalone.mecachecker.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public final class AdditionalShiftDatePickerDialogFragment extends ShiftDatePickerDialogFragment {

    public static AdditionalShiftDatePickerDialogFragment newInstance(long id, @NonNull ShiftData shiftData) {
        AdditionalShiftDatePickerDialogFragment fragment = new AdditionalShiftDatePickerDialogFragment();
        fragment.setArguments(getArgs(id, shiftData));
        return fragment;
    }

    private AdditionalShiftTimeSetter callbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (AdditionalShiftTimeSetter) getTargetFragment();
    }

    @Override
    void saveDate(@NonNull LocalDate newDate, @NonNull LocalTime start, @NonNull LocalTime end) {
        callbacks.setShiftTimes(getItemId(), newDate, start, end);
    }
}
