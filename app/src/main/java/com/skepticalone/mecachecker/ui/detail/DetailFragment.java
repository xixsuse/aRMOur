package com.skepticalone.mecachecker.ui.detail;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.ui.common.BaseFragment;
import com.skepticalone.mecachecker.util.Snackbar;

public abstract class DetailFragment<Entity extends Item> extends BaseFragment<Entity>
        implements ItemDetailAdapter.Callbacks {

    private Snackbar snackbar;
    @Nullable
    private Entity currentItem;

    public static DetailFragment getNewDetailFragment(@IdRes int itemType) {
        if (itemType == R.id.rostered) return new RosteredShiftDetailFragment();
        if (itemType == R.id.additional) return new AdditionalShiftDetailFragment();
        if (itemType == R.id.cross_cover) return new CrossCoverDetailFragment();
        if (itemType == R.id.expenses) return new ExpenseDetailFragment();
        throw new IllegalStateException();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        snackbar = (Snackbar) context;
    }

    @Override
    public final int getLayout() {
        return R.layout.detail_recycler;
    }

    @NonNull
    @Override
    public abstract ItemDetailAdapter<Entity> getAdapter();

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViewModel().getCurrentItem().observe(this, new Observer<Entity>() {
            @Override
            public void onChanged(@Nullable Entity entity) {
                currentItem = entity;
                getAdapter().setItem(entity);
            }
        });
        getViewModel().getErrorMessage().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer text) {
                if (text != null && getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    snackbar.showSnackbar(text);
                }
            }
        });
    }

    @NonNull
    final Entity getCurrentItem() {
        if (currentItem == null) throw new IllegalStateException();
        return currentItem;
    }

}
