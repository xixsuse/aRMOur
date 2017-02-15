package com.skepticalone.mecachecker.shift;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import com.skepticalone.mecachecker.CheckedShift;
import com.skepticalone.mecachecker.data.ShiftProvider;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String SHIFT_ID = "SHIFT_ID";
    private static final String START = "START";
    private static final String END = "END";
    private final Calendar mStart = new GregorianCalendar();
    private final Calendar mEnd = new GregorianCalendar();

    public static DatePickerFragment create(long shiftId, long start, long end) {
        Bundle arguments = new Bundle();
        arguments.putLong(SHIFT_ID, shiftId);
        arguments.putLong(START, start);
        arguments.putLong(END, end);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mStart.setTimeInMillis(getArguments().getLong(START));
        return new DatePickerDialog(getActivity(), this,
                mStart.get(Calendar.YEAR),
                mStart.get(Calendar.MONTH),
                mStart.get(Calendar.DAY_OF_MONTH)
        );
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mStart.setTimeInMillis(getArguments().getLong(START));
        mEnd.setTimeInMillis(getArguments().getLong(END));
        CheckedShift shift = new CheckedShift(mStart, mEnd);
        shift.updateDate(year, month, dayOfMonth);
        ContentValues values = new ContentValues();
        values.put(ShiftProvider.START_TIME, shift.getStart().getTime());
        values.put(ShiftProvider.END_TIME, shift.getEnd().getTime());
        getActivity().getContentResolver().update(ShiftProvider.shiftUri(getArguments().getLong(SHIFT_ID)), values, null, null);
    }
}
