package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

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
