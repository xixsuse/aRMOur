package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.CrossCover;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.data.viewModel.DateViewModelContract;

import org.threeten.bp.LocalDate;

public final class CrossCoverDateDialogFragment extends DateDialogFragment<CrossCover> {

    private DateViewModelContract<CrossCover> viewModel;

    @Override
    void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        viewModel = viewModelProvider.get(CrossCoverViewModel.class);
    }

    @NonNull
    @Override
    DateViewModelContract<CrossCover> getViewModel() {
        return viewModel;
    }

    @NonNull
    @Override
    LocalDate getDateForDisplay(@NonNull CrossCover crossCover) {
        return crossCover.getDate();
    }

}
