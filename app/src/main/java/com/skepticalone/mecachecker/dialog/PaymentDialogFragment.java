package com.skepticalone.mecachecker.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputType;
import android.widget.Toast;

import com.skepticalone.mecachecker.R;

import java.math.BigDecimal;

public final class PaymentDialogFragment extends EditTextDialogFragment {

    private Callbacks mCallbacks;

    public static PaymentDialogFragment newInstance(long id, @NonNull BigDecimal payment, @StringRes int title) {
        PaymentDialogFragment fragment = new PaymentDialogFragment();
        Bundle args = getArgs(id, title, payment.toPlainString(), R.string.currency_input_hint);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) getTargetFragment();
    }

    @Override
    final int getInputType() {
        return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
    }

    @Override
    final void save(@Nullable String trimmedTextWithLength) {
        try {
            mCallbacks.savePayment(getItemId(), new BigDecimal(trimmedTextWithLength));
        } catch (NullPointerException | NumberFormatException e) {
            Toast.makeText(getActivity(), R.string.invalid_format, Toast.LENGTH_SHORT).show();
        }
    }

    public interface Callbacks {
        void savePayment(long id, @NonNull BigDecimal payment);
    }

}
