package com.skepticalone.mecachecker.components.shifts;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

abstract class DetailFragment extends BaseFragment {
    static final long NO_ID = -1L;
    static final String ITEM_ID = "ITEM_ID";

    private long mItemId;

    static Bundle createArguments(long id) {
        Bundle arguments = new Bundle();
        arguments.putLong(ITEM_ID, id);
        return arguments;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mItemId = getArguments().getLong(ITEM_ID, NO_ID);
        if (mItemId == NO_ID) {
            throw new IllegalStateException();
        }
    }

    final long getItemId() {
        return mItemId;
    }

    @Override
    int getLoaderId() {
        return 0;
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(String.valueOf(getItemId()));
        return textView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
