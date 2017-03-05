package com.skepticalone.mecachecker.components;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.util.AppConstants;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class PickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String SHIFT_ID = "SHIFT_ID";
    private static final String ROSTERED_SHIFT_START = "ROSTERED_SHIFT_START";
    private static final String ROSTERED_SHIFT_END = "ROSTERED_SHIFT_END";
    private static final String LOGGED_SHIFT_START = "LOGGED_SHIFT_START";
    private static final String LOGGED_SHIFT_END = "LOGGED_SHIFT_END";
    private static final String IS_DATE = "IS_DATE";
    private static final String IS_START = "IS_START";
    private static final String IS_ROSTERED = "IS_ROSTERED";
    private ShiftOverlapListener mListener;
    private Interval oldRosteredShift;
    private Interval oldLoggedShift;

    public static PickerFragment createDatePicker(long shiftId, Interval rosteredShift, @Nullable Interval loggedShift) {
        Bundle arguments = new Bundle();
        arguments.putBoolean(IS_DATE, true);
        arguments.putLong(SHIFT_ID, shiftId);
        arguments.putLong(ROSTERED_SHIFT_START, rosteredShift.getStartMillis());
        arguments.putLong(ROSTERED_SHIFT_END, rosteredShift.getEndMillis());
        if (loggedShift != null) {
            arguments.putLong(LOGGED_SHIFT_START, loggedShift.getStartMillis());
            arguments.putLong(LOGGED_SHIFT_END, loggedShift.getEndMillis());
        }
        PickerFragment fragment = new PickerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public static PickerFragment createTimePicker(long shiftId, Interval rosteredShift, @Nullable Interval loggedShift, boolean isRostered, boolean isStart) {
        Bundle arguments = new Bundle();
        arguments.putBoolean(IS_DATE, false);
        arguments.putLong(SHIFT_ID, shiftId);
        arguments.putLong(ROSTERED_SHIFT_START, rosteredShift.getStartMillis());
        arguments.putLong(ROSTERED_SHIFT_END, rosteredShift.getEndMillis());
        if (loggedShift != null) {
            arguments.putLong(LOGGED_SHIFT_START, loggedShift.getStartMillis());
            arguments.putLong(LOGGED_SHIFT_END, loggedShift.getEndMillis());
        }
        arguments.putBoolean(IS_ROSTERED, isRostered);
        arguments.putBoolean(IS_START, isStart);
        PickerFragment fragment = new PickerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ShiftOverlapListener) context;
        oldRosteredShift = new Interval(getArguments().getLong(ROSTERED_SHIFT_START), getArguments().getLong(ROSTERED_SHIFT_END));
        long loggedShiftStart = getArguments().getLong(LOGGED_SHIFT_START, 0L), loggedShiftEnd = getArguments().getLong(LOGGED_SHIFT_END, 0L);
        oldLoggedShift = (loggedShiftStart == 0L || loggedShiftEnd == 0L) ? null : new Interval(loggedShiftStart, loggedShiftEnd);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime dateTime;
        if (getArguments().getBoolean(IS_DATE)) {
            dateTime = oldRosteredShift.getStart();
            return new DatePickerDialog(getActivity(), this,
                    dateTime.getYear(),
                    dateTime.getMonthOfYear() - 1,
                    dateTime.getDayOfMonth()
            );
        } else {
            boolean isRostered = getArguments().getBoolean(IS_ROSTERED), isStart = getArguments().getBoolean(IS_START);
            dateTime = isRostered ?
                    (isStart ? oldRosteredShift.getStart() : oldRosteredShift.getEnd()) :
                    (isStart ? oldLoggedShift.getStart() : oldLoggedShift.getEnd())
            ;
            return new TimePickerDialog(
                    getActivity(),
                    this,
                    dateTime.getHourOfDay(),
                    dateTime.getMinuteOfHour(),
                    false
            );
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ContentValues values = new ContentValues();
        LocalDate thisDate = new LocalDate(year, month + 1, dayOfMonth);
        DateTime start = oldRosteredShift.getStart().withDate(thisDate);
        values.put(ShiftContract.RosteredShift.COLUMN_NAME_ROSTERED_START, start.getMillis());
        DateTime end = start.withTime(oldRosteredShift.getEnd().toLocalTime());
        if (!end.isAfter(start)) {
            end = end.plusDays(1);
        }
        values.put(ShiftContract.RosteredShift.COLUMN_NAME_ROSTERED_END, end.getMillis());
        updateLoggedValues(values, start);
        save(values);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
        ContentValues values = new ContentValues();
        DateTime start, end;
        boolean isRostered = getArguments().getBoolean(IS_ROSTERED), isStart = getArguments().getBoolean(IS_START);
        if (isRostered) {
            LocalTime thisTime = new LocalTime(hourOfDay, AppConstants.getSteppedMinutes(minutes));
            if (isStart) {
                start = oldRosteredShift.getStart().withTime(thisTime);
                values.put(ShiftContract.RosteredShift.COLUMN_NAME_ROSTERED_START, start.getMillis());
                end = start.withTime(oldRosteredShift.getEnd().toLocalTime());
            } else {
                start = oldRosteredShift.getStart();
                end = start.withTime(thisTime);
            }
            if (!end.isAfter(start)) {
                end = end.plusDays(1);
            }
            values.put(ShiftContract.RosteredShift.COLUMN_NAME_ROSTERED_END, end.getMillis());
            updateLoggedValues(values, start);
        } else {
            LocalTime thisTime = new LocalTime(hourOfDay, minutes);
            start = oldRosteredShift.getStart().withTime(isStart ? thisTime : oldLoggedShift.getStart().toLocalTime());
            values.put(ShiftContract.RosteredShift.COLUMN_NAME_LOGGED_START, start.getMillis());
            end = start.withTime(isStart ? oldLoggedShift.getEnd().toLocalTime() : thisTime);
            if (!end.isAfter(start)) {
                end = end.plusDays(1);
            }
            values.put(ShiftContract.RosteredShift.COLUMN_NAME_LOGGED_END, end.getMillis());
        }
        save(values);
    }

    private void updateLoggedValues(ContentValues values, DateTime startOfRosteredShift) {
        if (oldLoggedShift != null) {
            DateTime start = startOfRosteredShift.withTime(oldLoggedShift.getStart().toLocalTime());
            values.put(ShiftContract.RosteredShift.COLUMN_NAME_LOGGED_START, start.getMillis());
            DateTime end = start.withTime(oldLoggedShift.getEnd().toLocalTime());
            if (!end.isAfter(start)) {
                end = end.plusDays(1);
            }
            values.put(ShiftContract.RosteredShift.COLUMN_NAME_LOGGED_END, end.getMillis());
        }
    }

    private void save(ContentValues values) {
        if (getActivity().getContentResolver().update(ShiftProvider.shiftUri(getArguments().getLong(SHIFT_ID)), values, null, null) == 0) {
            mListener.onShiftOverlap();
        }
    }

}