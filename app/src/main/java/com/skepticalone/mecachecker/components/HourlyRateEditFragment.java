package com.skepticalone.mecachecker.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.Locale;

public class HourlyRateEditFragment extends DialogFragment {

    private static final String SHIFT_ID = "SHIFT_ID";
    private static final String HOURLY_RATE = "HOURLY_RATE";

    public static HourlyRateEditFragment create(long shiftId, int hourlyRate) {
        HourlyRateEditFragment fragment = new HourlyRateEditFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(SHIFT_ID, shiftId);
        arguments.putInt(HOURLY_RATE, hourlyRate);
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        EditText rateView = new EditText(getActivity());
        rateView.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        rateView.setText(String.format(Locale.US, "%.2f", getArguments().getInt(HOURLY_RATE) / 100f));
        return new AlertDialog.Builder(getActivity())
                .setView(rateView)
                .setCancelable(true)
                .create();
    }
}
