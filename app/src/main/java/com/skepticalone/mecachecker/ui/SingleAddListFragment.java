package com.skepticalone.mecachecker.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ItemViewModel;
import com.skepticalone.mecachecker.model.Item;

import java.util.List;

abstract class SingleAddListFragment<ItemType extends Item, Entity extends ItemType, ViewModel extends ItemViewModel<Entity>> extends ListFragment<ItemType, Entity, ViewModel> {

    private FloatingActionButton mFab;

    @Override
    final int getLayout() {
        return R.layout.single_add_list_fragment;
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFab = view.findViewById(R.id.add_button);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });
        mFab.hide(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mFab.hide(true);
    }

    @Override
    public void onChanged(@Nullable List<Entity> entities) {
        super.onChanged(entities);
        if (entities == null) {
            mFab.hide(true);
        } else {
            mFab.show(true);
        }
    }

    final void addItem() {
        getViewModel().insertRandomItem();
    }
}

