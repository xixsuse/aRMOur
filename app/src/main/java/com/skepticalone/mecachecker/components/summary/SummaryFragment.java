package com.skepticalone.mecachecker.components.summary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;

public abstract class SummaryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final SummaryAdapter mAdapter = new SummaryAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(getLoaderId(), null, this);
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

    abstract void bindViewHolderToCursor(SummaryViewHolder holder, int row, @NonNull Cursor cursor);

    final class SummaryViewHolder extends RecyclerView.ViewHolder {
        final TextView
                titleView, unclaimedView, claimedView, paidView, totalView;
        final PieView
                pieView;
        final ProgressBar
                progressBar;

        private SummaryViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.title);
            unclaimedView = (TextView) itemView.findViewById(R.id.unclaimed);
            claimedView = (TextView) itemView.findViewById(R.id.claimed);
            paidView = (TextView) itemView.findViewById(R.id.paid);
            totalView = (TextView) itemView.findViewById(R.id.total);
            pieView = (PieView) itemView.findViewById(R.id.pie);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
        }
    }

    private final class SummaryAdapter extends RecyclerView.Adapter<SummaryViewHolder> {

        @Nullable
        private Cursor mCursor = null;

        @Override
        public SummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SummaryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_list_item, parent, false));
        }

        @Override
        public final void onBindViewHolder(SummaryViewHolder holder, int position) {
            assert mCursor != null;
            bindViewHolderToCursor(holder, position, mCursor);
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
