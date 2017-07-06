package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ItemViewModel;
import com.skepticalone.mecachecker.model.Item;
import com.skepticalone.mecachecker.ui.adapter.ItemDetailAdapter;


abstract class DetailFragment<ItemType extends Item, Entity extends ItemType, ViewModel extends ItemViewModel<Entity>, Adapter extends ItemDetailAdapter<ItemType>> extends LifecycleFragment {

    static final String DIALOG_FRAGMENT = "DIALOG_FRAGMENT";
    private ViewModel mModel;

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view_detail, container, false);
        recyclerView.setAdapter(getAdapter());
        return recyclerView;
    }

    abstract Adapter getAdapter();

    abstract Class<ViewModel> getViewModelClass();

    final ViewModel getViewModel() {
        return mModel;
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mModel = ViewModelProviders.of(getActivity()).get(getViewModelClass());
        mModel.getSelectedItem().observe(this, new Observer<Entity>() {
                    @Override
                    public void onChanged(@Nullable Entity entity) {
                        if (entity != null) getAdapter().setItem(entity);
                    }
                }
        );
    }

}
