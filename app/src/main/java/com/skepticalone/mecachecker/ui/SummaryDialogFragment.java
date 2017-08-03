package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemSummaryAdapter;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

import java.util.List;

abstract class SummaryDialogFragment<Entity extends Item> extends LifecycleDialogFragment implements Observer<List<Entity>> {

    static SummaryDialogFragment getNewSummaryDialogFragment(@IdRes int itemType) {
//        if (itemType == R.id.rostered) return new RosteredShiftListFragment();
//        if (itemType == R.id.additional) return new AdditionalShiftListFragment();
//        if (itemType == R.id.cross_cover) return new CrossCoverListFragment();
        if (itemType == R.id.expenses) return new ExpenseSummaryDialogFragment();
        throw new IllegalStateException();
    }

    @NonNull
    abstract ViewModelContract<Entity> onCreateViewModel(@NonNull ViewModelProvider provider);

    @NonNull
    abstract ItemSummaryAdapter<Entity> createAdapter();

    private final ItemSummaryAdapter<Entity> adapter = createAdapter();

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCreateViewModel(ViewModelProviders.of(getActivity())).getItems().observe(this, this);
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.detail_recycler, container, false);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public final void onChanged(@Nullable List<Entity> items) {
        adapter.setItems(items);
    }

}
