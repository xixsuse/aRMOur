package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.entity.CrossCoverEntity;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.data.viewModel.DateViewModelContract;

import org.threeten.bp.LocalDate;

public final class CrossCoverDateDialogFragment extends DateDialogFragment<CrossCoverEntity> {

    @NonNull
    @Override
    LocalDate getDateForDisplay(@NonNull CrossCoverEntity crossCover) {
        return crossCover.getDate();
    }

    @NonNull
    @Override
    DateViewModelContract<CrossCoverEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

}
