package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.viewModel.DateViewModelContract;

import org.joda.time.LocalDate;

public final class CrossCoverDateDialogFragment extends DateDialogFragment<CrossCoverEntity> {

    @NonNull
    @Override
    LocalDate getDateForDisplay(@NonNull CrossCoverEntity crossCover) {
        return crossCover.getDate();
    }

    @NonNull
    @Override
    DateViewModelContract<CrossCoverEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(CrossCoverViewModel.class);
    }

}
