package com.skepticalone.mecachecker.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

abstract class ShiftTimePickerDialogFragment extends TimePickerDialogFragment {

    private static final String
            IS_START = "IS_START",
            START_MILLIS = "START_MILLIS",
            END_MILLIS = "END_MILLIS";

    static Bundle getArgs(long id, boolean isStart, @NonNull ShiftData shiftData) {
        Bundle args = getArgs(id, (isStart ? shiftData.getStart() : shiftData.getEnd()).toLocalTime());
        args.putBoolean(IS_START, isStart);
        args.putLong(START_MILLIS, shiftData.getStart().getMillis());
        args.putLong(END_MILLIS, shiftData.getEnd().getMillis());
        return args;
    }

    @Override
    final void saveTime(@NonNull LocalTime time) {
        Bundle arguments = getArguments();
        saveTime(time,
                arguments.getBoolean(IS_START),
                new ShiftData(new DateTime(arguments.getLong(START_MILLIS)), new DateTime(arguments.getLong(END_MILLIS))));
    }

    abstract void saveTime(@NonNull LocalTime time, boolean isStart, @NonNull ShiftData shiftData);

}
