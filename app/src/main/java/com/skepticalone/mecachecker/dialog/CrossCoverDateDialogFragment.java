package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;

import org.joda.time.LocalDate;

public final class CrossCoverDateDialogFragment extends DateDialogFragment<CrossCoverEntity, CrossCoverViewModel> {

    @NonNull
    @Override
    LocalDate getDateForDisplay(@NonNull CrossCoverEntity crossCover) {
        return crossCover.getDate();
    }

    @NonNull
    @Override
    CrossCoverViewModel onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(CrossCoverViewModel.class);
    }

    @Override
    void onDateSet(@NonNull LocalDate date) {
        getViewModel().saveNewDate(date);
    }
}
