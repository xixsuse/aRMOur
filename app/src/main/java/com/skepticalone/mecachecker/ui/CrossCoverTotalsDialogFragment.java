package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

public final class CrossCoverTotalsDialogFragment extends PayableTotalsDialogFragment<CrossCoverEntity> {
    @NonNull
    @Override
    ViewModelContract<CrossCoverEntity> onCreateViewModel(@NonNull ViewModelProvider provider) {
        return provider.get(CrossCoverViewModel.class);
    }

    @Override
    public int getTitle() {
        return R.string.cross_cover_shifts;
    }
}
