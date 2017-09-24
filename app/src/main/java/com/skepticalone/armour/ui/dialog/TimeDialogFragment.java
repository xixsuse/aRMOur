package com.skepticalone.armour.ui.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.skepticalone.armour.data.entity.LiveShiftConfig;
import com.skepticalone.armour.data.entity.ShiftData;
import com.skepticalone.armour.util.AppConstants;

import org.threeten.bp.LocalTime;

abstract class TimeDialogFragment<Entity> extends DialogFragment<Entity> implements TimePickerDialog.OnTimeSetListener {

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
    final void onUpdateView(@NonNull Entity item) {
        ShiftData shiftData = getShiftDataForDisplay(item);
        LocalTime time = (start ? shiftData.getStart() : shiftData.getEnd()).atZone(LiveShiftConfig.getInstance(getActivity()).getFreshZoneId(getActivity())).toLocalTime();
        timePickerDialog.updateTime(time.getHour(), time.getMinute());
    }

    @Override
    public final void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfHour) {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            saveNewTime(LocalTime.of(hourOfDay, AppConstants.getSteppedMinutes(minuteOfHour)), start);
        }
    }

    abstract void saveNewTime(@NonNull LocalTime time, boolean isStart);

}
