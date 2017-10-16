package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.FilteredItemTotalsAdapter;
import com.skepticalone.armour.adapter.SinglePayableTotalsAdapter;
import com.skepticalone.armour.data.model.CrossCover;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public final class CrossCoverTotalsDialogFragment extends PayableItemTotalsDialogFragment<CrossCover> {

    public static CrossCoverTotalsDialogFragment newInstance(boolean selected) {
        CrossCoverTotalsDialogFragment fragment = new CrossCoverTotalsDialogFragment();
        fragment.setArguments(getArgs(selected));
        return fragment;
    }

    @NonNull
    @Override
    FilteredItemTotalsAdapter<CrossCover> createAdapter(@NonNull Context context) {
        return new SinglePayableTotalsAdapter<>(context, isSelected() ? R.string.selected_cross_cover_shifts : R.string.all_cross_cover_shifts, this);
    }

    @NonNull
    @Override
    ItemViewModelContract<CrossCover> getViewModel(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(CrossCoverViewModel.class);
    }

}
