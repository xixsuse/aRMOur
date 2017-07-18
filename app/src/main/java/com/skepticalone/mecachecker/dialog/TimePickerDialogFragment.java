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

    static Bundle getArgs(long id, @NonNull LocalTime time) {
        Bundle args = getArgs(id);
        args.putInt(HOUR_OF_DAY, time.getHourOfDay());
        args.putInt(MINUTE_OF_HOUR, time.getMinuteOfHour());
        return args;
    }

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        return new TimePickerDialog(getActivity(), this,
                arguments.getInt(HOUR_OF_DAY),
                arguments.getInt(MINUTE_OF_HOUR),
                DateFormat.is24HourFormat(getActivity())
        );
    }

    @Override
    public final void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfHour) {
        saveTime(new LocalTime(hourOfDay, minuteOfHour));
    }

    abstract void saveTime(@NonNull LocalTime time);

}
