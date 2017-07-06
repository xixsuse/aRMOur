package com.skepticalone.mecachecker.ui;


import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.data.ExpenseViewModel;
import com.skepticalone.mecachecker.db.entity.ExpenseEntity;
import com.skepticalone.mecachecker.model.Expense;
import com.skepticalone.mecachecker.ui.adapter.ExpenseListAdapter;

public class ExpenseListFragment extends ListFragment<Expense, ExpenseEntity, ExpenseViewModel, ExpenseListAdapter> {

    private final ExpenseListAdapter mAdapter = new ExpenseListAdapter(this);

    @Override
    int getItemType() {
        return Constants.ITEM_TYPE_EXPENSE;
    }

    @Override
    Class<ExpenseViewModel> getViewModelClass() {
        return ExpenseViewModel.class;
    }

    @Override
    ExpenseListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    void setupFab(FloatingActionMenu menu, FloatingActionButton fabNormalDay, FloatingActionButton fabLongDay, FloatingActionButton fabNightShift) {
        menu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewModel().addExpense();
            }
        });
    }

}
