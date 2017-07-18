package com.skepticalone.mecachecker.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import org.joda.time.LocalTime;

abstract class TimePickerDialogFragment extends AbstractDialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String
            HOUR_OF_DAY = "HOUR_OF_DAY",
            MINUTE_OF_HOUR = "MINUTE_OF_HOUR";

//    private ShiftData shiftData;

    static Bundle getArgs(long itemId, @NonNull LocalTime time) {
        Bundle args = getArgs(itemId);
        args.putInt(HOUR_OF_DAY, time.getHourOfDay());
        args.putInt(MINUTE_OF_HOUR, time.getMinuteOfHour());
        return args;
    }

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
//        Bundle args = getArguments();
//        LocalTime time = new DateTime(args.getLong(args.getBoolean(IS_START) ? START_MILLIS : END_MILLIS)).toLocalTime();
//        LocalTime time = new LocalTime(args.getInt(HOUR_OF_DAY), args.getInt(MINUTE_OF_HOUR));
        return new TimePickerDialog(getActivity(), this,
                getArguments().getInt(HOUR_OF_DAY),
                getArguments().getInt(MINUTE_OF_HOUR),
                DateFormat.is24HourFormat(getActivity())
        );
    }

    @Override
    public final void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfHour) {
        setTime(new LocalTime(hourOfDay, minuteOfHour));
//        Bundle arguments = getArguments();
//        ShiftData shiftData = new ShiftData(new DateTime(arguments.getLong(START_MILLIS)), new DateTime(arguments.getLong(END_MILLIS)));
//        saveShiftTimes(newTime, arguments.getBoolean(IS_START), shiftData);
//        if (arguments.getBoolean(IS_START)) {
//            saveStart(new LocalDate(arguments.getLong(START_MILLIS)).toDateTime(newTime), new LocalTime(arguments.getLong(END_MILLIS)));
//        } else {
//            saveEnd(newTime);
//        }
    }

    abstract void setTime(@NonNull LocalTime time);

//    abstract void saveShiftTimes(@NonNull LocalTime newTime, boolean isStart, @NonNull LocalDate date, @NonNull);

}
