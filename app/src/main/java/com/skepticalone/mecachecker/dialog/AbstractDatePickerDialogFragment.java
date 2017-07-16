package com.skepticalone.mecachecker.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

abstract class AbstractDatePickerDialogFragment extends AbstractDialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String
            YEAR = "YEAR",
            MONTH = "MONTH",
            DAY_OF_MONTH = "DAY_OF_MONTH";

    static Bundle getArgs(long itemId, @NonNull LocalDate date) {
        Bundle args = getArgs(itemId);
        args.putInt(YEAR, date.getYear());
        args.putInt(MONTH, date.getMonthOfYear());
        args.putInt(DAY_OF_MONTH, date.getDayOfMonth());
        return args;
    }

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
        saveDate(new LocalDate(year, month + 1, dayOfMonth));
    }

    abstract void saveDate(@NonNull LocalDate newDate);

}
