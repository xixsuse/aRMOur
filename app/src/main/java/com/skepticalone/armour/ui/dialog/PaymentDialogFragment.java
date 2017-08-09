package com.skepticalone.armour.ui.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Payable;
import com.skepticalone.armour.data.viewModel.PayableViewModelContract;

import java.math.BigDecimal;

public abstract class PaymentDialogFragment<Entity extends Payable> extends TextDialogFragment<Entity> {

    @Override
    final int getEditText() {
        return R.layout.currency_edit_text;
    }

    @NonNull
    @Override
    abstract PayableViewModelContract<Entity> getViewModel();

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
