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
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        final EditText editText = (EditText) getActivity().getLayoutInflater().inflate(R.layout.currency_input, null);
        editText.setText(BigDecimal.valueOf(getArguments().getInt(HOURLY_RATE), 2).toPlainString());
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.hourly_rate)
                .setView(editText)
                .setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            BigDecimal rate = (new BigDecimal(editText.getText().toString())).setScale(2, RoundingMode.HALF_UP);
                            ContentValues values = new ContentValues();
                            values.put(Contract.AdditionalShifts.COLUMN_NAME_RATE, rate.unscaledValue().intValue());
                            getActivity().getContentResolver().update(Provider.additionalShiftUri(getArguments().getLong(SHIFT_ID)), values, null, null);
                        } catch (NumberFormatException e) {
                            // do nothing
                        }
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
