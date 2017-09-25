package com.skepticalone.armour.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.adapter.CrossCoverDetailAdapter;
import com.skepticalone.armour.data.model.CrossCover;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.ui.dialog.CommentDialogFragment;
import com.skepticalone.armour.ui.dialog.CrossCoverCommentDialogFragment;
import com.skepticalone.armour.ui.dialog.CrossCoverDateDialogFragment;
import com.skepticalone.armour.ui.dialog.CrossCoverPaymentDialogFragment;

public final class CrossCoverDetailFragment
        extends DetailFragment<CrossCover>
        implements CrossCoverDetailAdapter.Callbacks {

    private final CrossCoverDetailAdapter adapter = new CrossCoverDetailAdapter(this);

    @NonNull
    @Override
    protected CrossCoverDetailAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    protected CrossCoverViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

    @Override
    public void changeDate() {
        showDialogFragment(new CrossCoverDateDialogFragment());
    }

    @Override
    public final void setClaimed(boolean claimed) {
        getViewModel().setClaimed(claimed);
    }

    @Override
    public final void setPaid(boolean paid) {
        getViewModel().setPaid(paid);
    }

    @Override
    public void changePayment() {
        showDialogFragment(new CrossCoverPaymentDialogFragment());
    }

    @NonNull
    @Override
    CommentDialogFragment<CrossCover> createCommentDialogFragment() {
        return new CrossCoverCommentDialogFragment();
    }

}
