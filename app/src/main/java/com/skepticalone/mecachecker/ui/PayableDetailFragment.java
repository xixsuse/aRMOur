package com.skepticalone.mecachecker.ui;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.PayableDetailAdapter;
import com.skepticalone.mecachecker.data.model.Payable;
import com.skepticalone.mecachecker.data.viewModel.PayableViewModelContract;
import com.skepticalone.mecachecker.dialog.PaymentDialogFragment;

abstract class PayableDetailFragment<Entity extends Payable, ViewModel extends PayableViewModelContract<Entity>> extends DetailFragment<Entity, ViewModel>
        implements PayableDetailAdapter.Callbacks {

    @NonNull
    @Override
    abstract PayableDetailAdapter<Entity> createAdapter(Context context);

    @Override
    public final void setClaimed(boolean claimed) {
        getViewModel().setClaimed(getCurrentItem().getId(), claimed);
    }

    @Override
    public final void setPaid(boolean paid) {
        getViewModel().setPaid(getCurrentItem().getId(), paid);
    }

    @NonNull
    abstract PaymentDialogFragment getNewPaymentDialogFragment();

    @Override
    public final void changePayment() {
        showDialogFragment(getNewPaymentDialogFragment());
    }
}
