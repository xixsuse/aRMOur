package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.FilteredItemTotalsAdapter;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

import java.util.List;

abstract class FilteredItemTotalsDialogFragment<FinalItem> extends ItemTotalsDialogFragment<FinalItem> {

    private static final String SELECTED = "SELECTED";
    private boolean selected;
    private FilteredItemTotalsAdapter<FinalItem> adapter;

    static Bundle getArgs(boolean selected) {
        Bundle args = new Bundle();
        args.putBoolean(SELECTED, selected);
        return args;
    }

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        selected = getArguments().getBoolean(SELECTED);
    }

    @Override
    final void onCreateAdapter(@NonNull Context context) {
        adapter = createAdapter(context);
    }

    @NonNull
    abstract FilteredItemTotalsAdapter<FinalItem> createAdapter(@NonNull Context context);

    @NonNull
    @Override
    final FilteredItemTotalsAdapter<FinalItem> getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    final LiveData<List<FinalItem>> getObservedItems(@NonNull ViewModelProvider viewModelProvider) {
        ItemViewModelContract<FinalItem> viewModel = getViewModel(viewModelProvider);
        return isSelected() ? viewModel.getSelectedItems() : viewModel.getAllItems();
    }

    @Override
    final int getTitle() {
        return isSelected() ? R.string.subtotals : R.string.totals;
    }

    @NonNull
    abstract ItemViewModelContract<FinalItem> getViewModel(@NonNull ViewModelProvider viewModelProvider);

    final boolean isSelected() {
        return selected;
    }

}
