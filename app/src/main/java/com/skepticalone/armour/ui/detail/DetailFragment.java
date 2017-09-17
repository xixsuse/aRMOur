package com.skepticalone.armour.ui.detail;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemDetailAdapter;
import com.skepticalone.armour.data.model.Item;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;
import com.skepticalone.armour.ui.common.BaseFragment;
import com.skepticalone.armour.ui.dialog.CommentDialogFragment;
import com.skepticalone.armour.util.Snackbar;

public abstract class DetailFragment<Entity extends Item> extends BaseFragment<Entity> implements ItemDetailAdapter.Callbacks {

    private Snackbar snackbar;

    public static DetailFragment getNewDetailFragment(@IdRes int itemType) {
        if (itemType == R.id.rostered) return new RosteredShiftDetailFragment();
        if (itemType == R.id.additional) return new AdditionalShiftDetailFragment();
        if (itemType == R.id.cross_cover) return new CrossCoverDetailFragment();
        if (itemType == R.id.expenses) return new ExpenseDetailFragment();
        throw new IllegalStateException();
    }

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        snackbar = (Snackbar) context;
    }

    @Override
    protected final int getLayout() {
        return R.layout.detail_recycler;
    }

    @NonNull
    @Override
    protected abstract ItemDetailAdapter<Entity> getAdapter();

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ItemViewModelContract<Entity> viewModel = getViewModel();
        viewModel.getCurrentItem().observe(this, getAdapter());
        viewModel.getErrorMessage().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer text) {
                if (text != null && getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    snackbar.showSnackbar(text);
                }
            }
        });
    }

    @NonNull
    abstract CommentDialogFragment<Entity> createCommentDialogFragment();

    @Override
    public final void changeComment() {
        showDialogFragment(createCommentDialogFragment());
    }
}
