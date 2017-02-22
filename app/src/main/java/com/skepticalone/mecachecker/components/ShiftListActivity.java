package com.skepticalone.mecachecker.components;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.skepticalone.mecachecker.R;

public class ShiftListActivity extends AppCompatActivity implements ShiftListFragment.Listener {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.shift_preferences, false);
        setContentView(R.layout.shift_list_activity);
        mTwoPane = findViewById(R.id.shift_detail_fragment_container) != null;
    }

    @Override
    public void onShiftClicked(long shiftId) {
        if (mTwoPane) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.shift_detail_fragment_container, ShiftDetailFragment.create(shiftId))
                    .commit();
        } else {
            Intent intent = new Intent(this, ShiftDetailActivity.class);
            intent.putExtra(ShiftDetailFragment.SHIFT_ID, shiftId);
            startActivity(intent);
        }
    }
}
