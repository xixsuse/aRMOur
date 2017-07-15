package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.CrossCoverDetailAdapter;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.CrossCoverEntity;
import com.skepticalone.mecachecker.data.CrossCoverViewModel;
import com.skepticalone.mecachecker.dialog.DatePickerDialogFragment;
import com.skepticalone.mecachecker.model.CrossCover;

import org.joda.time.LocalDate;

public final class CrossCoverDetailFragment
        extends PayableDetailFragment<CrossCover, CrossCoverEntity, CrossCoverViewModel>
        implements CrossCoverDetailAdapter.Callbacks, DatePickerDialogFragment.Callbacks {

    @NonNull
    @Override
    ItemDetailAdapter<CrossCover> createAdapter() {
        return new CrossCoverDetailAdapter(this);
    }

    @NonNull
    @Override
    CrossCoverViewModel createViewModel() {
        return ViewModelProviders.of(getActivity()).get(CrossCoverViewModel.class);
    }

    @Override
    public void changeDate(long itemId, @NonNull LocalDate currentDate) {
        showDialogFragment(DatePickerDialogFragment.newInstance(itemId, currentDate));
    }

    @Override
    public void onDateSet(long itemId, @NonNull LocalDate date) {
        getViewModel().setDateSync(itemId, date);
    }

}
