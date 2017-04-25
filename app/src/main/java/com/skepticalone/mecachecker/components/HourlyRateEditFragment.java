package com.skepticalone.mecachecker.components;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;

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
        @SuppressLint("InflateParams")
        final EditText editText = (EditText) getActivity().getLayoutInflater().inflate(R.layout.hourly_rate_layout, null);
        editText.setText(String.format(Locale.US, "%.2f", getArguments().getInt(HOURLY_RATE) / 100f));
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.hourly_rate)
                .setView(editText)
                .setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int rate = Math.round(Float.parseFloat(editText.getText().toString()) * 100);
                        ContentValues values = new ContentValues();
                        values.put(ShiftContract.AdditionalShifts.COLUMN_NAME_RATE, rate);
                        getActivity().getContentResolver().update(ShiftProvider.additionalShiftUri(getArguments().getLong(SHIFT_ID)), values, null, null);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HourlyRateEditFragment.this.getDialog().cancel();
                    }
                })
                .create();
    }

}
