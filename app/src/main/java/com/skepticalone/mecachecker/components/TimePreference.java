package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.util.AppConstants;
import com.skepticalone.mecachecker.util.DateTimeUtils;

class TimePreference extends DialogPreference {

    private int mTotalMinutes;
    private TimePicker mTimePicker;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.time_preference_layout);
        setPositiveButtonText(R.string.set);
        setNegativeButtonText(R.string.cancel);
    }


    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mTimePicker = (TimePicker) view;
        ((TimePicker) view).setIs24HourView(false);
        int hours = DateTimeUtils.calculateHours(mTotalMinutes);
        int minutes = DateTimeUtils.calculateMinutes(mTotalMinutes);
        if (Build.VERSION.SDK_INT >= 23) {
            mTimePicker.setHour(hours);
            mTimePicker.setMinute(minutes);
        } else {
            //noinspection deprecation
            mTimePicker.setCurrentHour(hours);
            //noinspection deprecation
            mTimePicker.setCurrentMinute(minutes);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mTotalMinutes = getPersistedInt(0);
        } else {
            mTotalMinutes = (int) defaultValue;
            persistInt(mTotalMinutes);
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int hours, minutes;
            if (Build.VERSION.SDK_INT >= 23) {
                hours = mTimePicker.getHour();
                minutes = mTimePicker.getMinute();
            } else {
                //noinspection deprecation
                hours = mTimePicker.getCurrentHour();
                //noinspection deprecation
                minutes = mTimePicker.getCurrentMinute();
            }
            minutes = AppConstants.getSteppedMinutes(minutes);
            mTotalMinutes = DateTimeUtils.calculateTotalMinutes(hours, minutes);
            persistInt(mTotalMinutes);
            notifyChanged();
        }
    }

    @Override
    public CharSequence getSummary() {
        return DateTimeUtils.getTimeString(mTotalMinutes);
    }

}