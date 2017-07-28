package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.CrossCoverDetailAdapter;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.dialog.CrossCoverDateDialogFragment;
import com.skepticalone.mecachecker.dialog.CrossCoverPaymentDialogFragment;

public final class CrossCoverDetailFragment
        extends DetailFragment<CrossCoverEntity, CrossCoverViewModel>
        implements CrossCoverDetailAdapter.Callbacks {

    @NonNull
    @Override
    ItemDetailAdapter<CrossCoverEntity> createAdapter(Context context) {
        return new CrossCoverDetailAdapter(this);
    }

    @NonNull
    @Override
    CrossCoverViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(CrossCoverViewModel.class);
    }

    @Override
    public void changeDate() {
        showDialogFragment(new CrossCoverDateDialogFragment());
    }

    @Override
    public void changePayment() {
        showDialogFragment(new CrossCoverPaymentDialogFragment());
    }

    @Override
    public void setClaimed(boolean claimed) {
        getViewModel().setClaimed(claimed);
    }

    @Override
    public void setPaid(boolean paid) {
        getViewModel().setPaid(paid);
    }

}
