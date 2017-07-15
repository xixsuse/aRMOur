package com.skepticalone.mecachecker.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.skepticalone.mecachecker.R;

abstract class CoordinatorActivity extends AppCompatActivity implements SnackCallbacks {

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

    @Override
    public final void showSnackbar(@NonNull CharSequence text) {
        Snackbar.make(mCoordinatorLayout, text, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public final void showSnackbar(@NonNull CharSequence text, @NonNull CharSequence action, @NonNull View.OnClickListener listener) {
        Snackbar.make(mCoordinatorLayout, text, Snackbar.LENGTH_LONG).setAction(action, listener).show();
    }
}
