package com.skepticalone.mecachecker.ui.common;

import android.arch.lifecycle.ViewModelProviders;
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
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.data.viewModel.ItemViewModelContract;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;

public abstract class CoordinatorActivity extends AppCompatActivity implements com.skepticalone.mecachecker.util.Snackbar {

    CoordinatorLayout mCoordinatorLayout;

    @LayoutRes
    protected abstract int getContentView();

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mCoordinatorLayout = findViewById(R.id.coordinator);
    }

    @StringRes
    protected static int getName(@IdRes int itemType) {
        if (itemType == R.id.rostered) return R.string.rostered_shift;
        if (itemType == R.id.additional) return R.string.additional_shift;
        if (itemType == R.id.cross_cover) return R.string.cross_cover;
        if (itemType == R.id.expenses) return R.string.expense;
        throw new IllegalArgumentException();
    }

    @NonNull
    protected ItemViewModelContract getViewModel(@IdRes int itemType) {
        if (itemType == R.id.rostered) return ViewModelProviders.of(this).get(RosteredShiftViewModel.class);
        if (itemType == R.id.additional) return ViewModelProviders.of(this).get(AdditionalShiftViewModel.class);
        if (itemType == R.id.cross_cover) return ViewModelProviders.of(this).get(CrossCoverViewModel.class);
        if (itemType == R.id.expenses) return ViewModelProviders.of(this).get(ExpenseViewModel.class);
        throw new IllegalStateException();
    }

    @Override
    public final void showSnackbar(@StringRes int text) {
        Snackbar.make(mCoordinatorLayout, text, Snackbar.LENGTH_LONG).show();
    }

}
