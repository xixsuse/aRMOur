package com.skepticalone.mecachecker.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.ShiftData;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public final class ShiftDatePickerDialogFragment extends AbstractDatePickerDialogFragment {

    private static final String
            START_HOURS = "START_HOURS",
            START_MINUTES = "START_MINUTES",
            END_HOURS = "END_HOURS",
            END_MINUTES = "END_MINUTES";

    public static ShiftDatePickerDialogFragment newInstance(long itemId, @NonNull ShiftData shiftData) {
        Bundle args = getArgs(itemId, shiftData.getStart().toLocalDate());
        LocalTime start = shiftData.getStart().toLocalTime(), end = shiftData.getEnd().toLocalTime();
        args.putInt(START_HOURS, start.getHourOfDay());
        args.putInt(START_MINUTES, start.getMinuteOfHour());
        args.putInt(END_HOURS, end.getHourOfDay());
        args.putInt(END_MINUTES, end.getMinuteOfHour());
        ShiftDatePickerDialogFragment fragment = new ShiftDatePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AdditionalShiftTimeSetter callbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (AdditionalShiftTimeSetter) getTargetFragment();
    }

    @Override
    void saveDate(@NonNull LocalDate newDate) {
        Bundle arguments = getArguments();
        callbacks.setShiftTimes(
                getItemId(),
                newDate,
                new LocalTime(arguments.getInt(START_HOURS), arguments.getInt(START_MINUTES)),
                new LocalTime(arguments.getInt(END_HOURS), arguments.getInt(END_MINUTES))
        );
    }

}
