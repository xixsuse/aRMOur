package com.skepticalone.armour.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.armour.data.viewModel.CrossCoverViewModel;
import com.skepticalone.armour.data.viewModel.ExpenseViewModel;
import com.skepticalone.armour.data.viewModel.ItemViewModel;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.armour.ui.common.CoordinatorActivity;
import com.skepticalone.armour.ui.common.MainActivity;

public final class DetailActivity extends CoordinatorActivity {

    private static final String DETAIL_FRAGMENT = "DETAIL_FRAGMENT";

    @Override
    protected int getContentView() {
        return R.layout.detail_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getBoolean(R.bool.has_two_panes)) {
            finish();
            return;
        }
        final int name, itemType = getIntent().getIntExtra(MainActivity.ITEM_TYPE, -1);
        final Class<? extends ItemViewModel> viewModelClass;
        switch (itemType) {
            case R.id.rostered:
                name = R.string.rostered_shift;
                viewModelClass = RosteredShiftViewModel.class;
                break;
            case R.id.additional:
                name = R.string.additional_shift;
                viewModelClass = AdditionalShiftViewModel.class;
                break;
            case R.id.cross_cover:
                name = R.string.cross_cover_shift;
                viewModelClass = CrossCoverViewModel.class;
                break;
            case R.id.expenses:
                name = R.string.expense;
                viewModelClass = ExpenseViewModel.class;
                break;
            default:
                throw new IllegalStateException();
        }
        setTitle(name);
        ViewModelProviders.of(this).get(viewModelClass).setCurrentItemId(getIntent().getLongExtra(MainActivity.ITEM_ID, -1L));
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.coordinator, DetailFragment.getNewDetailFragment(itemType), DETAIL_FRAGMENT)
                    .commit();
        }
    }

}
