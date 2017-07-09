package com.skepticalone.mecachecker.ui;


import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.CrossCoverEntity;
import com.skepticalone.mecachecker.data.CrossCoverViewModel;
import com.skepticalone.mecachecker.model.CrossCover;
import com.skepticalone.mecachecker.ui.adapter.CrossCoverListAdapter;
import com.skepticalone.mecachecker.ui.adapter.ItemListAdapter;

public class CrossCoverListFragment extends SingleAddListFragment<CrossCover, CrossCoverEntity, CrossCoverViewModel> {

    @NonNull
    @Override
    ItemListAdapter<CrossCover> onCreateAdapter() {
        return new CrossCoverListAdapter(this);
    }

    @Override
    int getItemType() {
        return Constants.ITEM_TYPE_CROSS_COVER;
    }

    @Override
    Class<CrossCoverViewModel> getViewModelClass() {
        return CrossCoverViewModel.class;
    }

}