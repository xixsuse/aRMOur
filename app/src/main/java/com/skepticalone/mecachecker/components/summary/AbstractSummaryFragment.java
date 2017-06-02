package com.skepticalone.mecachecker.components.summary;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;

public abstract class AbstractSummaryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final SummaryAdapter mAdapter = new SummaryAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.summary_fragment, container, false);
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(getLoaderId(), null, this);
    }

    abstract String[] getProjection();

    abstract Uri getContentUri();

    @Override
    public final Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), getContentUri(), getProjection(), null, null, null);
    }

    @Override
    public final void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public final void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    abstract int getLoaderId();

    abstract int getRowCount();

    abstract void bindCursor(SummaryViewHolder holder, int row, @NonNull Cursor cursor);

    void onViewHolderCreated(SummaryViewHolder holder) {
    }

    final class SummaryViewHolder extends RecyclerView.ViewHolder {
        final TextView
                titleView, totalView, unclaimedView, claimedView, paidView;
        final View
                subtotalsView;
        final PieView
                pieView;

        private SummaryViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.title);
            totalView = (TextView) itemView.findViewById(R.id.total);
            subtotalsView = itemView.findViewById(R.id.subtotals);
            unclaimedView = (TextView) subtotalsView.findViewById(R.id.unclaimed);
            claimedView = (TextView) subtotalsView.findViewById(R.id.claimed);
            paidView = (TextView) subtotalsView.findViewById(R.id.paid);
            pieView = (PieView) itemView.findViewById(R.id.pie);
        }
    }

    private final class SummaryAdapter extends RecyclerView.Adapter<SummaryViewHolder> {

        @Nullable
        private Cursor mCursor = null;

        @Override
        public SummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SummaryViewHolder viewHolder = new SummaryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_list_item, parent, false));
            onViewHolderCreated(viewHolder);
            return viewHolder;
        }

        @Override
        public final void onBindViewHolder(SummaryViewHolder holder, int position) {
            assert mCursor != null;
            bindCursor(holder, position, mCursor);
        }

        @Override
        public final int getItemCount() {
            return mCursor == null ? 0 : getRowCount();
        }

        private void swapCursor(@Nullable Cursor cursor) {
            mCursor = cursor;
            notifyDataSetChanged();
        }

    }



}
