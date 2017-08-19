package com.skepticalone.armour.data.newData;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.ShiftUtil;

import org.joda.time.Interval;


final class LiveShiftTypeCalculator extends LiveData<ShiftTypeCalculator> implements SharedPreferences.OnSharedPreferenceChangeListener {

    @NonNull
    private final String
            keyNormalDayStart,
            keyNormalDayEnd,
            keyLongDayStart,
            keyLongDayEnd,
            keyNightShiftStart,
            keyNightShiftEnd;

    private final int
            defaultNormalDayStart,
            defaultNormalDayEnd,
            defaultLongDayStart,
            defaultLongDayEnd,
            defaultNightShiftStart,
            defaultNightShiftEnd;

    LiveShiftTypeCalculator(@NonNull Application application) {
        Resources resources = application.getResources();
        keyNormalDayStart = resources.getString(R.string.key_start_normal_day);
        keyNormalDayEnd = resources.getString(R.string.key_end_normal_day);
        keyLongDayStart = resources.getString(R.string.key_start_long_day);
        keyLongDayEnd = resources.getString(R.string.key_end_long_day);
        keyNightShiftStart = resources.getString(R.string.key_start_night_shift);
        keyNightShiftEnd = resources.getString(R.string.key_end_night_shift);
        defaultNormalDayStart = resources.getInteger(R.integer.default_start_normal_day);
        defaultNormalDayEnd = resources.getInteger(R.integer.default_end_normal_day);
        defaultLongDayStart = resources.getInteger(R.integer.default_start_long_day);
        defaultLongDayEnd = resources.getInteger(R.integer.default_end_long_day);
        defaultNightShiftStart = resources.getInteger(R.integer.default_start_night_shift);
        defaultNightShiftEnd = resources.getInteger(R.integer.default_end_night_shift);
        updateCalculator(PreferenceManager.getDefaultSharedPreferences(application));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (
                key.equals(keyNormalDayStart) ||
                        key.equals(keyNormalDayEnd) ||
                        key.equals(keyLongDayStart) ||
                        key.equals(keyLongDayEnd) ||
                        key.equals(keyNightShiftStart) ||
                        key.equals(keyNightShiftEnd)
                ) {
            updateCalculator(sharedPreferences);
        }
    }

    private void updateCalculator(@NonNull SharedPreferences sharedPreferences) {
        setValue(new CalculatorImplementation(
                sharedPreferences.getInt(keyNormalDayStart, defaultNormalDayStart),
                sharedPreferences.getInt(keyNormalDayEnd, defaultNormalDayEnd),
                sharedPreferences.getInt(keyLongDayStart, defaultLongDayStart),
                sharedPreferences.getInt(keyLongDayEnd, defaultLongDayEnd),
                sharedPreferences.getInt(keyNightShiftStart, defaultNightShiftStart),
                sharedPreferences.getInt(keyNightShiftEnd, defaultNightShiftEnd)
        ));
    }

    private static final class CalculatorImplementation implements ShiftTypeCalculator {

        private final int
                normalDayStart,
                normalDayEnd,
                longDayStart,
                longDayEnd,
                nightShiftStart,
                nightShiftEnd;

        private CalculatorImplementation(
                int normalDayStart,
                int normalDayEnd,
                int longDayStart,
                int longDayEnd,
                int nightShiftStart,
                int nightShiftEnd
        ) {
            this.normalDayStart = normalDayStart;
            this.normalDayEnd = normalDayEnd;
            this.longDayStart = longDayStart;
            this.longDayEnd = longDayEnd;
            this.nightShiftStart = nightShiftStart;
            this.nightShiftEnd = nightShiftEnd;
        }

        @Override
        public ShiftUtil.ShiftType getShiftType(Interval interval) {
            int start = interval.getStart().getMinuteOfDay(), end = interval.getEnd().getMinuteOfDay();
            return
                    (start == normalDayStart && end == normalDayEnd) ? ShiftUtil.ShiftType.NORMAL_DAY :
                            (start == longDayStart && end == longDayEnd) ? ShiftUtil.ShiftType.LONG_DAY :
                                    (start == nightShiftStart && end == nightShiftEnd) ? ShiftUtil.ShiftType.NIGHT_SHIFT :
                                            ShiftUtil.ShiftType.CUSTOM;
        }
    }
}
