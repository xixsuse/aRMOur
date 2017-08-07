package com.skepticalone.mecachecker.ui.detail;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.PayableDetailAdapter;
import com.skepticalone.mecachecker.data.model.Payable;
import com.skepticalone.mecachecker.data.viewModel.PayableViewModelContract;
import com.skepticalone.mecachecker.ui.dialog.PaymentDialogFragment;

abstract class PayableDetailFragment<Entity extends Payable> extends DetailFragment<Entity>
        implements PayableDetailAdapter.Callbacks {

    @Override
    public final void setClaimed(boolean claimed) {
        getViewModel().setClaimed(claimed);
    }

    @Override
    public final void setPaid(boolean paid) {
        getViewModel().setPaid(paid);
    }

    @NonNull
    @Override
    protected abstract PayableViewModelContract<Entity> getViewModel();

    @NonNull
    abstract PaymentDialogFragment<Entity> createPaymentDialogFragment();

    @Override
    public final void changePayment() {
        showDialogFragment(createPaymentDialogFragment());
    }
}
