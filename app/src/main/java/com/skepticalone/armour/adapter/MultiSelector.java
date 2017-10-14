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

public final class MultiSelector implements ActionMode.Callback {

    @NonNull
    private final RecyclerView.Adapter adapter;

    @NonNull
    private final UiCallbacks uiCallbacks;

    @NonNull
    private final ModelCallbacks modelCallbacks;

    @Nullable
    private ActionMode mSelectMode;

    MultiSelector(@NonNull RecyclerView.Adapter adapter, @NonNull UiCallbacks uiCallbacks, @NonNull ModelCallbacks modelCallbacks) {
        this.adapter = adapter;
        this.uiCallbacks = uiCallbacks;
        this.modelCallbacks = modelCallbacks;
        if (this.modelCallbacks.getSelectedPositions().size() > 0) {
            uiCallbacks.startActionMode(this);
        }
    }

    private void setSelected(int position, boolean selected) {
        if (selected) modelCallbacks.getSelectedPositions().put(position, true);
        else modelCallbacks.getSelectedPositions().delete(position);
        adapter.notifyItemChanged(position);
    }

    final boolean isSelected(int position) {
        return modelCallbacks.getSelectedPositions().get(position, false);
    }

    final boolean inSelectMode() {
        return mSelectMode != null;
    }

    final boolean onLongClick(int position) {
        if (mSelectMode == null) {
            modelCallbacks.getSelectedPositions().clear();
            setSelected(position, true);
            uiCallbacks.startActionMode(this);
            return true;
        }
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public final boolean onClick(int position) {
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
        int size = modelCallbacks.getSelectedPositions().size();
        if (size == 0) {
            mode.finish();
            return false;
        } else {
            mode.setTitle(modelCallbacks.getTitle(size));
            return true;
        }
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                modelCallbacks.deleteItems(adapter);
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
            for (int i = 0; i < modelCallbacks.getSelectedPositions().size(); i++) {
                if (modelCallbacks.getSelectedPositions().valueAt(i)) {
                    adapter.notifyItemChanged(modelCallbacks.getSelectedPositions().keyAt(i));
                }
            }
            mSelectMode = null;
        }
        modelCallbacks.getSelectedPositions().clear();
        uiCallbacks.onActionModeDestroyed();
    }

    public interface UiCallbacks {
        void startActionMode(@NonNull ActionMode.Callback callback);
        void onActionModeDestroyed();
    }

    public interface ModelCallbacks {
        @NonNull
        String getTitle(int count);

        @NonNull
        SparseBooleanArray getSelectedPositions();

        void deleteItems(@NonNull RecyclerView.Adapter adapter);
    }

}
