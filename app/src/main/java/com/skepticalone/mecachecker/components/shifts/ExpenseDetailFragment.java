package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

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

    @Override
    Uri getContentUri() {
        return null;
    }

    @Nullable
    @Override
    String[] getProjection() {
        return new String[0];
    }

    @Override
    void useCursor(@Nullable Cursor cursor) {

    }

    @Override
    void onBindViewHolder(ListItemViewHolder holder, int position) {

    }

    @Override
    int getItemCount() {
        return 0;
    }
}
