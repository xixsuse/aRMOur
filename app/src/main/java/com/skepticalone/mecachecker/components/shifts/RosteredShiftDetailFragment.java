package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

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

    @Override
    int getLoaderId() {
        return 0;
    }

    @Override
    Uri getContentUri() {
        return null;
    }

    @Override
    String getColumnNameClaimed() {
        return null;
    }

    @Override
    String getColumnNamePaid() {
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
    void onBindViewHolder(PlainListItemViewHolder holder, int position) {

    }

    @Override
    void onBindViewHolder(SwitchListItemViewHolder holder, int position) {

    }

    @Override
    int getItemCount() {
        return 0;
    }

    @Override
    boolean isSwitchType(int position) {
        return false;
    }
}
