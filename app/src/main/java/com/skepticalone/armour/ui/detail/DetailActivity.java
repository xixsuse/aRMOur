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

public final class DetailActivity extends CoordinatorActivity {

    public static final String ITEM_TYPE = "ITEM_TYPE";
    public static final String ITEM_ID = "ITEM_ID";
    private static final int NO_ITEM_TYPE = 0;
    private static final String DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private static final long NO_ID = -1L;

    @Override
    protected int getContentView() {
        return R.layout.detail_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int name, itemType = getIntent().getIntExtra(ITEM_TYPE, NO_ITEM_TYPE);
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
        ViewModelProviders.of(this).get(viewModelClass).setCurrentItemId(getIntent().getLongExtra(ITEM_ID, NO_ID));
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.coordinator, DetailFragment.getNewDetailFragment(itemType), DETAIL_FRAGMENT)
                    .commit();
        }
    }

}
