package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RawRosteredShiftEntity;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;

public final class RosteredShiftCommentDialogFragment extends CommentDialogFragment<RawRosteredShiftEntity> {

    @NonNull
    @Override
    ItemViewModelContract<RawRosteredShiftEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(RosteredShiftViewModel.class);
    }

}
