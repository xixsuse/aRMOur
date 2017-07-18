package com.skepticalone.mecachecker.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

public final class ShiftTimePickerDialogFragment extends TimePickerDialogFragment {

    private static final String
            IS_START = "IS_START",
            START_MILLIS = "START_MILLIS",
            END_MILLIS = "END_MILLIS";

    public static ShiftTimePickerDialogFragment newInstance(long itemId, boolean isStart, @NonNull ShiftData shiftData) {
        Bundle args = getArgs(itemId, (isStart ? shiftData.getStart() : shiftData.getEnd()).toLocalTime());
        args.putBoolean(IS_START, isStart);
        args.putLong(START_MILLIS, shiftData.getStart().getMillis());
        args.putLong(END_MILLIS, shiftData.getEnd().getMillis());
        ShiftTimePickerDialogFragment fragment = new ShiftTimePickerDialogFragment();
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
    void setTime(@NonNull LocalTime time) {
        boolean isStart = getArguments().getBoolean(IS_START);
        DateTime start = new DateTime(getArguments().getLong(START_MILLIS));
        callbacks.setShiftTimes(getItemId(), start.toLocalDate(), isStart ? time : start.toLocalTime(), isStart ? new LocalTime(getArguments().getLong(END_MILLIS)) : time);
    }

}
