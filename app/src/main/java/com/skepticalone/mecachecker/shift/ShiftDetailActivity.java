package com.skepticalone.mecachecker.shift;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;

import com.skepticalone.mecachecker.R;


public class ShiftDetailActivity extends AppCompatActivity implements
        TimePickerFragment.OnShiftTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_detail_activity);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ShiftDetailFragment fragment = (ShiftDetailFragment) getSupportFragmentManager().findFragmentById(R.id.shift_detail_fragment);
        if (fragment != null) {
            fragment.onDateSet(year, month, dayOfMonth);
        }
    }

    @Override
    public void onStartTimeSet(int hourOfDay, int minute) {
        ShiftDetailFragment fragment = (ShiftDetailFragment) getSupportFragmentManager().findFragmentById(R.id.shift_detail_fragment);
        if (fragment != null) {
            fragment.onStartTimeSet(hourOfDay, minute);
        }
    }

    @Override
    public void onEndTimeSet(int hourOfDay, int minute) {
        ShiftDetailFragment fragment = (ShiftDetailFragment) getSupportFragmentManager().findFragmentById(R.id.shift_detail_fragment);
        if (fragment != null) {
            fragment.onEndTimeSet(hourOfDay, minute);
        }

    }
}
