package com.skepticalone.mecachecker.ui;

import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

abstract class PayableDetailFragment<Entity extends Item, ViewModel extends ViewModelContract<Entity>> extends DetailFragment<Entity, ViewModel>
        implements PayableDetailAdapterHelp.Callbacks {

    @Override
    public void onChanged(@Nullable Entity entity) {
        super.onChanged(entity);
    }

}
