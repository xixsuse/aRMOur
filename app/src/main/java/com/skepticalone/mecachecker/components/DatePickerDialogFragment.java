package com.skepticalone.mecachecker.components;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import org.joda.time.LocalDate;


abstract class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String
            CONTENT_URI = "CONTENT_URI",
            YEAR = "YEAR",
            MONTH = "MONTH",
            DAY_OF_MONTH = "DAY_OF_MONTH";

    @NonNull
    static Bundle getArgs(@NonNull Uri contentUri, @NonNull LocalDate date) {
        Bundle args = new Bundle();
        args.putParcelable(CONTENT_URI, contentUri);
        args.putInt(YEAR, date.getYear());
        args.putInt(MONTH, date.getMonthOfYear());
        args.putInt(DAY_OF_MONTH, date.getDayOfMonth());
        return args;
    }

    @NonNull
    abstract ContentValues getValues(@NonNull LocalDate date);

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), this,
                getArguments().getInt(YEAR),
                getArguments().getInt(MONTH) - 1,
                getArguments().getInt(DAY_OF_MONTH)
        );
    }

    @Override
    public final void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Uri contentUri = getArguments().getParcelable(CONTENT_URI);
        assert contentUri != null;
        LocalDate date = new LocalDate(year, month + 1, dayOfMonth);
        getActivity().getContentResolver().update(contentUri, getValues(date), null, null);
    }
}
