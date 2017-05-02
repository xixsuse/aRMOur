package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.skepticalone.mecachecker.R;

class CurrencyPreference extends DialogPreference {

    private static final int DEFAULT_VALUE = 0;
    private EditText mEditText;
    private int mValue;

    CurrencyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.currency_input);
        setPositiveButtonText(R.string.set);
        setNegativeButtonText(R.string.cancel);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mValue = getPersistedInt(DEFAULT_VALUE);
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
