package com.skepticalone.mecachecker.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.ui.SnackCallbacks;

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
    final void save(@Nullable String trimmedTextWithLength) {
        if (trimmedTextWithLength == null) {
            ((SnackCallbacks) getActivity()).showSnackbar(R.string.value_required);
        } else {
            try {
                mCallbacks.savePayment(getItemId(), new BigDecimal(trimmedTextWithLength));
            } catch (NumberFormatException e) {
                ((SnackCallbacks) getActivity()).showSnackbar(R.string.invalid_format);
            }
        }
    }

    public interface Callbacks {
        void savePayment(long id, @NonNull BigDecimal payment);
    }

}
