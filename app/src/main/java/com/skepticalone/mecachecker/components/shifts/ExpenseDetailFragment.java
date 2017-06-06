package com.skepticalone.mecachecker.components.shifts;

import com.skepticalone.mecachecker.R;

public class ExpenseDetailFragment extends DetailFragment {

    public ExpenseDetailFragment() {
    }

    static ExpenseDetailFragment create(long id) {
        ExpenseDetailFragment fragment = new ExpenseDetailFragment();
        fragment.setArguments(createArguments(id));
        return fragment;
    }

    @Override
    int getTitle() {
        return R.string.expense;
    }

}
