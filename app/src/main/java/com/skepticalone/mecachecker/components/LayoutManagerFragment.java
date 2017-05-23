package com.skepticalone.mecachecker.components;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.skepticalone.mecachecker.R;


public abstract class LayoutManagerFragment extends ShiftTypeAwareFragment {

    RecyclerView.LayoutManager mLayoutManager;

    abstract RecyclerView.Adapter getAdapter();

    @LayoutRes
    abstract public int getLayout();

    abstract boolean shouldAddDivider();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mLayoutManager = recyclerView.getLayoutManager();
        recyclerView.setAdapter(getAdapter());
        if (shouldAddDivider()) {
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
            );
        }
    }
}
