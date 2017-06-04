package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;

abstract class AbstractItemListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final CursorAdapter mAdapter = new CursorAdapter();

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
        );
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getTitle());
        getLoaderManager().initLoader(getLoaderId(), null, this);
    }

    @StringRes
    abstract int getTitle();

    abstract int getLoaderId();

    abstract Uri getContentUri();

    abstract String[] getProjection();

    @Nullable
    abstract String getSortOrder();

    @Override
    public final Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), getContentUri(), getProjection(), null, null, getSortOrder());
    }

    @Override
    public final void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public final void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    abstract int getColumnIndexId();

    abstract void bindViewHolderToCursor(ListItemViewHolder holder, @NonNull Cursor cursor);

    void onViewHolderCreated(ListItemViewHolder holder) {
    }

    private final class CursorAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

        @Nullable
        private Cursor mCursor = null;

        CursorAdapter() {
            super();
            setHasStableIds(true);
        }

        @Override
        public long getItemId(int position) {
            assert mCursor != null;
            mCursor.moveToPosition(position);
            return mCursor.getLong(getColumnIndexId());
        }

        private void swapCursor(@Nullable Cursor data) {
            mCursor = data;
            notifyDataSetChanged();
        }

        @Override
        public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ListItemViewHolder viewHolder = new ListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
            onViewHolderCreated(viewHolder);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ListItemViewHolder holder, int position) {
            assert mCursor != null;
            mCursor.moveToPosition(position);
            bindViewHolderToCursor(holder, mCursor);
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }
    }

}
