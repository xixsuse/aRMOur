package com.skepticalone.mecachecker.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

public final class RosteredShiftTimePickerDialogFragment extends ShiftTimePickerDialogFragment {

    public static RosteredShiftTimePickerDialogFragment newInstance(long id, boolean isStart, @NonNull ShiftData shiftData) {
        RosteredShiftTimePickerDialogFragment fragment = new RosteredShiftTimePickerDialogFragment();
        fragment.setArguments(getArgs(id, isStart, shiftData));
        return fragment;
    }

    private RosteredShiftTimeSetter callbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (RosteredShiftTimeSetter) getTargetFragment();
    }

    @Override
    void saveTime(@NonNull LocalTime time, boolean isStart, @NonNull DateTime start, @NonNull DateTime end) {
        callbacks.setShiftTimes(
                getItemId(),
                start.toLocalDate(),
                isStart ? time : start.toLocalTime(),
                isStart ? end.toLocalTime() : time
        );
    }
}
