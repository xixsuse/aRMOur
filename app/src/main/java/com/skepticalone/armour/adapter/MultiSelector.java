package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.skepticalone.armour.R;

import java.util.HashSet;
import java.util.Set;

final class MultiSelector implements ActionMode.Callback {

    @NonNull
    private final SparseBooleanArray mSelectedPositions = new SparseBooleanArray();

    @NonNull
    private final RecyclerView.Adapter adapter;

    @NonNull
    private final Callbacks callbacks;

    @Nullable
    private ActionMode mSelectMode;

    MultiSelector(@NonNull RecyclerView.Adapter adapter, @NonNull Callbacks callbacks) {
        this.adapter = adapter;
        this.callbacks = callbacks;
    }

    private void setSelected(int position, boolean selected) {
        if (selected) mSelectedPositions.put(position, true);
        else mSelectedPositions.delete(position);
        adapter.notifyItemChanged(position);
    }

    final boolean isSelected(int position) {
        return mSelectedPositions.get(position, false);
    }

    final boolean inSelectMode() {
        return mSelectMode != null;
    }

    final boolean onLongClick(int position) {
        if (mSelectMode == null) {
            mSelectedPositions.clear();
            setSelected(position, true);
            callbacks.startActionMode(this);
            return true;
        }
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    final boolean onClick(int position) {
        if (mSelectMode != null) {
            setSelected(position, !isSelected(position));
            mSelectMode.invalidate();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mSelectMode = mode;
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.contextual_action_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        int size = mSelectedPositions.size();
        if (size == 0) {
            mode.finish();
            return false;
        } else {
            mode.setTitle(callbacks.getTitle(size));
            return true;
        }
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                Set<Long> itemIds = new HashSet<>();
                for (int i = 0; i < mSelectedPositions.size(); i++) {
                    if (mSelectedPositions.valueAt(i)) {
                        itemIds.add(adapter.getItemId(mSelectedPositions.keyAt(i)));
                    }
                }
                mSelectedPositions.clear();
                callbacks.deleteItems(itemIds);
                mSelectMode = null;
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        if (mSelectMode != null) {
            for (int i = 0; i < mSelectedPositions.size(); i++) {
                if (mSelectedPositions.valueAt(i)) {
                    adapter.notifyItemChanged(mSelectedPositions.keyAt(i));
                }
            }
            mSelectMode = null;
        }
        mSelectedPositions.clear();
        callbacks.onActionModeDestroyed();
    }

    interface Callbacks {
        void startActionMode(@NonNull ActionMode.Callback callback);

        void deleteItems(@NonNull Set<Long> itemIds);

        void onActionModeDestroyed();
        @NonNull
        String getTitle(int count);
    }

}
