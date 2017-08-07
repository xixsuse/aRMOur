package com.skepticalone.mecachecker.ui.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.viewModel.DateItemViewModelContract;

import org.joda.time.LocalDate;

public final class CrossCoverDateDialogFragment extends DateDialogFragment<CrossCoverEntity> {

    @NonNull
    @Override
    LocalDate getDateForDisplay(@NonNull CrossCoverEntity crossCover) {
        return crossCover.getDate();
    }

    @NonNull
    @Override
    DateItemViewModelContract<CrossCoverEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

}
