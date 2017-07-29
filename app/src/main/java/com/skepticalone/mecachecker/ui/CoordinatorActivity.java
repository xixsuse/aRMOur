package com.skepticalone.mecachecker.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.ItemDaoContract;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.data.viewModel.ItemViewModel;

abstract class CoordinatorActivity extends AppCompatActivity implements com.skepticalone.mecachecker.util.Snackbar {

    CoordinatorLayout mCoordinatorLayout;

    @LayoutRes
    abstract int getContentView();

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mCoordinatorLayout = findViewById(R.id.coordinator);
    }

    @NonNull
    static Class<? extends ItemViewModel<? extends Item, ? extends ItemDaoContract>> getViewModelClass(@IdRes int itemType) {
        if (itemType == R.id.additional) return AdditionalShiftViewModel.class;
        if (itemType == R.id.cross_cover) return CrossCoverViewModel.class;
        if (itemType == R.id.expenses) return ExpenseViewModel.class;
        throw new IllegalStateException();
    }

    @Override
    public final void showSnackbar(@StringRes int text) {
        Snackbar.make(mCoordinatorLayout, text, Snackbar.LENGTH_LONG).show();
    }

}
