package com.skepticalone.mecachecker.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public final class RosteredShiftDatePickerDialogFragment extends ShiftDatePickerDialogFragment {

    private static final String
            IS_LOGGED = "IS_LOGGED",
            LOGGED_START_HOURS = "LOGGED_START_HOURS",
            LOGGED_START_MINUTES = "LOGGED_START_MINUTES",
            LOGGED_END_HOURS = "LOGGED_END_HOURS",
            LOGGED_END_MINUTES = "LOGGED_END_MINUTES";

    public static RosteredShiftDatePickerDialogFragment newInstance(long id, @NonNull ShiftData shiftData, @Nullable ShiftData loggedShiftData) {
        Bundle args = getArgs(id, shiftData);
        if (loggedShiftData == null) {
            args.putBoolean(IS_LOGGED, false);
        } else {
            args.putBoolean(IS_LOGGED, true);
            LocalTime loggedStart = loggedShiftData.getStart().toLocalTime(), loggedEnd = loggedShiftData.getEnd().toLocalTime();
            args.putInt(LOGGED_START_HOURS, loggedStart.getHourOfDay());
            args.putInt(LOGGED_START_MINUTES, loggedStart.getMinuteOfHour());
            args.putInt(LOGGED_END_HOURS, loggedEnd.getHourOfDay());
            args.putInt(LOGGED_END_MINUTES, loggedEnd.getMinuteOfHour());
        }
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
        Bundle arguments = getArguments();
        LocalTime loggedStart, loggedEnd;
        if (arguments.getBoolean(IS_LOGGED)) {
            loggedStart = new LocalTime(arguments.getInt(LOGGED_START_HOURS), arguments.getInt(LOGGED_START_MINUTES));
            loggedEnd = new LocalTime(arguments.getInt(LOGGED_END_HOURS), arguments.getInt(LOGGED_END_MINUTES));
        } else {
            loggedStart = loggedEnd = null;
        }
        callbacks.setShiftTimes(getItemId(), newDate, start, end, loggedStart, loggedEnd);
    }
}
