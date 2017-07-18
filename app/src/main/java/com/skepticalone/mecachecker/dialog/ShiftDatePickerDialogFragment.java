package com.skepticalone.mecachecker.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

abstract class ShiftDatePickerDialogFragment extends DatePickerDialogFragment {

    private static final String
            START_HOURS = "START_HOURS",
            START_MINUTES = "START_MINUTES",
            END_HOURS = "END_HOURS",
            END_MINUTES = "END_MINUTES";

    static Bundle getArgs(long id, @NonNull ShiftData shiftData) {
        Bundle args = getArgs(id, shiftData.getStart().toLocalDate());
        LocalTime start = shiftData.getStart().toLocalTime(), end = shiftData.getEnd().toLocalTime();
        args.putInt(START_HOURS, start.getHourOfDay());
        args.putInt(START_MINUTES, start.getMinuteOfHour());
        args.putInt(END_HOURS, end.getHourOfDay());
        args.putInt(END_MINUTES, end.getMinuteOfHour());
        return args;
    }

    @Override
    final void saveDate(@NonNull LocalDate newDate) {
        Bundle arguments = getArguments();
        saveDate(
                newDate,
                new LocalTime(arguments.getInt(START_HOURS), arguments.getInt(START_MINUTES)),
                new LocalTime(arguments.getInt(END_HOURS), arguments.getInt(END_MINUTES))
        );
    }

    abstract void saveDate(@NonNull LocalDate newDate, @NonNull LocalTime start, @NonNull LocalTime end);

}
