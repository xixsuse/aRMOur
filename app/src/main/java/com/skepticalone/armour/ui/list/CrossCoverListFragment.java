package com.skepticalone.armour.ui.list;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.CrossCoverListAdapter;
import com.skepticalone.armour.data.model.CrossCover;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.ui.totals.CrossCoverTotalsDialogFragment;

public final class CrossCoverListFragment extends SingleAddListFragment<CrossCover> {

    private CrossCoverListAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new CrossCoverListAdapter(context, this);
    }

    @Override
    int getItemType() {
        return R.id.cross_cover;
    }

    @NonNull
    @Override
    protected CrossCoverListAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    protected CrossCoverViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

    @NonNull
    @Override
    CrossCoverTotalsDialogFragment createSummaryDialogFragment() {
        return new CrossCoverTotalsDialogFragment();
    }
}
