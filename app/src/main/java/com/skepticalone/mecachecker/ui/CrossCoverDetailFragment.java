package com.skepticalone.mecachecker.ui;

import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.skepticalone.mecachecker.data.CrossCoverEntity;
import com.skepticalone.mecachecker.data.CrossCoverViewModel;
import com.skepticalone.mecachecker.model.CrossCover;
import com.skepticalone.mecachecker.ui.adapter.CrossCoverDetailAdapter;
import com.skepticalone.mecachecker.ui.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.ui.components.DatePickerDialogFragment;

import org.joda.time.LocalDate;

public class CrossCoverDetailFragment
        extends DetailFragment<CrossCover, CrossCoverEntity, CrossCoverViewModel>
        implements CrossCoverDetailAdapter.Callbacks, DatePickerDialogFragment.Callbacks {

    @NonNull
    @Override
    ItemDetailAdapter<CrossCover> onCreateAdapter() {
        return new CrossCoverDetailAdapter(this);
    }

    @Override
    Class<CrossCoverViewModel> getViewModelClass() {
        return CrossCoverViewModel.class;
    }

    @Override
    public void changeDate(long itemId, @NonNull LocalDate currentDate) {
        DatePickerDialogFragment fragment = DatePickerDialogFragment.newInstance(itemId, currentDate);
        fragment.setTargetFragment(this, 0);
        fragment.show(getFragmentManager(), DIALOG_FRAGMENT);
    }

    @Override
    public void onDateSet(long itemId, @NonNull LocalDate date) {
        try {
            getViewModel().setDate(itemId, date);
        } catch (SQLiteConstraintException e) {
            Toast.makeText(getActivity(), "Date overlaps!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setClaimed(long itemId, boolean claimed) {
        getViewModel().setClaimed(itemId, claimed);
    }

    @Override
    public void setPaid(long itemId, boolean paid) {
        getViewModel().setPaid(itemId, paid);
    }

}
