package com.skepticalone.armour.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.skepticalone.armour.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class CurrencyPreference extends IntegerPreference {

    private EditText mEditText;

    public CurrencyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.currency_layout);
        setPositiveButtonText(R.string.set);
        setNegativeButtonText(R.string.cancel);
    }

    @NonNull
    private static BigDecimal getMoney(int cents) {
        return BigDecimal.valueOf(cents, 2);
    }

    @Override
    public CharSequence getSummary() {
        return getContext().getString(R.string.payment_format, getMoney(getValue()));
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mEditText = view.findViewById(R.id.edit_text);
        mEditText.setText(getMoney(getValue()).toPlainString());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String value = mEditText.getText().toString().trim();
            if (value.isEmpty()) {
                Toast.makeText(getContext(), R.string.value_required, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    setValue(new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).unscaledValue().intValue());
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), R.string.invalid_format, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
