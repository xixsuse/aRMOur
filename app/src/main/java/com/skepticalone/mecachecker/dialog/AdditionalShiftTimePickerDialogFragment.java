package com.skepticalone.mecachecker.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.LocalTime;

public final class AdditionalShiftTimePickerDialogFragment extends ShiftTimePickerDialogFragment {

    public static AdditionalShiftTimePickerDialogFragment newInstance(long id, boolean isStart, @NonNull ShiftData shiftData) {
        AdditionalShiftTimePickerDialogFragment fragment = new AdditionalShiftTimePickerDialogFragment();
        fragment.setArguments(getArgs(id, isStart, shiftData));
        return fragment;
    }

    private AdditionalShiftTimeSetter callbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (AdditionalShiftTimeSetter) getTargetFragment();
    }

    @Override
    void saveTime(@NonNull LocalTime time, boolean isStart, @NonNull ShiftData shiftData) {
        callbacks.setShiftTimes(
                getItemId(),
                shiftData.getStart().toLocalDate(),
                isStart ? time : shiftData.getStart().toLocalTime(),
                isStart ? shiftData.getEnd().toLocalTime() : time
        );
    }
}
