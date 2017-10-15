package com.skepticalone.armour.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
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

    private CrossCoverDetailAdapter adapter;
    private CrossCoverViewModel viewModel;

    @Override
    protected void onCreateAdapter(@NonNull Context context) {
        adapter = new CrossCoverDetailAdapter(context, this);
    }

    @NonNull
    @Override
    protected CrossCoverDetailAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(CrossCoverViewModel.class);
    }

    @NonNull
    @Override
    protected CrossCoverViewModel getViewModel() {
        return viewModel;
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
