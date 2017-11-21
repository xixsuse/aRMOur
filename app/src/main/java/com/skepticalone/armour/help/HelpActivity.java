package com.skepticalone.armour.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.skepticalone.armour.ui.common.MainActivity;

public final class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, HelpFragment.newInstance(getIntent().getIntExtra(MainActivity.ITEM_TYPE, 0)))
                    .commit();
        }
    }
}
