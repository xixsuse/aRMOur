package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RawAdditionalShiftEntity;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class AdditionalShiftCommentDialogFragment extends CommentDialogFragment<RawAdditionalShiftEntity> {

    @NonNull
    @Override
    ItemViewModelContract<RawAdditionalShiftEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(AdditionalShiftViewModel.class);
    }

}
