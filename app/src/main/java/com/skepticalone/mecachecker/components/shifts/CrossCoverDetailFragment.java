package com.skepticalone.mecachecker.components.shifts;

import com.skepticalone.mecachecker.R;

public class CrossCoverDetailFragment extends DetailFragment {

    public CrossCoverDetailFragment() {
    }

    static CrossCoverDetailFragment create(long id) {
        CrossCoverDetailFragment fragment = new CrossCoverDetailFragment();
        fragment.setArguments(createArguments(id));
        return fragment;
    }

    @Override
    int getTitle() {
        return R.string.cross_cover;
    }

}
