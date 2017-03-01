package com.skepticalone.mecachecker.components;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.util.AppConstants;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String SHIFT_ID = "SHIFT_ID";
    private static final String START = "START";
    private static final String END = "END";
    private static final String IS_START = "IS_START";
    private ShiftOverlapListener mListener;
    private Interval oldShift;
    private boolean mIsStart;

    public static TimePickerFragment create(long shiftId, Interval shift, boolean isStart) {
        Bundle arguments = new Bundle();
        arguments.putLong(SHIFT_ID, shiftId);
        arguments.putLong(START, shift.getStartMillis());
        arguments.putLong(END, shift.getEndMillis());
        arguments.putBoolean(IS_START, isStart);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ShiftOverlapListener) context;
        oldShift = new Interval(getArguments().getLong(START), getArguments().getLong(END));
        mIsStart = getArguments().getBoolean(IS_START);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime dateTime = mIsStart ? oldShift.getStart() : oldShift.getEnd();
        return new TimePickerDialog(
                getActivity(),
                this,
                dateTime.getHourOfDay(),
                dateTime.getMinuteOfHour(),
                false
        );
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
        minutes = AppConstants.getSteppedMinutes(minutes);
        DateTime start, end;
        if (mIsStart) {
            start = oldShift.getStart().withTime(hourOfDay, minutes, 0, 0);
            end = start.withTime(oldShift.getEnd().toLocalTime());
        } else {
            start = oldShift.getStart();
            end = start.withTime(hourOfDay, minutes, 0, 0);
        }
        if (!end.isAfter(start)) {
            end = end.plusDays(1);
        }
        if (getActivity().getContentResolver().update(ShiftProvider.shiftUri(getArguments().getLong(SHIFT_ID)), ShiftProvider.getContentValues(start.getMillis(), end.getMillis()), null, null) == 0) {
            mListener.onShiftOverlap();
        }
    }

}