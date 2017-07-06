package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ExpenseViewModel;
import com.skepticalone.mecachecker.data.ItemViewModel;

public class DetailActivity extends LifecycleAppCompatActivity {

    static final String ITEM_TYPE = "ITEM_TYPE";
    static final String ITEM_ID = "ITEM_ID";
    private static final int NO_ITEM_TYPE = 0;
    private static final String DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private static final long NO_ID = -1L;

    @Override
    int getContentViewWithToolbar() {
        return R.layout.detail_activity;
    }

    @Override
    boolean getDisplayHomeAsUpEnabled() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Class<? extends ItemViewModel> viewModelClass;
        switch (getIntent().getIntExtra(ITEM_TYPE, NO_ITEM_TYPE)) {
            case Constants.ITEM_TYPE_EXPENSE:
                viewModelClass = ExpenseViewModel.class;
                break;
            default:
                throw new IllegalStateException();
        }
        ItemViewModel model = ViewModelProviders.of(this).get(viewModelClass);
        model.selectItem(getIntent().getLongExtra(ITEM_ID, NO_ID));
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.detail_fragment_container, new ExpenseDetailFragment(), DETAIL_FRAGMENT)
                    .commit();
        }
    }

}
