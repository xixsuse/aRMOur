package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemTotalsAdapter;

import java.util.List;

public abstract class ItemTotalsDialogFragment<FinalItem> extends BottomSheetDialogFragment {

    abstract void onCreateAdapter(@NonNull Context context);

    @NonNull
    abstract ItemTotalsAdapter<FinalItem> getAdapter();

    @NonNull
    abstract LiveData<List<FinalItem>> getObservedItems(@NonNull ViewModelProvider viewModelProvider);

    @LayoutRes
    abstract int getLayout();

    @StringRes
    abstract int getTitle();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onCreateAdapter(context);
    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getObservedItems(ViewModelProviders.of(getActivity())).observe(this, getAdapter());
    }

    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(getLayout(), container, false);
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(getAdapter());
        ((TextView) layout.findViewById(R.id.totals_title)).setText(getTitle());
        return layout;
    }

}
