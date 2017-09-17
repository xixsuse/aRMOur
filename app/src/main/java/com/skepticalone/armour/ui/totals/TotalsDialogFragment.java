package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemTotalsAdapter;
import com.skepticalone.armour.data.model.Item;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

import java.util.List;

public abstract class TotalsDialogFragment<Entity extends Item> extends BottomSheetDialogFragment implements Observer<List<Entity>> {

    private ItemTotalsAdapter<Entity> adapter;

//    static TotalsDialogFragment getNewSummaryDialogFragment(@IdRes int itemType) {
////        if (itemType == R.id.rostered) return new RosteredShiftListFragment();
//        if (itemType == R.id.additional) return new AdditionalShiftListFragment.AdditionalShiftTotalsDialogFragment();
//        if (itemType == R.id.cross_cover) return new CrossCoverListFragment.CrossCoverTotalsDialogFragment();
//        if (itemType == R.id.expenses) return new ExpenseListFragment.ExpenseTotalsDialogFragment();
//        throw new IllegalStateException();
//    }

    @NonNull
    abstract ItemViewModelContract<Entity> getViewModel();

    final ItemTotalsAdapter<Entity> getAdapter() {
        return adapter;
    }

    @LayoutRes
    abstract int getLayout();

    @NonNull
    abstract ItemTotalsAdapter<Entity> createAdapter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = createAdapter();
    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViewModel().getItems().observe(this, this);
    }

    @Override
    public final void onChanged(@Nullable List<Entity> entities) {
        adapter.setItems(entities);
    }

    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(getLayout(), container, false);
        ((RecyclerView) layout.findViewById(R.id.recycler_view)).setAdapter(adapter);
        return layout;
    }

}
