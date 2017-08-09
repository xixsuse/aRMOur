package com.skepticalone.armour.ui.detail;

import android.support.annotation.NonNull;

import com.skepticalone.armour.adapter.PayableDetailAdapter;
import com.skepticalone.armour.data.model.Payable;
import com.skepticalone.armour.data.viewModel.PayableViewModelContract;
import com.skepticalone.armour.ui.dialog.PaymentDialogFragment;

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
