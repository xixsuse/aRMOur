package com.skepticalone.mecachecker.dialog;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class ShiftDatePickerDialogFragment extends DatePickerDialogFragment {

    private static final String
            START_HOUR_OF_DAY = "START_HOUR_OF_DAY",
            START_MINUTE_OF_HOUR = "START_MINUTE_OF_HOUR",
            END_HOUR_OF_DAY = "END_HOUR_OF_DAY",
            END_MINUTE_OF_HOUR = "END_MINUTE_OF_HOUR",
            COLUMN_NAME_START = "COLUMN_NAME_START",
            COLUMN_NAME_END = "COLUMN_NAME_END";

    static Bundle getArgs(@NonNull Uri contentUri, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end, @NonNull String columnNameStart, @NonNull String columnNameEnd) {
        Bundle args = getArgs(contentUri, date);
        args.putInt(START_HOUR_OF_DAY, start.getHourOfDay());
        args.putInt(START_MINUTE_OF_HOUR, start.getMinuteOfHour());
        args.putInt(END_HOUR_OF_DAY, end.getHourOfDay());
        args.putInt(END_MINUTE_OF_HOUR, end.getMinuteOfHour());
        args.putString(COLUMN_NAME_START, columnNameStart);
        args.putString(COLUMN_NAME_END, columnNameEnd);
        return args;
    }

    public static ShiftDatePickerDialogFragment newInstance(@NonNull Uri contentUri, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end, @NonNull String columnNameStart, @NonNull String columnNameEnd) {
        ShiftDatePickerDialogFragment fragment = new ShiftDatePickerDialogFragment();
        fragment.setArguments(getArgs(contentUri, date, start, end, columnNameStart, columnNameEnd));
        return fragment;
    }

    @CallSuper
    @NonNull
    @Override
    ContentValues getValues(@NonNull LocalDate date) {
        LocalTime
                startTime = new LocalTime(getArguments().getInt(START_HOUR_OF_DAY), getArguments().getInt(START_MINUTE_OF_HOUR)),
                endTime = new LocalTime(getArguments().getInt(END_HOUR_OF_DAY), getArguments().getInt(END_MINUTE_OF_HOUR));
        DateTime start = date.toDateTime(startTime), end = date.toDateTime(endTime);
        while (!end.isAfter(start)) {
            end = end.plusDays(1);
        }
        ContentValues values = new ContentValues();
        values.put(getArguments().getString(COLUMN_NAME_START), start.getMillis());
        values.put(getArguments().getString(COLUMN_NAME_END), end.getMillis());
        return values;
    }
}