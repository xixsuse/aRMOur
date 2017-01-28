package com.skepticalone.mecachecker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String IS_START = "IS_START";
    private static final String HOUR = "HOUR";
    private static final String MINUTE = "MINUTE";

    private OnShiftTimeSetListener mListener;

    public static TimePickerFragment create(boolean isStart, int hour, int minute){
        Bundle arguments = new Bundle();
        arguments.putBoolean(IS_START, isStart);
        arguments.putInt(HOUR, hour);
        arguments.putInt(MINUTE, minute);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnShiftTimeSetListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TimePickerDialog dialog = new TimePickerDialog(
                getActivity(),
                this,
                getArguments().getInt(HOUR),
                getArguments().getInt(MINUTE),
                false
        ) {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                super.onTimeChanged(view, hourOfDay, minute);
            }
        };
        dialog.setTitle(getArguments().getBoolean(IS_START) ? R.string.start : R.string.end);
        return dialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        minute -= minute % ShiftActivity.MINUTES_PER_STEP;
        if (getArguments().getBoolean(IS_START)) mListener.onStartTimeSet(hourOfDay, minute);
        else mListener.onEndTimeSet(hourOfDay, minute);
    }

    public interface OnShiftTimeSetListener {
        void onStartTimeSet(int hourOfDay, int minute);
        void onEndTimeSet(int hourOfDay, int minute);
    }
}