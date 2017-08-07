package com.skepticalone.mecachecker.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.adapter.RosteredShiftDetailAdapter;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.mecachecker.ui.dialog.CommentDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.MessageDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.RosteredShiftDateDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.RosteredShiftTimeDialogFragment;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class RosteredShiftDetailFragment
        extends DetailFragment<RosteredShiftEntity, RosteredShiftViewModel>
        implements RosteredShiftDetailAdapter.Callbacks {

    @NonNull
    @Override
    ItemDetailAdapter<RosteredShiftEntity> createAdapter(Context context) {
        return new RosteredShiftDetailAdapter(this, ShiftUtil.Calculator.getInstance(context));
    }

    @NonNull
    @Override
    RosteredShiftViewModel createViewModel(@NonNull ViewModelProvider provider) {
        return provider.get(RosteredShiftViewModel.class);
    }

    @Override
    public void changeDate() {
        showDialogFragment(new RosteredShiftDateDialogFragment());
    }

    @Override
    public void changeTime(boolean start, boolean logged) {
        showDialogFragment(RosteredShiftTimeDialogFragment.newInstance(start, logged));
    }

    @Override
    public void setLogged(boolean logged) {
        getViewModel().setLogged(getCurrentItem().getId(), logged);
    }

    @NonNull
    @Override
    CommentDialogFragment getNewCommentDialogFragment() {
        return new RosteredShiftCommentDialogFragment();
    }

    @Override
    public void showMessage(@NonNull String message) {
        showDialogFragment(MessageDialogFragment.newInstance(message));
    }

}
