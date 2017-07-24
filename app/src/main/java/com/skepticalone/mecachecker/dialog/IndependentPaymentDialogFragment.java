package com.skepticalone.mecachecker.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;

import com.skepticalone.mecachecker.data.model.PayableItem;

import java.math.BigDecimal;

public abstract class IndependentPaymentDialogFragment<ItemType extends PayableItem> extends IndependentEditTextDialogFragment<ItemType, PayableViewModel> {

    @Override
    final int getHint() {
        return super.getHint();
    }

    @Override
    final int getInputType() {
        return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
    }

    @Override
    final void onCurrentItemChanged(@NonNull ItemType item) {
        setText(item.getPaymentData().getPayment().toPlainString());
    }

    @Override
    final void saveText(@Nullable String paymentString) {
        if (paymentString == null) {
            // TODO: 24/07/17
//            ((SnackCallbacks) getActivity()).showSnackbar(R.string.value_required);
        } else {
            try {
                getViewModel().saveNewPayment(new BigDecimal(paymentString));
            } catch (NumberFormatException e) {
                // TODO: 24/07/17
//                ((SnackCallbacks) getActivity()).showSnackbar(R.string.invalid_format);
            }
        }
    }

}
