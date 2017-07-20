package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDialogFragment;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.ErrorMessageObserver;
import com.skepticalone.mecachecker.data.viewModel.ItemViewModel;
import com.skepticalone.mecachecker.dialog.CommentDialogFragment;

abstract class DetailFragment<ItemType extends Item, Entity extends ItemType, ViewModel extends ItemViewModel<Entity>> extends BaseFragment<ItemDetailAdapter<ItemType>, ViewModel>
        implements ItemDetailAdapter.Callbacks, CommentDialogFragment.Callbacks {

    private static final String DIALOG_FRAGMENT = "DIALOG_FRAGMENT";

    private final ErrorMessageObserver errorMessageObserver = new ErrorMessageObserver(){
        @Override
        public void update(@StringRes int errorMessage) {
            snackbarCallbacks.showSnackbar(errorMessage);
        }
    };

    @Override
    final int getLayout() {
        return R.layout.detail_recycler;
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViewModel().selectedItem.observe(this, new Observer<Entity>() {
                    @Override
                    public void onChanged(@Nullable Entity entity) {
                        if (entity != null) getAdapter().setItem(entity);
                    }
                }
        );
    }

    final void showDialogFragment(AppCompatDialogFragment dialogFragment) {
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT);
    }

    @Override
    public final void changeComment(long id, @Nullable String currentComment) {
        showDialogFragment(CommentDialogFragment.newInstance(id, currentComment));
    }

    @Override
    public final void setComment(long itemId, @Nullable String trimmedComment) {
        getViewModel().setComment(itemId, trimmedComment);
    }

    @Override
    public void onResume() {
        super.onResume();
        getViewModel().errorMessage.addObserver(errorMessageObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        getViewModel().errorMessage.deleteObserver(errorMessageObserver);
    }
}
