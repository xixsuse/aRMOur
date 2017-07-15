package com.skepticalone.mecachecker.ui;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.model.AdditionalShift;

public final class AdditionalShiftDetailFragment
        extends DetailFragment<AdditionalShift, AdditionalShiftEntity, AdditionalShiftViewModel> {

    @NonNull
    @Override
    ItemDetailAdapter<AdditionalShift> createAdapter() {
        return null;
    }

    @NonNull
    @Override
    AdditionalShiftViewModel createViewModel() {
        return null;
    }
}
