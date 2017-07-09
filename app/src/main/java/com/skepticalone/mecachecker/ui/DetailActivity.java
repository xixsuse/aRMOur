package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.skepticalone.mecachecker.data.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.ExpenseViewModel;
import com.skepticalone.mecachecker.data.ItemViewModel;

public class DetailActivity extends AppCompatActivity {

    static final String ITEM_TYPE = "ITEM_TYPE";
    static final String ITEM_ID = "ITEM_ID";
    private static final int NO_ITEM_TYPE = 0;
    private static final String DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private static final long NO_ID = -1L;
//
//    @Override
//    int getContentViewWithToolbar() {
//        return R.layout.detail_activity;
//    }
//
//    @Override
//    boolean getDisplayHomeAsUpEnabled() {
//        return true;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Class<? extends ItemViewModel> viewModelClass;
        switch (getIntent().getIntExtra(ITEM_TYPE, NO_ITEM_TYPE)) {
            case Constants.ITEM_TYPE_CROSS_COVER:
                viewModelClass = CrossCoverViewModel.class;
                break;
            case Constants.ITEM_TYPE_EXPENSE:
                viewModelClass = ExpenseViewModel.class;
                break;
            default:
                throw new IllegalStateException();
        }
        ItemViewModel model = ViewModelProviders.of(this).get(viewModelClass);
        model.selectItem(getIntent().getLongExtra(ITEM_ID, NO_ID));
        if (savedInstanceState == null) {
            DetailFragment detailFragment;
            switch (getIntent().getIntExtra(ITEM_TYPE, NO_ITEM_TYPE)) {
                case Constants.ITEM_TYPE_CROSS_COVER:
                    detailFragment = new CrossCoverDetailFragment();
                    break;
                case Constants.ITEM_TYPE_EXPENSE:
                    detailFragment = new ExpenseDetailFragment();
                    break;
                default:
                    throw new IllegalStateException();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, detailFragment, DETAIL_FRAGMENT)
                    .commit();
        }
    }

}
