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
            ShiftEditFragment fragment = new ShiftEditFragment();
            Bundle arguments = new Bundle();
            arguments.putLong(ShiftEditFragment.SHIFT_ID, getIntent().getLongExtra(ShiftEditFragment.SHIFT_ID, ShiftEditFragment.NO_ID));
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment, ShiftEditFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ShiftEditFragment fragment = (ShiftEditFragment) getFragmentManager().findFragmentByTag(ShiftEditFragment.TAG);
        if (fragment != null) {
            fragment.onDateSet(year, month, dayOfMonth);
        }
    }

    @Override
    public void onStartTimeSet(int hourOfDay, int minute) {
        ShiftEditFragment fragment = (ShiftEditFragment) getFragmentManager().findFragmentByTag(ShiftEditFragment.TAG);
        if (fragment != null) {
            fragment.onStartTimeSet(hourOfDay, minute);
        }
    }

    @Override
    public void onEndTimeSet(int hourOfDay, int minute) {
        ShiftEditFragment fragment = (ShiftEditFragment) getFragmentManager().findFragmentByTag(ShiftEditFragment.TAG);
        if (fragment != null) {
            fragment.onEndTimeSet(hourOfDay, minute);
        }
    }
}
