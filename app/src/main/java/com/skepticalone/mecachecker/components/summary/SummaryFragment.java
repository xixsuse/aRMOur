package com.skepticalone.mecachecker.components.summary;

import android.database.Cursor;
import android.os.Bundle;
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

    private final SummaryAdapter mAdapter = getNewAdapter();

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

    abstract int getLoaderId();

    abstract SummaryAdapter getNewAdapter();

    @Override
    public final void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public final void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    abstract class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {

        @Nullable
        Cursor mCursor = null;

        @Override
        public SummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SummaryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_list_item, parent, false));
        }

        final void swapCursor(@Nullable Cursor cursor) {
            mCursor = cursor;
            notifyDataSetChanged();
        }

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

    }

}
