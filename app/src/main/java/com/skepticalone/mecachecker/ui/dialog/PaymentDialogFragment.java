package com.skepticalone.mecachecker.ui.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Payable;
import com.skepticalone.mecachecker.data.viewModel.PayableItemViewModelContract;

import java.math.BigDecimal;

abstract class PaymentDialogFragment<Entity extends Payable> extends TextDialogFragment<Entity> {

    @Override
    final int getEditText() {
        return R.layout.currency_edit_text;
    }

    @NonNull
    @Override
    abstract PayableItemViewModelContract<Entity> getViewModel();

    @Nullable
    @Override
    final String getTextForDisplay(@NonNull Entity item) {
        return item.getPaymentData().getPayment().toPlainString();
    }

    @Override
    final void saveText(@Nullable String paymentString) {
        if (paymentString == null) {
            showSnackbar(R.string.value_required);
        } else {
            try {
                getViewModel().saveNewPayment(new BigDecimal(paymentString));
            } catch (NumberFormatException e) {
                showSnackbar(R.string.invalid_format);
            }
        }
    }

}
