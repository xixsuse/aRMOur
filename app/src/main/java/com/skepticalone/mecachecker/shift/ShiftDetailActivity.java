package com.skepticalone.mecachecker.shift;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;

public class ShiftDetailActivity extends AppCompatActivity implements
        TimePickerFragment.OnShiftTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            ShiftDetailFragment fragment = new ShiftDetailFragment();
            Bundle arguments = new Bundle();
            arguments.putLong(ShiftDetailFragment.SHIFT_ID, getIntent().getLongExtra(ShiftDetailFragment.SHIFT_ID, ShiftDetailFragment.NO_ID));
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment, ShiftDetailFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ShiftDetailFragment fragment = (ShiftDetailFragment) getFragmentManager().findFragmentByTag(ShiftDetailFragment.TAG);
        if (fragment != null) {
            fragment.onDateSet(year, month, dayOfMonth);
        }
    }

    @Override
    public void onStartTimeSet(int hourOfDay, int minute) {
        ShiftDetailFragment fragment = (ShiftDetailFragment) getFragmentManager().findFragmentByTag(ShiftDetailFragment.TAG);
        if (fragment != null) {
            fragment.onStartTimeSet(hourOfDay, minute);
        }
    }

    @Override
    public void onEndTimeSet(int hourOfDay, int minute) {
        ShiftDetailFragment fragment = (ShiftDetailFragment) getFragmentManager().findFragmentByTag(ShiftDetailFragment.TAG);
        if (fragment != null) {
            fragment.onEndTimeSet(hourOfDay, minute);
        }
    }
}
