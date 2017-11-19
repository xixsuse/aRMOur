package com.skepticalone.armour.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.skepticalone.armour.R;

public final class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rostered_help);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().add(android.R.id.content, HelpFragment.newInstance(getIntent().getIntExtra(MainActivity.ITEM_TYPE, 0))).commit();
//        }
    }
}
