package com.skepticalone.armour.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.skepticalone.armour.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SuppressWarnings("WeakerAccess")
public final class CurrencyPreference extends DialogPreference {

    private static final int DEFAULT_VALUE = 0;
    private EditText mEditText;
    private int mValue;

    public CurrencyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.currency_edit_text);
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
        return getContext().getString(R.string.payment_format, BigDecimal.valueOf(mValue, 2));
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
            String value = mEditText.getText().toString().trim();
            if (value.isEmpty()) {
                Toast.makeText(getContext(), R.string.value_required, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    mValue = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).unscaledValue().intValue();
                    persistInt(mValue);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), R.string.invalid_format, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
