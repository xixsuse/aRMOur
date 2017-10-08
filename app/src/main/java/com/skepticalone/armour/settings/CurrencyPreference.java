package com.skepticalone.armour.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.MoneyConverter;

import java.math.BigDecimal;

public final class CurrencyPreference extends IntegerPreference {

    private EditText mEditText;

    public CurrencyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.currency_layout);
        setPositiveButtonText(R.string.set);
        setNegativeButtonText(R.string.cancel);
    }

    @Override
    public CharSequence getSummary() {
        return getContext().getString(R.string.payment_format, MoneyConverter.centsToMoney(getValue()));
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mEditText = view.findViewById(R.id.edit_text);
        mEditText.setText(MoneyConverter.centsToMoney(getValue()).toPlainString());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String value = mEditText.getText().toString().trim();
            if (value.isEmpty()) {
                Toast.makeText(getContext(), R.string.value_required, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    setValue((int) MoneyConverter.moneyToCents(new BigDecimal(value)));
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), R.string.invalid_format, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
