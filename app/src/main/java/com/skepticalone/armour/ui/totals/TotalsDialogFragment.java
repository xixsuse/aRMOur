package com.skepticalone.armour.ui.totals;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemTotalsAdapter;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public abstract class TotalsDialogFragment<Entity> extends BottomSheetDialogFragment {

    static final String SELECTED = "SELECTED";

    private boolean selected;

    private ItemTotalsAdapter<Entity> adapter;

    @NonNull
    abstract ItemViewModelContract<Entity> getViewModel();

    final ItemTotalsAdapter<Entity> getAdapter() {
        return adapter;
    }

    @LayoutRes
    abstract int getLayout();

    @NonNull
    abstract ItemTotalsAdapter<Entity> createAdapter(@NonNull Context context);

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        selected = getArguments().getBoolean(SELECTED);
        adapter = createAdapter(context);
    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        (selected ? getViewModel().getSelectedItems() : getViewModel().getAllItems()).observe(this, adapter);
    }

    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(getLayout(), container, false);
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        ((TextView) layout.findViewById(R.id.totals_title)).setText(selected ? R.string.subtotals : R.string.totals);
        return layout;
    }

    final boolean isSelected() {
        return selected;
    }

}
