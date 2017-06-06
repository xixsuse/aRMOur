package com.skepticalone.mecachecker.components.shifts;

import com.skepticalone.mecachecker.R;

public class RosteredShiftDetailFragment extends DetailFragment {

    public RosteredShiftDetailFragment() {
    }

    static RosteredShiftDetailFragment create(long id) {
        RosteredShiftDetailFragment fragment = new RosteredShiftDetailFragment();
        fragment.setArguments(createArguments(id));
        return fragment;
    }

    @Override
    int getTitle() {
        return R.string.rostered_shift;
    }
}
