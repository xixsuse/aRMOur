package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;

import org.joda.time.LocalDate;

public final class AdditionalShiftDateDialogFragment extends DateDialogFragment<AdditionalShiftEntity, AdditionalShiftViewModel> {

    @NonNull
    @Override
    LocalDate getDateForDisplay(@NonNull AdditionalShiftEntity additionalShift) {
        return additionalShift.getShiftData().getStart().toLocalDate();
    }

    @NonNull
    @Override
    AdditionalShiftViewModel onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(AdditionalShiftViewModel.class);
    }

    @Override
    void onDateSet(@NonNull LocalDate date) {
        getViewModel().saveNewDate(date);
    }
}
