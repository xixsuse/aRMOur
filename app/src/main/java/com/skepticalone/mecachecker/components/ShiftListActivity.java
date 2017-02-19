package com.skepticalone.mecachecker.components;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.skepticalone.mecachecker.R;

public class ShiftListActivity extends AppCompatActivity implements ShiftListFragment.Listener {
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_shift);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        mTwoPane = findViewById(R.id.item_detail_container) != null;
    }

    @Override
    public void onShiftClicked(long shiftId) {
        if (mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, ShiftDetailFragment.create(shiftId))
                    .commit();
        } else {
            Intent intent = new Intent(this, ShiftDetailActivity.class);
            intent.putExtra(ShiftDetailFragment.SHIFT_ID, shiftId);
            startActivity(intent);
        }
    }
}
