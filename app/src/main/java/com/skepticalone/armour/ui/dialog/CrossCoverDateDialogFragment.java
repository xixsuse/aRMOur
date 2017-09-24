package com.skepticalone.armour.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.model.RawCrossCoverEntity;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.data.viewModel.DateViewModelContract;

import org.threeten.bp.LocalDate;

public final class CrossCoverDateDialogFragment extends DateDialogFragment<RawCrossCoverEntity> {

    @NonNull
    @Override
    LocalDate getDateForDisplay(@NonNull RawCrossCoverEntity crossCover) {
        return crossCover.getDate();
    }

    @NonNull
    @Override
    DateViewModelContract<RawCrossCoverEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

}
