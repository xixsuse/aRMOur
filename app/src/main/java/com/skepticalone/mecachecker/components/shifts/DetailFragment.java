package com.skepticalone.mecachecker.components.shifts;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;

abstract class DetailFragment extends BaseFragment {
    static final long NO_ID = -1L;
    static final String ITEM_ID = "ITEM_ID";

    private final Adapter mAdapter = new Adapter();
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
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
        );
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
    }

    @Override
    public final void onLoaderReset(Loader<Cursor> loader) {
        useCursor(null);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public final void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        useCursor(data);
        mAdapter.notifyDataSetChanged();
    }

    abstract void useCursor(@Nullable Cursor cursor);

    abstract void onBindViewHolder(ListItemViewHolder holder, int position);

    abstract int getItemCount();

    private final class Adapter extends ListItemViewAdapter {

//        static final int ITEM_VIEW_TYPE_TEXT, ITEM_VIEW_TYPE_SWITCH;
//
//        @Override
//        public int getItemViewType(int position) {
//            return super.getItemViewType(position);
//        }

        @Override
        public void onBindViewHolder(ListItemViewHolder holder, int position) {
            DetailFragment.this.onBindViewHolder(holder, position);
        }

        @Override
        public int getItemCount() {
            return DetailFragment.this.getItemCount();
        }

        @Override
        public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ListItemViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
            viewHolder.secondaryIcon.setImageResource(R.drawable.ic_pencil_black_24dp);
            return viewHolder;
        }
    }
}
