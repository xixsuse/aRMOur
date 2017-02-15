package com.skepticalone.mecachecker.shift;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TimePicker;

import com.skepticalone.mecachecker.AppConstants;
import com.skepticalone.mecachecker.CheckedShift;
import com.skepticalone.mecachecker.data.ShiftProvider;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String SHIFT_ID = "SHIFT_ID";
    private static final String START = "START";
    private static final String END = "END";
    private static final String IS_START = "IS_START";
    private final Calendar mStart = new GregorianCalendar();
    private final Calendar mEnd = new GregorianCalendar();

    public static TimePickerFragment create(long shiftId, long start, long end, boolean isStart) {
        Bundle arguments = new Bundle();
        arguments.putLong(SHIFT_ID, shiftId);
        arguments.putLong(START, start);
        arguments.putLong(END, end);
        arguments.putBoolean(IS_START, isStart);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar time;
        if (getArguments().getBoolean(IS_START)) {
            mStart.setTimeInMillis(getArguments().getLong(START));
            time = mStart;
        } else {
            mEnd.setTimeInMillis(getArguments().getLong(END));
            time = mEnd;
        }
        return new TimePickerDialog(
                getActivity(),
                this,
                time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE),
                false
        );
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        minute -= minute % AppConstants.MINUTES_PER_STEP;
        mStart.setTimeInMillis(getArguments().getLong(START));
        mEnd.setTimeInMillis(getArguments().getLong(END));
        CheckedShift shift = new CheckedShift(mStart, mEnd);
        shift.updateTime(getArguments().getBoolean(IS_START), hourOfDay, minute);
        ContentValues values = new ContentValues();
        values.put(ShiftProvider.START_TIME, shift.getStart().getTime());
        values.put(ShiftProvider.END_TIME, shift.getEnd().getTime());
        getActivity().getContentResolver().update(ShiftProvider.shiftUri(getArguments().getLong(SHIFT_ID)), values, null, null);
    }

}