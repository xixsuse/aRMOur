package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.RunReviewAdapter;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;

import java.util.List;

public final class RunReviewDialogFragment extends ItemTotalsDialogFragment<RosteredShift> {

    private RunReviewAdapter adapter;

    @Override
    int getLayout() {
        return R.layout.run_review;
    }

    @Override
    int getTitle() {
        return R.string.run_review;
    }

    @Override
    void onCreateAdapter(@NonNull Context context) {
        adapter = new RunReviewAdapter(context);
    }

    @NonNull
    @Override
    RunReviewAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    LiveData<List<RosteredShift>> getObservedItems(@NonNull ViewModelProvider viewModelProvider) {
        return viewModelProvider.get(RosteredShiftViewModel.class).getSelectedItems();
    }

}
