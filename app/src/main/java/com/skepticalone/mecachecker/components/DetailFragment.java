package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.behaviours.DetailFragmentBehaviour;
import com.skepticalone.mecachecker.util.LifecycleConstants;

abstract class DetailFragment extends BaseFragment implements DetailFragmentBehaviour {

    static final int NO_ROW_NUMBER = -1;
    private final Adapter mAdapter = new Adapter();
    private long mItemId;
    private boolean mLoaded = false;

    static DetailFragment create(int itemType, long itemId) {
        DetailFragment fragment;
        switch (itemType) {
            case LifecycleConstants.ITEM_TYPE_ROSTERED_SHIFT:
                fragment = new RosteredShiftDetailFragment();
                break;
            case LifecycleConstants.ITEM_TYPE_ADDITIONAL_SHIFT:
                fragment = new AdditionalShiftDetailFragment();
                break;
            case LifecycleConstants.ITEM_TYPE_CROSS_COVER:
                fragment = new CrossCoverDetailFragment();
                break;
            case LifecycleConstants.ITEM_TYPE_EXPENSE:
                fragment = new ExpenseDetailFragment();
                break;
            default:
                throw new IllegalStateException();
        }
        Bundle args = new Bundle();
        args.putLong(LifecycleConstants.ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!getArguments().containsKey(LifecycleConstants.ITEM_ID)) {
            throw new IllegalStateException();
        }
        mItemId = getArguments().getLong(LifecycleConstants.ITEM_ID);
    }

    final long getItemId() {
        return mItemId;
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view_detail, container, false);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
        );
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
    }

    @Override
    public final void onLoaderReset(Loader<Cursor> loader) {
        mLoaded = false;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public final void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mLoaded = data != null && data.moveToFirst();
        if (mLoaded) {
            readFromPositionedCursor(data);
        }
        mAdapter.notifyDataSetChanged();
    }

    abstract void readFromPositionedCursor(@NonNull Cursor cursor);

    abstract int getRowCountIfLoaded();

    abstract boolean onBindListItemViewHolder(ListItemViewHolder holder, int position);

    @Override
    public Uri getUpdateContentUri() {
        return getReadContentUri();
    }

    @Override
    public final void showDialogFragment(DialogFragment fragment, String tag) {
        fragment.show(getFragmentManager(), tag);
    }

    @Override
    public final void update(ContentValues contentValues) {
        getActivity().getContentResolver().update(getUpdateContentUri(), contentValues, null, null);
    }

    private final class Adapter extends RecyclerView.Adapter<ListItemViewHolder> {

        @Override
        public final int getItemCount() {
            return mLoaded ? getRowCountIfLoaded() : 0;
        }

        @Override
        public final ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ListItemViewHolder(parent);
        }

        @Override
        public final void onBindViewHolder(ListItemViewHolder holder, int position) {
            if (!onBindListItemViewHolder(holder, position)) throw new IllegalStateException();
        }

    }
}
