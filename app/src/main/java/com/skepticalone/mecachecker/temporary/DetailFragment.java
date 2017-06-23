package com.skepticalone.mecachecker.temporary;

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
import com.skepticalone.mecachecker.data.ExpenseViewModel;
import com.skepticalone.mecachecker.db.entity.ExpenseEntity;
import com.skepticalone.mecachecker.ui.ExpenseDetailAdapter;


public class DetailFragment extends LifecycleFragment {

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view_detail, container, false);
        return mRecyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ExpenseViewModel model = ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
        final ExpenseDetailAdapter adapter = new ExpenseDetailAdapter(model);
        mRecyclerView.setAdapter(adapter);
        model.getSelectedExpense().observe(this, new Observer<ExpenseEntity>() {
            @Override
            public void onChanged(@Nullable ExpenseEntity expenseEntity) {
                if (expenseEntity != null) adapter.setExpense(expenseEntity);
            }
        });
    }

}
