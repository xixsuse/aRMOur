package com.skepticalone.mecachecker.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.RosteredShiftDetailAdapter;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.mecachecker.ui.dialog.CommentDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.RosteredShiftCommentDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.RosteredShiftDateDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.RosteredShiftTimeDialogFragment;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class RosteredShiftDetailFragment
        extends DetailFragment<RosteredShiftEntity>
        implements RosteredShiftDetailAdapter.Callbacks {

    private RosteredShiftDetailAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new RosteredShiftDetailAdapter(this, ShiftUtil.Calculator.getInstance(context));
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
    CommentDialogFragment<RosteredShiftEntity> createCommentDialogFragment() {
        return new RosteredShiftCommentDialogFragment();
    }

    @Override
    public void setLogged(boolean logged) {
        getViewModel().setLogged(logged);
    }

    @Override
    public void showMessage(@NonNull String message) {

    }
}
