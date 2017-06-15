package com.skepticalone.mecachecker.components;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.skepticalone.mecachecker.util.AppConstants;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


public class ShiftTimePickerDialogFragment extends PickerDialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String
            CONTENT_URI = "CONTENT_URI",
            IS_START = "IS_START",
            YEAR = "YEAR",
            MONTH = "MONTH",
            DAY_OF_MONTH = "DAY_OF_MONTH",
            START_HOUR_OF_DAY = "START_HOUR_OF_DAY",
            START_MINUTE_OF_HOUR = "START_MINUTE_OF_HOUR",
            END_HOUR_OF_DAY = "END_HOUR_OF_DAY",
            END_MINUTE_OF_HOUR = "END_MINUTE_OF_HOUR",
            COLUMN_NAME_START = "COLUMN_NAME_START",
            COLUMN_NAME_END = "COLUMN_NAME_END";

    public static ShiftTimePickerDialogFragment newInstance(@NonNull Uri contentUri, boolean isStart, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end, @NonNull String columnNameStart, @NonNull String columnNameEnd) {
        Bundle args = new Bundle();
        args.putParcelable(CONTENT_URI, contentUri);
        args.putBoolean(IS_START, isStart);
        args.putInt(YEAR, date.getYear());
        args.putInt(MONTH, date.getMonthOfYear());
        args.putInt(DAY_OF_MONTH, date.getDayOfMonth());
        args.putInt(START_HOUR_OF_DAY, start.getHourOfDay());
        args.putInt(START_MINUTE_OF_HOUR, start.getMinuteOfHour());
        args.putInt(END_HOUR_OF_DAY, end.getHourOfDay());
        args.putInt(END_MINUTE_OF_HOUR, end.getMinuteOfHour());
        args.putString(COLUMN_NAME_START, columnNameStart);
        args.putString(COLUMN_NAME_END, columnNameEnd);
        ShiftTimePickerDialogFragment fragment = new ShiftTimePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        boolean isStart = getArguments().getBoolean(IS_START);
        LocalTime time = new LocalTime(
                getArguments().getInt(isStart ? START_HOUR_OF_DAY : END_HOUR_OF_DAY),
                getArguments().getInt(isStart ? START_MINUTE_OF_HOUR : END_MINUTE_OF_HOUR)
        );
        return new TimePickerDialog(getActivity(), this, time.getHourOfDay(), time.getMinuteOfHour(), DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        LocalDate date = new LocalDate(getArguments().getInt(YEAR), getArguments().getInt(MONTH), getArguments().getInt(DAY_OF_MONTH));
        LocalTime time = new LocalTime(hourOfDay, AppConstants.getSteppedMinutes(minute));
        boolean isStart = getArguments().getBoolean(IS_START);
        DateTime
                start = date.toDateTime(isStart ? time : new LocalTime(getArguments().getInt(START_HOUR_OF_DAY), getArguments().getInt(START_MINUTE_OF_HOUR))),
                end = date.toDateTime(isStart ? new LocalTime(getArguments().getInt(END_HOUR_OF_DAY), getArguments().getInt(END_MINUTE_OF_HOUR)) : time);
        while (!end.isAfter(start)) {
            end = end.plusDays(1);
        }
        ContentValues values = new ContentValues();
        values.put(getArguments().getString(COLUMN_NAME_START), start.getMillis());
        values.put(getArguments().getString(COLUMN_NAME_END), end.getMillis());
        Uri contentUri = getArguments().getParcelable(CONTENT_URI);
        assert contentUri != null;
        if (getActivity().getContentResolver().update(contentUri, values, null, null) != 1) {
            onOverlappingShifts();
        }
    }
}
