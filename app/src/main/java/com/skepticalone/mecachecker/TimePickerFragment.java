package com.skepticalone.mecachecker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public static final String START = "START";
    public static final String HOUR = "HOUR";
    public static final String MINUTE = "MINUTE";
    public static final String TIME_PICKER_FRAGMENT = "TimePickerFragment";
    private Callback mCallback;

    public static TimePickerFragment create(boolean start, int hour, int minute){
        Bundle arguments = new Bundle();
        arguments.putBoolean(START, start);
        arguments.putInt(HOUR, hour);
        arguments.putInt(MINUTE, minute);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (Callback) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TIME_PICKER_FRAGMENT, "onCreateDialog: ");
        return new TimePickerDialog(
                getActivity(),
                this,
                getArguments().getInt(HOUR),
                getArguments().getInt(MINUTE),
                true
        );
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (getArguments().getBoolean(START)) mCallback.onStartTimeSet(hourOfDay, minute);
        else mCallback.onEndTimeSet(hourOfDay, minute);
    }

    public interface Callback {
        void onStartTimeSet(int hourOfDay, int minute);
        void onEndTimeSet(int hourOfDay, int minute);
    }
}