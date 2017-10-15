package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemTotalsAdapter;
import com.skepticalone.armour.adapter.SinglePayableTotalsAdapter;
import com.skepticalone.armour.data.model.CrossCover;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class CrossCoverTotalsDialogFragment extends PayableTotalsDialogFragment<CrossCover> {

    public static CrossCoverTotalsDialogFragment newInstance(boolean selected) {
        Bundle args = new Bundle();
        args.putBoolean(SELECTED, selected);
        CrossCoverTotalsDialogFragment fragment = new CrossCoverTotalsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    ItemTotalsAdapter<CrossCover> createAdapter(@NonNull Context context) {
        return new SinglePayableTotalsAdapter<>(context, isSelected() ? R.string.selected_cross_cover_shifts : R.string.all_cross_cover_shifts, this);
    }

    @NonNull
    @Override
    ItemViewModelContract<CrossCover> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

}
