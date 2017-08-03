package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ExpenseSummaryAdapter;
import com.skepticalone.mecachecker.adapter.ItemSummaryAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

public final class ExpenseTotalsDialogFragment extends TotalsDialogFragment<ExpenseEntity> {

    private final ExpenseSummaryAdapter adapter = new ExpenseSummaryAdapter();

    private ToggleButton unclaimed, claimed, paid;

    @NonNull
    @Override
    ViewModelContract<ExpenseEntity> onCreateViewModel(@NonNull ViewModelProvider provider) {
        return provider.get(ExpenseViewModel.class);
    }

    @Override
    int getLayout() {
        return R.layout.expense_summary;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        //noinspection ConstantConditions
        unclaimed = layout.findViewById(R.id.unclaimed);
        claimed = layout.findViewById(R.id.claimed);
        paid = layout.findViewById(R.id.paid);
        unclaimed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                adapter.includeUnclaimed(isChecked);
            }
        });
        claimed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                adapter.includeClaimed(isChecked);
            }
        });
        paid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                adapter.includePaid(isChecked);
            }
        });
        return layout;
    }

    @NonNull
    @Override
    ItemSummaryAdapter<ExpenseEntity> getAdapter() {
        return adapter;
    }


    //    @NonNull
//    @Override
//    ItemSummaryAdapter<ExpenseEntity> createAdapter() {
//        return new ExpenseSummaryAdapter();
//    }

}
