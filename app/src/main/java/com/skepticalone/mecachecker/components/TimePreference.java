package com.skepticalone.mecachecker.components;

import android.annotation.TargetApi;
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

import org.joda.time.LocalTime;

import static org.joda.time.DateTimeConstants.MINUTES_PER_HOUR;

public class TimePreference extends DialogPreference {

    private int mTotalMinutes;
    @Nullable
    private TimePicker mTimePicker;

    @TargetApi(21)
    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(21)
    public TimePreference(Context context) {
        super(context);
        init();
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

    private void init() {
        setPositiveButtonText(R.string.set);
        setNegativeButtonText(R.string.cancel);
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
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        mTotalMinutes = defaultValue == null ? 0 : (int) defaultValue;
        if (restorePersistedValue) {
            mTotalMinutes = getPersistedInt(mTotalMinutes);
        }
        setSummary(getSummary());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult && mTimePicker != null) {
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
            setSummary(getSummary());
            if (callChangeListener(mTotalMinutes)) {
                persistInt(mTotalMinutes);
                notifyChanged();
            }
        }
    }

    @Override
    public CharSequence getSummary() {
        return getContext().getString(R.string.time_format, new LocalTime(calculateHours(mTotalMinutes), calculateMinutes(mTotalMinutes)).toDateTimeToday().getMillis());
    }

}
