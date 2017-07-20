package com.skepticalone.mecachecker.ui;


import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.CrossCoverListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.model.CrossCover;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;

public final class CrossCoverListFragment extends SingleAddListFragment<CrossCover, CrossCoverEntity, CrossCoverViewModel> {

    @Override
    int getItemType() {
        return Constants.ITEM_TYPE_CROSS_COVER;
    }

    @NonNull
    @Override
    ItemListAdapter<CrossCover> createAdapter(Context context) {
        return new CrossCoverListAdapter(this);
    }

    @NonNull
    @Override
    CrossCoverViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(CrossCoverViewModel.class);
    }
}