package com.skepticalone.mecachecker.components;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import com.skepticalone.mecachecker.data.ShiftProvider;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String SHIFT_ID = "SHIFT_ID";
    private static final String START = "START";
    private static final String END = "END";
    private final Calendar calendar = Calendar.getInstance();

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
        calendar.setTimeInMillis(getArguments().getLong(START));
        return new DatePickerDialog(getActivity(), this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.setTimeInMillis(getArguments().getLong(START));
        calendar.set(year, month, dayOfMonth);
        long start = calendar.getTimeInMillis();
        calendar.setTimeInMillis(getArguments().getLong(END));
        calendar.set(year, month, dayOfMonth);
        if (calendar.getTimeInMillis() <= start) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        getActivity().getContentResolver().update(ShiftProvider.shiftUri(getArguments().getLong(SHIFT_ID)), ShiftProvider.getContentValues(start, calendar.getTimeInMillis()), null, null);
    }
}
