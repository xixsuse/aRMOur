package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemTotalsAdapter;
import com.skepticalone.armour.adapter.SinglePayableTotalsAdapter;
import com.skepticalone.armour.data.model.RawCrossCoverEntity;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class CrossCoverTotalsDialogFragment extends PayableTotalsDialogFragment<RawCrossCoverEntity> {

    @NonNull
    @Override
    ItemTotalsAdapter<RawCrossCoverEntity> createAdapter() {
        return new SinglePayableTotalsAdapter<>(this, R.string.cross_cover_shifts);
    }

    @NonNull
    @Override
    ItemViewModelContract<RawCrossCoverEntity> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

}
