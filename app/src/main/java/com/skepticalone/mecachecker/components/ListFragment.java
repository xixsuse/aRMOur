package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.behaviours.ListFragmentBehaviour;
import com.skepticalone.mecachecker.data.Provider;

abstract class ListFragment extends BaseFragment implements ListFragmentBehaviour {

    private final CursorAdapter mAdapter = new CursorAdapter();
    Callbacks listCallbacks;
    private LinearLayoutManager mLayoutManager;
    private boolean mScrollToEndAtNextLoad = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listCallbacks = (Callbacks) context;
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view_list, container, false);
        mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
        );
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
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

    @Override
    public Uri getCreateDeleteContentUri() {
        return getReadContentUri();
    }

    abstract void bindViewHolderToCursor(ListItemViewHolder holder, @NonNull Cursor cursor);

    final void insert(@NonNull ContentValues values) {
        if (getActivity().getContentResolver().insert(getCreateDeleteContentUri(), values) != null) {
            mScrollToEndAtNextLoad = true;
        }
    }

    abstract void onItemClicked(long id);

    interface Callbacks {
        void makeDeletedSnack(@NonNull Uri dirUri, @NonNull ContentValues values);
        void launch(int itemType, long itemId);
    }

    private final class CursorAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

        @Nullable
        private Cursor mCursor = null;

        CursorAdapter() {
            super();
            setHasStableIds(true);
        }

        @Override
        public final long getItemId(int position) {
            assert mCursor != null;
            mCursor.moveToPosition(position);
            return mCursor.getLong(getColumnIndexId());
        }

        final void swapCursor(@Nullable Cursor data) {
            mCursor = data;
            notifyDataSetChanged();
        }

        @Override
        public final ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final ListItemViewHolder viewHolder = new ListItemViewHolder(parent);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClicked(viewHolder.getItemId());
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    long itemId = viewHolder.getItemId();
                    Uri itemUri = Provider.uriWithId(getCreateDeleteContentUri(), itemId);
                    Cursor c = getActivity().getContentResolver().query(itemUri, null, null, null, null);
                    if (c == null) return false;
                    final ContentValues values = new ContentValues();
                    try {
                        if (!c.moveToFirst() || c.getCount() != 1) return false;
                        for (int i = 0, columnCount = c.getColumnCount(); i < columnCount; i++) {
                            String name = c.getColumnName(i);
                            if (!name.equals(BaseColumns._ID)) {
                                switch (c.getType(i)) {
                                    case Cursor.FIELD_TYPE_NULL:
                                        values.putNull(name);
                                        break;
                                    case Cursor.FIELD_TYPE_INTEGER:
                                        values.put(name, c.getLong(i));
                                        break;
                                    case Cursor.FIELD_TYPE_STRING:
                                        values.put(name, c.getString(i));
                                        break;
                                    default:
                                        throw new IllegalStateException();
                                }
                            }
                        }
                    } finally {
                        c.close();
                    }
                    if (getActivity().getContentResolver().delete(itemUri, null, null) == 0)
                        return false;
                    listCallbacks.makeDeletedSnack(getCreateDeleteContentUri(), values);
                    return true;
                }
            });
            return viewHolder;
        }

        @Override
        public final void onBindViewHolder(ListItemViewHolder holder, int position) {
            assert mCursor != null;
            mCursor.moveToPosition(position);
            bindViewHolderToCursor(holder, mCursor);
        }

        @Override
        public final int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }
    }
}
