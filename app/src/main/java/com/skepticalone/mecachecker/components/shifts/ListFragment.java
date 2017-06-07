package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftType;

abstract class ListFragment extends BaseFragment {

    private final CursorAdapter mAdapter = new CursorAdapter();
    private RecyclerView mRecyclerView;
    private boolean mScrollToEndAtNextLoad = false;

    @DrawableRes
    static int getShiftTypeIcon(ShiftType shiftType) {
        switch (shiftType) {
            case NORMAL_DAY:
                return R.drawable.ic_normal_day_black_24dp;
            case LONG_DAY:
                return R.drawable.ic_long_day_black_24dp;
            case NIGHT_SHIFT:
                return R.drawable.ic_night_shift_black_24dp;
            case OTHER:
                return R.drawable.ic_custom_shift_black_24dp;
            default:
                throw new IllegalStateException();
        }
    }

    @DrawableRes
    static int getClaimStatusIcon(@NonNull Cursor cursor, int columnIndexClaimed, int columnIndexPaid) {
        return cursor.isNull(columnIndexPaid) ? cursor.isNull(columnIndexClaimed) ? R.drawable.ic_check_box_empty_black_24dp : R.drawable.ic_check_box_half_black_24dp : R.drawable.ic_check_box_full_black_24dp;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
        );
        mRecyclerView.setAdapter(mAdapter);
        return mRecyclerView;
    }

    @MenuRes
    abstract int getMenu();

    @Override
    public final void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(getMenu(), menu);
    }

    abstract Uri getContentUri();

    abstract String[] getProjection();

    @Nullable
    abstract String getSortOrder();

    @Override
    public final Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), getContentUri(), getProjection(), null, null, getSortOrder());
    }

    final void scrollToEndAtNextLoad() {
        mScrollToEndAtNextLoad = true;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        if (mScrollToEndAtNextLoad) {
            mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            mScrollToEndAtNextLoad = false;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    abstract int getColumnIndexId();

    abstract void bindViewHolderToCursor(ListItemViewHolder holder, @NonNull Cursor cursor);

    void onViewHolderCreated(ListItemViewHolder holder) {
    }

    abstract void onItemClicked(long id);

    abstract Uri getItemUri(long id);

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
        public ListItemViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
            final ListItemViewHolder viewHolder = new ListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
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
