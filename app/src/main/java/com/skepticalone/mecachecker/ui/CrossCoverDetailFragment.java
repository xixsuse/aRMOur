package com.skepticalone.mecachecker.ui;

import com.skepticalone.mecachecker.data.CrossCoverViewModel;
import com.skepticalone.mecachecker.db.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.model.CrossCover;
import com.skepticalone.mecachecker.ui.adapter.CrossCoverDetailAdapter;

public class CrossCoverDetailFragment
        extends DetailFragment<CrossCover, CrossCoverEntity, CrossCoverViewModel, CrossCoverDetailAdapter>
        implements CrossCoverDetailAdapter.Callbacks {

    private final CrossCoverDetailAdapter mAdapter = new CrossCoverDetailAdapter(this);

    @Override
    CrossCoverDetailAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    Class<CrossCoverViewModel> getViewModelClass() {
        return CrossCoverViewModel.class;
    }

    @Override
    public void setClaimed(long id, boolean claimed) {
        getViewModel().setClaimed(id, claimed);
    }

    @Override
    public void setPaid(long id, boolean paid) {
        getViewModel().setPaid(id, paid);
    }

}
