package com.skepticalone.mecachecker.components;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import com.skepticalone.mecachecker.data.ShiftProvider;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String SHIFT_ID = "SHIFT_ID";
    private static final String START = "START";
    private static final String END = "END";
    private ShiftOverlapListener mListener;
    private Interval oldShift;

    public static DatePickerFragment create(long shiftId, Interval shift) {
        Bundle arguments = new Bundle();
        arguments.putLong(SHIFT_ID, shiftId);
        arguments.putLong(START, shift.getStartMillis());
        arguments.putLong(END, shift.getEndMillis());
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ShiftOverlapListener) context;
        oldShift = new Interval(getArguments().getLong(START), getArguments().getLong(END));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime dateTime = oldShift.getStart();
        return new DatePickerDialog(getActivity(), this,
                dateTime.getYear(),
                dateTime.getMonthOfYear() - 1,
                dateTime.getDayOfMonth()
        );
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DateTime
                start = oldShift.getStart().withDate(year, month + 1, dayOfMonth),
                end = oldShift.getEnd().withDate(year, month + 1, dayOfMonth);
        if (!end.isAfter(start)) {
            end = end.plusDays(1);
        }
        if (getActivity().getContentResolver().update(ShiftProvider.shiftUri(getArguments().getLong(SHIFT_ID)), ShiftProvider.getContentValues(start.getMillis(), end.getMillis()), null, null) == 0) {
            mListener.onShiftOverlap();
        }
    }
}
