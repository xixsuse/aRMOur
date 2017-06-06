package com.skepticalone.mecachecker.components.shifts;

import com.skepticalone.mecachecker.R;

public class AdditionalShiftDetailFragment extends DetailFragment {

    public AdditionalShiftDetailFragment() {
    }

    static AdditionalShiftDetailFragment create(long id) {
        AdditionalShiftDetailFragment fragment = new AdditionalShiftDetailFragment();
        fragment.setArguments(createArguments(id));
        return fragment;
    }

    @Override
    int getTitle() {
        return R.string.additional_shift;
    }
}
