package com.skepticalone.mecachecker.dialog;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class RosteredShiftDatePickerDialogFragment extends ShiftDatePickerDialogFragment {
    private static final String
            HAS_LOG = "HAS_LOG",
            LOGGED_START_HOUR_OF_DAY = "LOGGED_START_HOUR_OF_DAY",
            LOGGED_START_MINUTE_OF_HOUR = "LOGGED_START_MINUTE_OF_HOUR",
            LOGGED_END_HOUR_OF_DAY = "LOGGED_END_HOUR_OF_DAY",
            LOGGED_END_MINUTE_OF_HOUR = "LOGGED_END_MINUTE_OF_HOUR",
            LOGGED_COLUMN_NAME_START = "LOGGED_COLUMN_NAME_START",
            LOGGED_COLUMN_NAME_END = "LOGGED_COLUMN_NAME_END";

    public static RosteredShiftDatePickerDialogFragment newInstance(
            @NonNull Uri contentUri,
            @NonNull LocalDate date,
            @NonNull LocalTime start,
            @NonNull LocalTime end,
            @Nullable LocalTime loggedStart,
            @Nullable LocalTime loggedEnd,
            @NonNull String columnNameStart,
            @NonNull String columnNameEnd,
            @NonNull String loggedColumnNameStart,
            @NonNull String loggedColumnNameEnd
    ) {
        Bundle args = getArgs(contentUri, date, start, end, columnNameStart, columnNameEnd);
        if (loggedStart == null || loggedEnd == null) {
            args.putBoolean(HAS_LOG, false);
        } else {
            args.putBoolean(HAS_LOG, true);
            args.putInt(LOGGED_START_HOUR_OF_DAY, loggedStart.getHourOfDay());
            args.putInt(LOGGED_START_MINUTE_OF_HOUR, loggedStart.getMinuteOfHour());
            args.putInt(LOGGED_END_HOUR_OF_DAY, loggedEnd.getHourOfDay());
            args.putInt(LOGGED_END_MINUTE_OF_HOUR, loggedEnd.getMinuteOfHour());
            args.putString(LOGGED_COLUMN_NAME_START, loggedColumnNameStart);
            args.putString(LOGGED_COLUMN_NAME_END, loggedColumnNameEnd);
        }
        RosteredShiftDatePickerDialogFragment fragment = new RosteredShiftDatePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    ContentValues getValues(@NonNull LocalDate date) {
        ContentValues values = super.getValues(date);
        if (getArguments().getBoolean(HAS_LOG)) {
            LocalTime
                    loggedStartTime = new LocalTime(getArguments().getInt(LOGGED_START_HOUR_OF_DAY), getArguments().getInt(LOGGED_START_MINUTE_OF_HOUR)),
                    loggedEndTime = new LocalTime(getArguments().getInt(LOGGED_END_HOUR_OF_DAY), getArguments().getInt(LOGGED_END_MINUTE_OF_HOUR));
            DateTime loggedStart = date.toDateTime(loggedStartTime), loggedEnd = date.toDateTime(loggedEndTime);
            while (!loggedEnd.isAfter(loggedStart)) {
                loggedEnd = loggedEnd.plusDays(1);
            }
            values.put(getArguments().getString(LOGGED_COLUMN_NAME_START), loggedStart.getMillis());
            values.put(getArguments().getString(LOGGED_COLUMN_NAME_END), loggedEnd.getMillis());
        }
        return values;
    }
}
