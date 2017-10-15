package com.skepticalone.armour.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.adapter.AdditionalShiftDetailAdapter;
import com.skepticalone.armour.data.model.AdditionalShift;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.ui.dialog.AdditionalShiftCommentDialogFragment;
import com.skepticalone.armour.ui.dialog.AdditionalShiftDateDialogFragment;
import com.skepticalone.armour.ui.dialog.AdditionalShiftPaymentDialogFragment;
import com.skepticalone.armour.ui.dialog.AdditionalShiftTimeDialogFragment;
import com.skepticalone.armour.ui.dialog.CommentDialogFragment;

public final class AdditionalShiftDetailFragment
        extends DetailFragment<AdditionalShift>
        implements AdditionalShiftDetailAdapter.Callbacks {

    private AdditionalShiftDetailAdapter adapter;
    private AdditionalShiftViewModel viewModel;

    @Override
    protected void onCreateAdapter(@NonNull Context context) {
        adapter = new AdditionalShiftDetailAdapter(context, this);
    }

    @NonNull
    @Override
    protected AdditionalShiftDetailAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(AdditionalShiftViewModel.class);
    }

    @NonNull
    @Override
    protected AdditionalShiftViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void changeDate() {
        showDialogFragment(new AdditionalShiftDateDialogFragment());
    }

    @Override
    public void changeTime(boolean start) {
        showDialogFragment(AdditionalShiftTimeDialogFragment.newInstance(start));
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
        showDialogFragment(new AdditionalShiftPaymentDialogFragment());
    }

    @NonNull
    @Override
    CommentDialogFragment<AdditionalShift> createCommentDialogFragment() {
        return new AdditionalShiftCommentDialogFragment();
    }

}
