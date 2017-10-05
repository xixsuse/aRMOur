package com.skepticalone.armour.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.adapter.RosteredShiftDetailAdapter;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.armour.ui.dialog.CommentDialogFragment;
import com.skepticalone.armour.ui.dialog.MessageDialogFragment;
import com.skepticalone.armour.ui.dialog.RosteredShiftCommentDialogFragment;
import com.skepticalone.armour.ui.dialog.RosteredShiftDateDialogFragment;
import com.skepticalone.armour.ui.dialog.RosteredShiftTimeDialogFragment;

public final class RosteredShiftDetailFragment
        extends DetailFragment<RosteredShift>
        implements RosteredShiftDetailAdapter.Callbacks {

    private RosteredShiftDetailAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new RosteredShiftDetailAdapter(context, this);
    }

    @NonNull
    @Override
    protected RosteredShiftDetailAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    protected RosteredShiftViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(RosteredShiftViewModel.class);
    }

    @Override
    public void changeDate() {
        showDialogFragment(new RosteredShiftDateDialogFragment());
    }

    @Override
    public void changeTime(boolean start, boolean logged) {
        showDialogFragment(RosteredShiftTimeDialogFragment.newInstance(start, logged));
    }

    @NonNull
    @Override
    CommentDialogFragment<RosteredShift> createCommentDialogFragment() {
        return new RosteredShiftCommentDialogFragment();
    }

    @Override
    public void setLogged(boolean logged) {
        getViewModel().setLogged(logged);
    }

    @Override
    public void showMessage(@NonNull String message) {
        showDialogFragment(MessageDialogFragment.newInstance(message));
    }
}
