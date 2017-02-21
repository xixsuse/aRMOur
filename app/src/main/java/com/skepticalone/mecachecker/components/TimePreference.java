package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.util.AppConstants;

import java.util.Calendar;


@SuppressWarnings("WeakerAccess")
public class TimePreference extends DialogPreference {

    private final Calendar calendar = Calendar.getInstance();
    private int mHours, mMinutes;
    @Nullable
    private TimePicker mTimePicker;

    public TimePreference(Context context) {
        this(context, null);
    }

    @SuppressWarnings({"WeakerAccess", "SameParameterValue"})
    public TimePreference(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.dialogPreferenceStyle);
    }

    @SuppressWarnings({"WeakerAccess", "SameParameterValue"})
    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPositiveButtonText(R.string.set);
        setNegativeButtonText(R.string.cancel);
    }

    private static int calculateTotalMinutes(int hours, int minutes) {
        return hours * AppConstants.MINUTES_PER_HOUR + minutes;
    }

    static int calculateHours(int totalMinutes) {
        return totalMinutes / AppConstants.MINUTES_PER_HOUR;
    }

    static int calculateMinutes(int totalMinutes) {
        return totalMinutes % AppConstants.MINUTES_PER_HOUR;
    }

    @Override
    protected View onCreateDialogView() {
        mTimePicker = new TimePicker(getContext());
        mTimePicker.setIs24HourView(false);
        return mTimePicker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        if (mTimePicker != null) {
            if (Build.VERSION.SDK_INT >= 23) {
                mTimePicker.setHour(mHours);
                mTimePicker.setMinute(mMinutes);
            } else {
                //noinspection deprecation
                mTimePicker.setCurrentHour(mHours);
                //noinspection deprecation
                mTimePicker.setCurrentMinute(mMinutes);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        int totalMinutes = defaultValue == null ? 0 : (int) defaultValue;
        if (restorePersistedValue) {
            totalMinutes = getPersistedInt(totalMinutes);
        }
        mHours = calculateHours(totalMinutes);
        mMinutes = calculateMinutes(totalMinutes);
        setSummary(getSummary());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult && mTimePicker != null) {
            if (Build.VERSION.SDK_INT >= 23) {
                mHours = mTimePicker.getHour();
                mMinutes = mTimePicker.getMinute();
            } else {
                //noinspection deprecation
                mHours = mTimePicker.getCurrentHour();
                //noinspection deprecation
                mMinutes = mTimePicker.getCurrentMinute();
            }
            mMinutes = AppConstants.getSteppedMinutes(mMinutes);
            setSummary(getSummary());
            int totalMinutes = calculateTotalMinutes(mHours, mMinutes);
            if (callChangeListener(totalMinutes)) {
                persistInt(totalMinutes);
                notifyChanged();
            }
        }

    }

    @Override
    public CharSequence getSummary() {
        calendar.set(Calendar.HOUR_OF_DAY, mHours);
        calendar.set(Calendar.MINUTE, mMinutes);
        return getContext().getString(R.string.time_format, calendar);
    }

}
