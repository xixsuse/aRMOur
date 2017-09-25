package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.CrossCover;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.data.viewModel.DateViewModelContract;

import org.threeten.bp.LocalDate;

public final class CrossCoverDateDialogFragment extends DateDialogFragment<CrossCover> {

    @NonNull
    @Override
    LocalDate getDateForDisplay(@NonNull CrossCover crossCover) {
        return crossCover.getDate();
    }

    @NonNull
    @Override
    DateViewModelContract<CrossCover> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

}
