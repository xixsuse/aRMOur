package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RawCrossCoverEntity;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class CrossCoverCommentDialogFragment extends CommentDialogFragment<RawCrossCoverEntity> {

    @NonNull
    @Override
    ItemViewModelContract<RawCrossCoverEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

}
