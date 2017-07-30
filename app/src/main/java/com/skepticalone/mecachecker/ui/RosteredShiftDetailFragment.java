package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.adapter.RosteredShiftDetailAdapter;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.mecachecker.dialog.RosteredShiftDateDialogFragment;
import com.skepticalone.mecachecker.dialog.RosteredShiftTimeDialogFragment;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class RosteredShiftDetailFragment
        extends DetailFragment<RosteredShiftEntity, RosteredShiftViewModel>
        implements RosteredShiftDetailAdapter.Callbacks {

    @NonNull
    @Override
    ItemDetailAdapter<RosteredShiftEntity> createAdapter(Context context) {
        return new RosteredShiftDetailAdapter(this, ShiftUtil.Calculator.getInstance(context));
    }

    @NonNull
    @Override
    RosteredShiftViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(RosteredShiftViewModel.class);
    }

    @Override
    public void changeDate() {
        showDialogFragment(new RosteredShiftDateDialogFragment());
    }

    @Override
    public void changeTime(boolean start, boolean logged) {
        showDialogFragment(RosteredShiftTimeDialogFragment.newInstance(start, logged));
    }

}
