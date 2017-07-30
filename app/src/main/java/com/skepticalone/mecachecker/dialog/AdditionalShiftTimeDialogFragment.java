package com.skepticalone.mecachecker.dialog;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;

import org.joda.time.LocalTime;

public final class AdditionalShiftTimeDialogFragment extends TimeDialogFragment<AdditionalShiftEntity, AdditionalShiftViewModel> {

    public static AdditionalShiftTimeDialogFragment newInstance(boolean start) {
        AdditionalShiftTimeDialogFragment fragment = new AdditionalShiftTimeDialogFragment();
        fragment.setArguments(getArgs(start));
        return fragment;
    }

    @NonNull
    @Override
    ShiftData getShiftDataForDisplay(@NonNull AdditionalShiftEntity shift) {
        return shift.getShiftData();
    }

    @NonNull
    @Override
    AdditionalShiftViewModel onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(AdditionalShiftViewModel.class);
    }

    @Override
    void onTimeSet(@NonNull LocalTime time, boolean start) {
        getViewModel().saveNewTime(getCurrentItem(), time, start);
    }

}
