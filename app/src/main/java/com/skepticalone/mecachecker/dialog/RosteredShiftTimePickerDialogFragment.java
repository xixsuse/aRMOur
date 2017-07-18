package com.skepticalone.mecachecker.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.LocalTime;

public final class RosteredShiftTimePickerDialogFragment extends ShiftTimePickerDialogFragment {

    private static final String
            IS_LOGGED = "IS_LOGGED",
            LOGGED_START_MILLIS = "LOGGED_START_MILLIS",
            LOGGED_END_MILLIS = "LOGGED_END_MILLIS";

    public static RosteredShiftTimePickerDialogFragment newInstance(long id, boolean isStart, boolean isLogged, @NonNull ShiftData shiftData, @Nullable ShiftData loggedShiftData) {
        Bundle args = getArgs(id, isStart, shiftData);
        if (loggedShiftData == null) {
            args.putBoolean(IS_LOGGED, false);
        } else {
            args.putBoolean(IS_LOGGED, true);
            args.putLong(LOGGED_START_MILLIS, loggedShiftData.getStart().getMillis());
            args.putLong(LOGGED_END_MILLIS, loggedShiftData.getEnd().getMillis());
        }
        RosteredShiftTimePickerDialogFragment fragment = new RosteredShiftTimePickerDialogFragment();
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
    void saveTime(@NonNull LocalTime time, boolean isStart, @NonNull ShiftData shiftData) {
        Bundle arguments = getArguments();
        boolean isLogged = arguments.getBoolean(IS_LOGGED);
        LocalTime loggedStart, loggedEnd;
        if (isLogged) {
            loggedStart = new LocalTime(arguments.getLong(LOGGED_START_MILLIS));
            loggedEnd = new LocalTime(arguments.getLong(LOGGED_END_MILLIS));
        } else {
            loggedStart = loggedEnd = null;
        }
        callbacks.setShiftTimes(
                getItemId(),
                shiftData.getStart().toLocalDate(),
                isStart ? time : shiftData.getStart().toLocalTime(),
                isStart ? shiftData.getEnd().toLocalTime() : time,
                loggedStart,
                loggedEnd
        );
    }
}
