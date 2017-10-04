package com.skepticalone.armour.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.AppConstants;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.LocalTime;

public final class TimePreference extends IntegerPreference {

    private TimePicker mTimePicker;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.time_preference_layout);
        setPositiveButtonText(R.string.set);
        setNegativeButtonText(R.string.cancel);
    }

    @NonNull
    public static LocalTime getTime(int totalMinutes) {
        return LocalTime.of(DateTimeUtils.calculateHours(totalMinutes), DateTimeUtils.calculateMinutes(totalMinutes));
    }

    @NonNull
    static LocalTime getTime(@NonNull SharedPreferences preferences, @NonNull String key) {
        return getTime(preferences.getInt(key, DEFAULT_VALUE));
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mTimePicker = (TimePicker) view;
        mTimePicker.setIs24HourView(DateFormat.is24HourFormat(mTimePicker.getContext()));
        int hours = DateTimeUtils.calculateHours(getValue());
        int minutes = DateTimeUtils.calculateMinutes(getValue());
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
            setValue(DateTimeUtils.getTotalMinutes(hours, minutes));
        }
    }

    @Override
    public CharSequence getSummary() {
        return DateTimeUtils.getTimeString(getTime(getValue()));
    }

}