package com.skepticalone.mecachecker.temporary;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ExpenseViewModel;
import com.skepticalone.mecachecker.db.entity.ExpenseEntity;
import com.skepticalone.mecachecker.ui.ExpenseAdapter;
import com.skepticalone.mecachecker.ui.ExpenseClickCallback;

import java.util.List;


public class ListFragment extends LifecycleFragment {

    private ExpenseAdapter mAdapter;
    private RecyclerView.AdapterDataObserver mObserver;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAdapter = new ExpenseAdapter((ExpenseClickCallback) context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.recycler_view_list, container, false);
        final RecyclerView recyclerView = layout.findViewById(R.id.recycler);
        recyclerView.setAdapter(mAdapter);
        mObserver = new RecyclerView.AdapterDataObserver() {

            private static final String TAG = "mObserver";

            @Override
            public void onChanged() {
                Log.d(TAG, "onChanged() called");
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                Log.d(TAG, "onItemRangeChanged() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "]");
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                Log.d(TAG, "onItemRangeChanged() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "], payload = [" + payload + "]");
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                Log.d(TAG, "onItemRangeRemoved() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "]");
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                Log.d(TAG, "onItemRangeMoved() called with: fromPosition = [" + fromPosition + "], toPosition = [" + toPosition + "], itemCount = [" + itemCount + "]");
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                Log.d(TAG, "onItemRangeInserted() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "]");
                Log.d(TAG, "onItemRangeInserted: scrolling to position: " + (positionStart + itemCount - 1));
                recyclerView.getLayoutManager().scrollToPosition(positionStart + itemCount - 1);
            }

        };
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class).getExpenses().observe(this, new Observer<List<ExpenseEntity>>() {
            @Override
            public void onChanged(@Nullable List<ExpenseEntity> expenses) {
                if (expenses != null) {
                    mAdapter.setExpenses(expenses);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.registerAdapterDataObserver(mObserver);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.unregisterAdapterDataObserver(mObserver);
    }

}
