package com.skepticalone.armour.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
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

    private final AdditionalShiftDetailAdapter adapter = new AdditionalShiftDetailAdapter(this);

    @NonNull
    @Override
    protected AdditionalShiftDetailAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    protected AdditionalShiftViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
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
