package com.skepticalone.mecachecker.components;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.util.AppConstants;
import com.skepticalone.mecachecker.util.ShiftCategory;

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
    private static final String IS_ADDITIONAL = "IS_ADDITIONAL";
    private static final String IS_CROSS_COVER = "IS_CROSS_COVER";
    private static final String IS_LOGGED = "IS_LOGGED";
    private ShiftOverlapListener mListener;
    private Interval oldShift;
    private Interval oldLoggedShift;
    private boolean isDate;
    private ShiftCategory mShiftCategory;

    public static PickerFragment createCrossCoverDatePicker(long shiftId, DateTime date) {
        Bundle arguments = new Bundle();
        arguments.putBoolean(IS_DATE, true);
        arguments.putLong(SHIFT_ID, shiftId);
        arguments.putBoolean(IS_CROSS_COVER, true);
        arguments.putLong(SHIFT_START, date.getMillis());
        PickerFragment fragment = new PickerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public static PickerFragment createIntervalDatePicker(long shiftId, boolean isRostered, Interval shift, @Nullable Interval loggedShift) {
        Bundle arguments = new Bundle();
        arguments.putBoolean(IS_DATE, true);
        arguments.putLong(SHIFT_ID, shiftId);
        arguments.putBoolean(isRostered ? IS_ROSTERED : IS_ADDITIONAL, true);
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
        isDate = getArguments().getBoolean(IS_DATE);
        mShiftCategory = getArguments().getBoolean(IS_ROSTERED, false) ? ShiftCategory.ROSTERED : getArguments().getBoolean(IS_ADDITIONAL, false) ? ShiftCategory.ADDITIONAL : getArguments().getBoolean(IS_CROSS_COVER, false) ? ShiftCategory.CROSS_COVER : null;
        if (mShiftCategory != ShiftCategory.CROSS_COVER) {
            oldShift = new Interval(getArguments().getLong(SHIFT_START), getArguments().getLong(SHIFT_END));
            oldLoggedShift = (getArguments().containsKey(LOGGED_SHIFT_START) && getArguments().containsKey(LOGGED_SHIFT_END)) ? new Interval(getArguments().getLong(LOGGED_SHIFT_START), getArguments().getLong(LOGGED_SHIFT_END)) : null;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime dateTime;
        if (isDate) {
            dateTime = mShiftCategory == ShiftCategory.CROSS_COVER ? new DateTime(getArguments().getLong(SHIFT_START)) : oldShift.getStart();
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
        if (mShiftCategory == ShiftCategory.CROSS_COVER) {
            values.put(ShiftContract.CrossCoverShifts.COLUMN_NAME_DATE, thisDate.toDateTimeAtStartOfDay().getMillis());
        } else {
            boolean isRostered = mShiftCategory == ShiftCategory.ROSTERED;
            DateTime start = oldShift.getStart().withDate(thisDate);
            values.put(isRostered ? ShiftContract.RosteredShifts.COLUMN_NAME_ROSTERED_START : ShiftContract.AdditionalShifts.COLUMN_NAME_START, start.getMillis());
            DateTime end = start.withTime(oldShift.getEnd().toLocalTime());
            if (!end.isAfter(start)) {
                end = end.plusDays(1);
            }
            values.put(isRostered ? ShiftContract.RosteredShifts.COLUMN_NAME_ROSTERED_END : ShiftContract.AdditionalShifts.COLUMN_NAME_END, end.getMillis());
            if (isRostered) updateLoggedValues(values, start);
        }
        save(values);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
        boolean isRostered = mShiftCategory == ShiftCategory.ROSTERED;
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
        Uri uri;
        switch (mShiftCategory) {
            case ROSTERED:
                uri = ShiftProvider.rosteredShiftUri(shiftId);
                break;
            case ADDITIONAL:
                uri = ShiftProvider.additionalShiftUri(shiftId);
                break;
            case CROSS_COVER:
                uri = ShiftProvider.crossCoverShiftUri(shiftId);
                break;
            default:
                throw new IllegalStateException();
        }
        if (getActivity().getContentResolver().update(uri, values, null, null) == 0) {
            mListener.onShiftOverlap();
        }
    }

}