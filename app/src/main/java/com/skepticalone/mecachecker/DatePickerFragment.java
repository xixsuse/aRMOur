package com.skepticalone.mecachecker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {

    private static final String YEAR = "YEAR";
    private static final String MONTH = "MONTH";
    private static final String DAY_OF_MONTH = "DAY_OF_MONTH";
    private DatePickerDialog.OnDateSetListener mListener;

    public static DatePickerFragment create(Shift shift) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(DatePickerFragment.YEAR, shift.getYear());
        arguments.putInt(DatePickerFragment.MONTH, shift.getMonth());
        arguments.putInt(DatePickerFragment.DAY_OF_MONTH, shift.getDayOfMonth());
        fragment.setArguments(arguments);
        return fragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (DatePickerDialog.OnDateSetListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), mListener,
                getArguments().getInt(YEAR),
                getArguments().getInt(MONTH),
                getArguments().getInt(DAY_OF_MONTH)
        );
    }
}
