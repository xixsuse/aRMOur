//package com.skepticalone.mecachecker.ui;
//
//import android.arch.lifecycle.LifecycleRegistry;
//import android.arch.lifecycle.LifecycleRegistryOwner;
//import android.os.Bundle;
//import android.support.annotation.CallSuper;
//import android.support.annotation.LayoutRes;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//
//import com.skepticalone.mecachecker.R;
//
//abstract class LifecycleAppCompatActivity extends AppCompatActivity implements LifecycleRegistryOwner {
//
//    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
//
//    @Override
//    public final LifecycleRegistry getLifecycle() {
//        return lifecycleRegistry;
//    }
//
//    @Override
//    @CallSuper
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(getContentViewWithToolbar());
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        //noinspection ConstantConditions
//        getSupportActionBar().setDisplayHomeAsUpEnabled(getDisplayHomeAsUpEnabled());
//    }
//
//    @LayoutRes
//    abstract int getContentViewWithToolbar();
//
//    abstract boolean getDisplayHomeAsUpEnabled();
//
//}
