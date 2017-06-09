package com.skepticalone.mecachecker.components.shifts;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

public class MidnightDatePickerDialogFragment extends DatePickerDialogFragment {

    private static final String CONTENT_URI = "CONTENT_URI";
    private static final String COLUMN_NAME = "COLUMN_NAME";

    public static MidnightDatePickerDialogFragment newInstance(@Nullable LocalDate date, @NonNull Uri contentUri, @NonNull String columnName) {
        Bundle args = getArgs(date);
        args.putParcelable(CONTENT_URI, contentUri);
        args.putString(COLUMN_NAME, columnName);
        MidnightDatePickerDialogFragment fragment = new MidnightDatePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ContentValues values = new ContentValues();
        values.put(
                getArguments().getString(COLUMN_NAME),
                new LocalDate(year, month, dayOfMonth).toDateTimeAtStartOfDay().getMillis()
        );
        Uri contentUri = getArguments().getParcelable(CONTENT_URI);
        assert contentUri != null;
        getActivity().getContentResolver().update(contentUri, values, null, null);
    }
}
