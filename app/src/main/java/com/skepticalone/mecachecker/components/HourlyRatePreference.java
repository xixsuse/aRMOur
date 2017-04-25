package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.skepticalone.mecachecker.R;

class HourlyRatePreference extends DialogPreference {

    private final int mDefaultValue;
    private EditText mEditText;
    private int mValue;

    public HourlyRatePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDefaultValue = context.getResources().getInteger(R.integer.default_hourly_rate);
        setDialogLayoutResource(R.layout.hourly_rate_layout);
        setPositiveButtonText(R.string.set);
        setNegativeButtonText(R.string.cancel);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, mDefaultValue);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mValue = getPersistedInt(mDefaultValue);
        } else {
            mValue = (int) defaultValue;
            persistInt(mValue);
        }
    }

    @Override
    public CharSequence getSummary() {
        return getContext().getString(R.string.currency_format, mValue / 100f);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mEditText = (EditText) view;
        mEditText.setText(getContext().getString(R.string.decimal_format, mValue / 100f));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            mValue = Math.round(Float.parseFloat(mEditText.getText().toString()) * 100);
            persistInt(mValue);
        }
    }

}
