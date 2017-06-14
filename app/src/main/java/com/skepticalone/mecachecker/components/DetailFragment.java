package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.CallSuper;
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

import static com.skepticalone.mecachecker.components.ViewHolderType.PLAIN;
import static com.skepticalone.mecachecker.components.ViewHolderType.SWITCH;

abstract class DetailFragment extends BaseFragment implements DetailFragmentBehaviour {

    static final long NO_ID = -1L;
    static final int NO_ROW_NUMBER = -1;
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

    @Nullable
    abstract ViewHolderType getViewHolderType(int position);

    abstract boolean bindPlainListItemViewHolder(PlainListItemViewHolder holder, int position);

    abstract boolean bindSwitchListItemViewHolder(SwitchListItemViewHolder holder, int position);

    @CallSuper
    PlainListItemViewHolder createPlainListItemViewHolder(ViewGroup parent) {
        return new PlainListItemViewHolder(parent);
    }

    SwitchListItemViewHolder createSwitchListItemViewHolder(ViewGroup parent) {
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
            ViewHolderType type = getViewHolderType(position);
            if (type == PLAIN) return PLAIN_VIEW_TYPE;
            if (type == SWITCH) return SWITCH_VIEW_TYPE;
            throw new IllegalStateException();
        }

        @Override
        public final ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case PLAIN_VIEW_TYPE:
                    return createPlainListItemViewHolder(parent);
                case SWITCH_VIEW_TYPE:
                    return createSwitchListItemViewHolder(parent);
                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public final void onBindViewHolder(ListItemViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case PLAIN_VIEW_TYPE:
                    if (bindPlainListItemViewHolder((PlainListItemViewHolder) holder, position))
                        return;
                    break;
                case SWITCH_VIEW_TYPE:
                    if (bindSwitchListItemViewHolder((SwitchListItemViewHolder) holder, position))
                        return;
                    break;
            }
            throw new IllegalStateException();
        }

    }
}
