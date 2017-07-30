package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;
import com.skepticalone.mecachecker.dialog.ExpenseCommentDialogFragment;
import com.skepticalone.mecachecker.util.Snackbar;

abstract class DetailFragment<Entity extends Item, ViewModel extends ViewModelContract<Entity>> extends BaseFragment<ItemDetailAdapter<Entity>, ViewModel>
        implements ItemDetailAdapter.Callbacks {

    private static final String DIALOG_FRAGMENT = "DIALOG_FRAGMENT";
    private Snackbar snackbar;
    @Nullable
    private Entity currentItem;

    static DetailFragment getNewDetailFragment(@IdRes int itemType) {
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
    final int getLayout() {
        return R.layout.detail_recycler;
    }

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

    @Nullable
    final Entity getCurrentItem() {
        return currentItem;
    }

    final void showDialogFragment(DialogFragment dialogFragment) {
        dialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT);
    }

    @Override
    public void changeComment() {
        showDialogFragment(new ExpenseCommentDialogFragment());
    }

}
