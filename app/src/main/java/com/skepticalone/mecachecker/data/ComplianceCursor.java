package com.skepticalone.mecachecker.data;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.joda.time.Duration;
import org.joda.time.Interval;

public class ComplianceCursor extends CursorWrapper {
    private static final String TAG = "ComplianceCursor";


    public ComplianceCursor(Cursor cursor) {
        super(cursor);
        Log.d(TAG, "ComplianceCursor() called with: cursor = [" + cursor + "]");
    }

    public long getId() {
        return getLong(ShiftContract.Compliance.COLUMN_INDEX_ID);
    }

    @NonNull
    public Interval getRosteredShift() {
        return new Interval(getLong(ShiftContract.Compliance.COLUMN_INDEX_ROSTERED_START), getLong(ShiftContract.Compliance.COLUMN_INDEX_ROSTERED_END));
    }

    @Nullable
    public Interval getLoggedShift() {
        return (isNull(ShiftContract.Compliance.COLUMN_INDEX_LOGGED_START) || isNull(ShiftContract.Compliance.COLUMN_INDEX_LOGGED_END)) ?
                null :
                new Interval(getLong(ShiftContract.Compliance.COLUMN_INDEX_LOGGED_START), getLong(ShiftContract.Compliance.COLUMN_INDEX_LOGGED_END));
    }

    public ShiftType getShiftType() {
        switch (getInt(ShiftContract.Compliance.COLUMN_INDEX_SHIFT_TYPE)) {
            case ShiftContract.Compliance.SHIFT_TYPE_NORMAL_DAY:
                return ShiftType.NORMAL_DAY;
            case ShiftContract.Compliance.SHIFT_TYPE_LONG_DAY:
                return ShiftType.LONG_DAY;
            case ShiftContract.Compliance.SHIFT_TYPE_NIGHT_SHIFT:
                return ShiftType.NIGHT_SHIFT;
            case ShiftContract.Compliance.SHIFT_TYPE_OTHER:
                return ShiftType.OTHER;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Nullable
    public Interval getIntervalBetweenShifts() {
        return isNull(ShiftContract.Compliance.COLUMN_INDEX_LAST_SHIFT_ROSTERED_END) ? null : new Interval(getLong(ShiftContract.Compliance.COLUMN_INDEX_LAST_SHIFT_ROSTERED_END), getLong(ShiftContract.Compliance.COLUMN_INDEX_ROSTERED_START));
    }

    @NonNull
    public Duration getDurationOverDay() {
        return new Duration(getLong(ShiftContract.Compliance.COLUMN_INDEX_DURATION_OVER_DAY));
    }

    @NonNull
    public Duration getDurationOverWeek() {
        return new Duration(getLong(ShiftContract.Compliance.COLUMN_INDEX_DURATION_OVER_WEEK));
    }

    @NonNull
    public Duration getDurationOverFortnight() {
        return new Duration(getLong(ShiftContract.Compliance.COLUMN_INDEX_DURATION_OVER_FORTNIGHT));
    }

    @Nullable
    public Interval getCurrentWeekend() {
        if (isNull(ShiftContract.Compliance.COLUMN_INDEX_CURRENT_WEEKEND_START) || isNull(ShiftContract.Compliance.COLUMN_INDEX_CURRENT_WEEKEND_END)) {
            return null;
        }
        return new Interval(getLong(ShiftContract.Compliance.COLUMN_INDEX_CURRENT_WEEKEND_START), getLong(ShiftContract.Compliance.COLUMN_INDEX_CURRENT_WEEKEND_END));
    }

    @Nullable
    public Interval getPreviousWeekend() {
        if (isNull(ShiftContract.Compliance.COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_START) || isNull(ShiftContract.Compliance.COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_END)) {
            return null;
        }
        return new Interval(getLong(ShiftContract.Compliance.COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_START), getLong(ShiftContract.Compliance.COLUMN_INDEX_PREVIOUS_WEEKEND_WORKED_END));
    }

    public boolean consecutiveWeekendsWorked() {
        return getInt(ShiftContract.Compliance.COLUMN_INDEX_CONSECUTIVE_WEEKENDS_WORKED) == 1;
    }

}
