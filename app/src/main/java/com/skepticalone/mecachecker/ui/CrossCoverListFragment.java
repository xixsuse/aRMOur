package com.skepticalone.mecachecker.ui;


import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.CrossCoverListAdapter;
import com.skepticalone.mecachecker.adapter.CrossCoverTotalsAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.adapter.ItemTotalsAdapter;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

public final class CrossCoverListFragment extends SingleAddListFragment<CrossCoverEntity, CrossCoverViewModel> {

    @Override
    int getItemType() {
        return R.id.cross_cover;
    }

    @NonNull
    @Override
    ItemListAdapter<CrossCoverEntity> createAdapter(Context context) {
        return new CrossCoverListAdapter(this);
    }

    @NonNull
    @Override
    CrossCoverViewModel createViewModel(@NonNull ViewModelProvider provider) {
        return provider.get(CrossCoverViewModel.class);
    }

    public static final class CrossCoverTotalsDialogFragment extends PayableTotalsDialogFragment<CrossCoverEntity> {

        @NonNull
        @Override
        ItemTotalsAdapter<CrossCoverEntity> createAdapter(@NonNull Context context) {
            return new CrossCoverTotalsAdapter(this);
        }

        @NonNull
        @Override
        ViewModelContract<CrossCoverEntity> onCreateViewModel(@NonNull ViewModelProvider provider) {
            return provider.get(CrossCoverViewModel.class);
        }

    }
}
