package com.skepticalone.mecachecker.ui.components;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputType;
import android.widget.Toast;

import com.skepticalone.mecachecker.R;

import java.math.BigDecimal;

public class MoneyDialogFragment extends EditTextDialogFragment {

    private Callbacks mCallbacks;

    public static MoneyDialogFragment newInstance(long id, @NonNull BigDecimal money, @StringRes int title) {
        MoneyDialogFragment fragment = new MoneyDialogFragment();
        Bundle args = getArgs(id, title, money.toPlainString(), R.string.currency_input_hint);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) getTargetFragment();
    }

    @Override
    int getInputType() {
        return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
    }

    @Override
    void save(@Nullable String trimmedTextWithLength) {
        try {
            mCallbacks.saveMoney(getItemId(), new BigDecimal(trimmedTextWithLength));
        } catch (NullPointerException | NumberFormatException e) {
            Toast.makeText(getActivity(), R.string.invalid_format, Toast.LENGTH_SHORT).show();
        }
    }

    public interface Callbacks {
        void saveMoney(long itemId, @NonNull BigDecimal money);
    }

}
