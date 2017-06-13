package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.view.MenuItem;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

abstract class ShiftTypeAwareItemListFragment extends ListFragment {

    private ShiftUtil.Calculator mCalculator;

    @Nullable
    private Instant mLastShiftEnd = null;

    abstract DateTime getEarliestStartForNewShift(@Nullable Instant lastShiftEnd);

    boolean shouldSkipWeekendsOnAdding(ShiftType shiftType) {
        return false;
    }

    abstract void addShift(@NonNull Interval newShift);

    private void addShift(ShiftType shiftType) {
        DateTime earliestStartForNewShift = getEarliestStartForNewShift(mLastShiftEnd);
        DateTime newStart = earliestStartForNewShift.withTime(mCalculator.getStartTime(shiftType));
        boolean skipWeekends = shouldSkipWeekendsOnAdding(shiftType);
        while (newStart.isBefore(earliestStartForNewShift) || (skipWeekends && newStart.getDayOfWeek() >= DateTimeConstants.SATURDAY)) {
            newStart = newStart.plusDays(1);
        }
        DateTime newEnd = newStart.withTime(mCalculator.getEndTime(shiftType));
        if (!newEnd.isAfter(newStart)) {
            newEnd = newEnd.plusDays(1);
        }
        addShift(new Interval(newStart, newEnd));
    }

    abstract int getColumnIndexShiftEnd();

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        mLastShiftEnd = data.moveToLast() ? new Instant(data.getLong(getColumnIndexShiftEnd())) : null;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        super.onLoaderReset(loader);
        mLastShiftEnd = null;
    }

    @Override
    final int getMenu() {
        return R.menu.shift_list_menu;
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_normal_day:
                addShift(ShiftType.NORMAL_DAY);
                return true;
            case R.id.add_long_day:
                addShift(ShiftType.LONG_DAY);
                return true;
            case R.id.add_night_shift:
                addShift(ShiftType.NIGHT_SHIFT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCalculator = new ShiftUtil.Calculator(context);
    }

    ShiftType getShiftType(Interval shift) {
        return mCalculator.getShiftType(shift);
    }

    DateTime getNewStart(ShiftType shiftType, DateTime minStart) {
        return minStart.withTime(mCalculator.getStartTime(shiftType));
    }

    LocalTime getEndTime(ShiftType shiftType) {
        return mCalculator.getEndTime(shiftType);
    }

}
