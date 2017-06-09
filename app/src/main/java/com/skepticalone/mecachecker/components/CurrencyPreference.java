package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.skepticalone.mecachecker.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

class CurrencyPreference extends DialogPreference {

    private static final int DEFAULT_VALUE = 0;
    private EditText mEditText;
    private int mValue;

    CurrencyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.input_money);
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
        return getContext().getString(R.string.currency_format, BigDecimal.valueOf(mValue, 2));
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mEditText = (EditText) view;
        mEditText.setText(BigDecimal.valueOf(mValue, 2).toPlainString());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            try {
                mValue = new BigDecimal(mEditText.getText().toString()).setScale(2, RoundingMode.HALF_UP).unscaledValue().intValue();
                persistInt(mValue);
            } catch (NumberFormatException e) {
                // do nothing
            }
        }
    }

}
