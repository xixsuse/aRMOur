package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

abstract class DetailFragment extends BaseFragment implements ShowsDialog, CanUpdate {

    static final long NO_ID = -1L;
    static final String ITEM_ID = "ITEM_ID";

    private final Adapter mAdapter = new Adapter();
    private long mItemId;
    private boolean mLoaded = false;

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

    abstract boolean isSwitchType(int position);

    abstract boolean bindPlainListItemViewHolder(PlainListItemViewHolder holder, int position);

    abstract boolean bindSwitchListItemViewHolder(SwitchListItemViewHolder holder, int position);

    PlainListItemViewHolder onCreatePlainListItemViewHolder(ViewGroup parent) {
        return new PlainListItemViewHolder(parent);
    }

    SwitchListItemViewHolder onCreateSwitchListItemViewHolder(ViewGroup parent) {
        throw new IllegalStateException();
    }

    @Override
    public final void showDialogFragment(DialogFragment fragment, String tag) {
        fragment.show(getFragmentManager(), tag);
    }

    @Override
    public final void update(ContentValues contentValues) {
        getActivity().getContentResolver().update(getContentUri(), contentValues, null, null);
    }

    private final class Adapter extends RecyclerView.Adapter<ListItemViewHolder> {

        private static final int
                PLAIN_VIEW_TYPE = 1,
                SWITCH_VIEW_TYPE = 2;

        @Override
        public final int getItemCount() {
            return mLoaded ? getRowCountIfLoaded() : 0;
        }

        @Override
        public final int getItemViewType(int position) {
            return isSwitchType(position) ? SWITCH_VIEW_TYPE : PLAIN_VIEW_TYPE;
//            return AbstractData.isSwitchType(position, getAllData()) ? SWITCH_VIEW_TYPE : PLAIN_VIEW_TYPE;
        }

        @Override
        public final ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == PLAIN_VIEW_TYPE) {
                return onCreatePlainListItemViewHolder(parent);
            } else if (viewType == SWITCH_VIEW_TYPE) {
                return onCreateSwitchListItemViewHolder(parent);
            } else {
                throw new IllegalStateException();
            }
        }

        @Override
        public final void onBindViewHolder(ListItemViewHolder holder, int position) {
            int viewType = holder.getItemViewType();
            if (
                    (viewType == PLAIN_VIEW_TYPE && bindPlainListItemViewHolder((PlainListItemViewHolder) holder, position)) ||
                            (viewType == SWITCH_VIEW_TYPE && bindSwitchListItemViewHolder((SwitchListItemViewHolder) holder, position))
                    ) return;
            throw new IllegalStateException();
        }

    }
}
