package com.skepticalone.mecachecker.components;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import org.joda.time.LocalDate;


abstract class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String
            YEAR = "YEAR",
            MONTH = "MONTH",
            DAY_OF_MONTH = "DAY_OF_MONTH";

    @NonNull
    static Bundle getArgs(@Nullable LocalDate localDate) {
        Bundle args = new Bundle();
        if (localDate == null) {
            localDate = LocalDate.now();
        }
        args.putInt(YEAR, localDate.getYear());
        args.putInt(MONTH, localDate.getMonthOfYear());
        args.putInt(DAY_OF_MONTH, localDate.getDayOfMonth());
        return args;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), this,
                getArguments().getInt(YEAR),
                getArguments().getInt(MONTH) - 1,
                getArguments().getInt(DAY_OF_MONTH)
        );
    }

}
