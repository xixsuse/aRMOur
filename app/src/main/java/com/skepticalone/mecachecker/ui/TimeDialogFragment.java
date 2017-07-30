package com.skepticalone.mecachecker.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;
import com.skepticalone.mecachecker.util.AppConstants;

import org.joda.time.LocalTime;

abstract class TimeDialogFragment<Entity, ViewModel extends ViewModelContract<Entity>> extends DialogFragment<Entity, ViewModel> implements TimePickerDialog.OnTimeSetListener {

    private static final String START = "START";
    private boolean start;
    private TimePickerDialog timePickerDialog;

    static Bundle getArgs(boolean start) {
        Bundle args = new Bundle();
        args.putBoolean(START, start);
        return args;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        start = getArguments().getBoolean(START);
    }

    @Override
    @NonNull
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        timePickerDialog = new TimePickerDialog(getActivity(), this,0, 0, DateFormat.is24HourFormat(getActivity()));
        return timePickerDialog;
    }

    @NonNull
    abstract ShiftData getShiftDataForDisplay(@NonNull Entity item);

    @Override
    public final void onChanged(@Nullable Entity item) {
        super.onChanged(item);
        if (item != null) {
            ShiftData shiftData = getShiftDataForDisplay(item);
            LocalTime time = (start ? shiftData.getStart() : shiftData.getEnd()).toLocalTime();
            timePickerDialog.updateTime(time.getHourOfDay(), time.getMinuteOfHour());
        }
    }

    @Override
    public final void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfHour) {
        onTimeSet(new LocalTime(hourOfDay, AppConstants.getSteppedMinutes(minuteOfHour)), start);
    }

    abstract void onTimeSet(@NonNull LocalTime time, boolean start);

}
