package com.skepticalone.mecachecker.dialog;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

public class MidnightDatePickerDialogFragment extends DatePickerDialogFragment {

    private static final String COLUMN_NAME_DATE = "COLUMN_NAME_DATE";

    public static MidnightDatePickerDialogFragment newInstance(@NonNull Uri contentUri, @NonNull LocalDate date, @NonNull String columnName) {
        Bundle args = getArgs(contentUri, date);
        args.putString(COLUMN_NAME_DATE, columnName);
        MidnightDatePickerDialogFragment fragment = new MidnightDatePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    ContentValues getValues(@NonNull LocalDate date) {
        ContentValues values = new ContentValues();
        values.put(getArguments().getString(COLUMN_NAME_DATE), date.toDateTimeAtStartOfDay().getMillis());
        return values;
    }

}
