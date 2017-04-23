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
    private static final String SHIFT_START = "SHIFT_START";
    private static final String SHIFT_END = "SHIFT_END";
    private static final String LOGGED_SHIFT_START = "LOGGED_SHIFT_START";
    private static final String LOGGED_SHIFT_END = "LOGGED_SHIFT_END";
    private static final String IS_DATE = "IS_DATE";
    private static final String IS_START = "IS_START";
    private static final String IS_ROSTERED = "IS_ROSTERED";
    private static final String IS_LOGGED = "IS_LOGGED";
    private ShiftOverlapListener mListener;
    private Interval oldShift;
    private Interval oldLoggedShift;
    private boolean isDate;
    private boolean isRostered;

    public static PickerFragment createDatePicker(long shiftId, boolean isRostered, Interval shift, @Nullable Interval loggedShift) {
        Bundle arguments = new Bundle();
        arguments.putBoolean(IS_DATE, true);
        arguments.putLong(SHIFT_ID, shiftId);
        arguments.putBoolean(IS_ROSTERED, isRostered);
        arguments.putLong(SHIFT_START, shift.getStartMillis());
        arguments.putLong(SHIFT_END, shift.getEndMillis());
        if (isRostered && loggedShift != null) {
            arguments.putLong(LOGGED_SHIFT_START, loggedShift.getStartMillis());
            arguments.putLong(LOGGED_SHIFT_END, loggedShift.getEndMillis());
        }
        PickerFragment fragment = new PickerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public static PickerFragment createTimePicker(long shiftId, boolean isRostered, Interval rosteredShift, @Nullable Interval loggedShift, boolean isStart, boolean isLogged) {
        Bundle arguments = new Bundle();
        arguments.putBoolean(IS_DATE, false);
        arguments.putLong(SHIFT_ID, shiftId);
        arguments.putBoolean(IS_ROSTERED, isRostered);
        arguments.putLong(SHIFT_START, rosteredShift.getStartMillis());
        arguments.putLong(SHIFT_END, rosteredShift.getEndMillis());
        if (loggedShift != null) {
            arguments.putLong(LOGGED_SHIFT_START, loggedShift.getStartMillis());
            arguments.putLong(LOGGED_SHIFT_END, loggedShift.getEndMillis());
        }
        arguments.putBoolean(IS_START, isStart);
        arguments.putBoolean(IS_LOGGED, isLogged);
        PickerFragment fragment = new PickerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ShiftOverlapListener) context;
        oldShift = new Interval(getArguments().getLong(SHIFT_START), getArguments().getLong(SHIFT_END));
        isDate = getArguments().getBoolean(IS_DATE);
        isRostered = getArguments().getBoolean(IS_ROSTERED);
        oldLoggedShift = (getArguments().containsKey(LOGGED_SHIFT_START) && getArguments().containsKey(LOGGED_SHIFT_END)) ? new Interval(getArguments().getLong(LOGGED_SHIFT_START), getArguments().getLong(LOGGED_SHIFT_END)) : null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime dateTime;
        if (isDate) {
            dateTime = oldShift.getStart();
            return new DatePickerDialog(getActivity(), this,
                    dateTime.getYear(),
                    dateTime.getMonthOfYear() - 1,
                    dateTime.getDayOfMonth()
            );
        } else {
            boolean isLogged = getArguments().getBoolean(IS_LOGGED), isStart = getArguments().getBoolean(IS_START);
            dateTime = isLogged ?
                    (isStart ? oldLoggedShift.getStart() : oldLoggedShift.getEnd()) :
                    (isStart ? oldShift.getStart() : oldShift.getEnd())
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
        DateTime start = oldShift.getStart().withDate(thisDate);
        values.put(isRostered ? ShiftContract.RosteredShifts.COLUMN_NAME_ROSTERED_START : ShiftContract.AdditionalShifts.COLUMN_NAME_START, start.getMillis());
        DateTime end = start.withTime(oldShift.getEnd().toLocalTime());
        if (!end.isAfter(start)) {
            end = end.plusDays(1);
        }
        values.put(isRostered ? ShiftContract.RosteredShifts.COLUMN_NAME_ROSTERED_END : ShiftContract.AdditionalShifts.COLUMN_NAME_END, end.getMillis());
        if (isRostered) updateLoggedValues(values, start);
        save(values);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
        ContentValues values = new ContentValues();
        DateTime start, end;
        boolean isStart = getArguments().getBoolean(IS_START);
        if (isRostered && getArguments().getBoolean(IS_LOGGED)) {
            LocalTime thisTime = new LocalTime(hourOfDay, minutes);
            start = oldShift.getStart().withTime(isStart ? thisTime : oldLoggedShift.getStart().toLocalTime());
            values.put(ShiftContract.RosteredShifts.COLUMN_NAME_LOGGED_START, start.getMillis());
            end = start.withTime(isStart ? oldLoggedShift.getEnd().toLocalTime() : thisTime);
            if (!end.isAfter(start)) {
                end = end.plusDays(1);
            }
            values.put(ShiftContract.RosteredShifts.COLUMN_NAME_LOGGED_END, end.getMillis());
        } else {
            LocalTime thisTime = new LocalTime(hourOfDay, AppConstants.getSteppedMinutes(minutes));
            if (isStart) {
                start = oldShift.getStart().withTime(thisTime);
                values.put(isRostered ? ShiftContract.RosteredShifts.COLUMN_NAME_ROSTERED_START : ShiftContract.AdditionalShifts.COLUMN_NAME_START, start.getMillis());
                end = start.withTime(oldShift.getEnd().toLocalTime());
            } else {
                start = oldShift.getStart();
                end = start.withTime(thisTime);
            }
            if (!end.isAfter(start)) {
                end = end.plusDays(1);
            }
            values.put(isRostered ? ShiftContract.RosteredShifts.COLUMN_NAME_ROSTERED_END : ShiftContract.AdditionalShifts.COLUMN_NAME_END, end.getMillis());
            updateLoggedValues(values, start);
        }
        save(values);
    }

    private void updateLoggedValues(ContentValues values, DateTime startOfRosteredShift) {
        if (oldLoggedShift != null) {
            DateTime start = startOfRosteredShift.withTime(oldLoggedShift.getStart().toLocalTime());
            values.put(ShiftContract.RosteredShifts.COLUMN_NAME_LOGGED_START, start.getMillis());
            DateTime end = start.withTime(oldLoggedShift.getEnd().toLocalTime());
            if (!end.isAfter(start)) {
                end = end.plusDays(1);
            }
            values.put(ShiftContract.RosteredShifts.COLUMN_NAME_LOGGED_END, end.getMillis());
        }
    }

    private void save(ContentValues values) {
        long shiftId = getArguments().getLong(SHIFT_ID);
        if (getActivity().getContentResolver().update(isRostered ? ShiftProvider.rosteredShiftUri(shiftId) : ShiftProvider.additionalShiftUri(shiftId), values, null, null) == 0) {
            mListener.onShiftOverlap();
        }
    }

}