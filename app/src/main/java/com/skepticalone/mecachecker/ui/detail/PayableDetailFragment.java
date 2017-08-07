package com.skepticalone.mecachecker.ui.detail;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.PayableDetailAdapter;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.PayableViewModelHelper;

abstract class PayableDetailFragment<Entity extends Item> extends DetailFragment<Entity>
        implements PayableDetailAdapter.Callbacks {

    @Override
    public final void setClaimed(boolean claimed) {
        getPayableViewModelHelper().setClaimed(getCurrentItem().getId(), claimed);
    }

    @Override
    public final void setPaid(boolean paid) {
        getPayableViewModelHelper().setPaid(getCurrentItem().getId(), paid);
    }

    @NonNull
    abstract PayableViewModelHelper getPayableViewModelHelper();

}
