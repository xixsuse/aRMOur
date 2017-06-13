package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;

abstract class ListFragment extends BaseFragment {

    private final CursorAdapter mAdapter = new CursorAdapter();
    private LinearLayoutManager mLayoutManager;
    private boolean mScrollToEndAtNextLoad = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
        );
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
    }

    @MenuRes
    abstract int getMenu();

    @Override
    public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(getMenu(), menu);
    }

    final void scrollToEndAtNextLoad() {
        mScrollToEndAtNextLoad = true;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        if (mScrollToEndAtNextLoad) {
            mLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
            mScrollToEndAtNextLoad = false;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    abstract int getColumnIndexId();

    abstract void bindViewHolderToCursor(PlainListItemViewHolder holder, @NonNull Cursor cursor);

    void onViewHolderCreated(PlainListItemViewHolder holder) {
    }

    abstract void onItemClicked(long id);

    abstract Uri getItemUri(long id);

    private final class CursorAdapter extends RecyclerView.Adapter<PlainListItemViewHolder> {

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
        public PlainListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final PlainListItemViewHolder viewHolder = new PlainListItemViewHolder(parent);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClicked(viewHolder.getItemId());
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return getActivity().getContentResolver().delete(getItemUri(viewHolder.getItemId()), null, null) > 0;
                }
            });
            onViewHolderCreated(viewHolder);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PlainListItemViewHolder holder, int position) {
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
