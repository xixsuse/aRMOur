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

import org.joda.time.LocalTime;

import static org.joda.time.DateTimeConstants.MINUTES_PER_HOUR;

class TimePreference extends DialogPreference {

    private static final int DEFAULT_TOTAL_MINUTES = 0;
    private int mTotalMinutes;
    private TimePicker mTimePicker;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.time_preference_layout);
        setPositiveButtonText(R.string.set);
        setNegativeButtonText(R.string.cancel);
    }

    private static int calculateTotalMinutes(int hours, int minutes) {
        return hours * MINUTES_PER_HOUR + minutes;
    }

    static int calculateHours(int totalMinutes) {
        return totalMinutes / MINUTES_PER_HOUR;
    }

    static int calculateMinutes(int totalMinutes) {
        return totalMinutes % MINUTES_PER_HOUR;
    }

//    @Override
//    protected View onCreateDialogView() {
//        mTimePicker = new TimePicker(getContext());
//        mTimePicker.setIs24HourView(false);
//        return mTimePicker;
//    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mTimePicker = (TimePicker) view;
        ((TimePicker) view).setIs24HourView(false);
        int hours = calculateHours(mTotalMinutes);
        int minutes = calculateMinutes(mTotalMinutes);
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
        return a.getInteger(index, DEFAULT_TOTAL_MINUTES);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mTotalMinutes = getPersistedInt(DEFAULT_TOTAL_MINUTES);
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
            mTotalMinutes = calculateTotalMinutes(hours, minutes);
            persistInt(mTotalMinutes);
            notifyChanged();
        }
    }

    @Override
    public CharSequence getSummary() {
        return getContext().getString(R.string.time_format, new LocalTime(calculateHours(mTotalMinutes), calculateMinutes(mTotalMinutes)).toDateTimeToday().getMillis());
    }

}
