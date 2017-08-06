package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemTotalsAdapter;
import com.skepticalone.mecachecker.adapter.SinglePayableTotalsAdapter;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.viewModel.ItemViewModel;

public final class CrossCoverTotalsDialogFragment extends PayableTotalsDialogFragment<CrossCoverEntity> {

    @NonNull
    @Override
    ItemTotalsAdapter<CrossCoverEntity> createAdapter(@NonNull Context context) {
        return new SinglePayableTotalsAdapter<>(this, R.string.cross_cover_shifts);
    }

    @NonNull
    @Override
    ItemViewModel<CrossCoverEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

}
