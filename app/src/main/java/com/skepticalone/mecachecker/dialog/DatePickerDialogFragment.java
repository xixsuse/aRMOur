package com.skepticalone.mecachecker.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

public final class DatePickerDialogFragment extends AbstractDialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String
            YEAR = "YEAR",
            MONTH = "MONTH",
            DAY_OF_MONTH = "DAY_OF_MONTH";
    private Callbacks mCallbacks;

    public static DatePickerDialogFragment newInstance(long itemId, @NonNull LocalDate date) {
        Bundle args = getArgs(itemId);
        args.putInt(YEAR, date.getYear());
        args.putInt(MONTH, date.getMonthOfYear());
        args.putInt(DAY_OF_MONTH, date.getDayOfMonth());
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) getTargetFragment();
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
        mCallbacks.onDateSet(getItemId(), new LocalDate(year, month + 1, dayOfMonth));
    }

    public interface Callbacks {
        void onDateSet(long itemId, @NonNull LocalDate date);
    }
}
