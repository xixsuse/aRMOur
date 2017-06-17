package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.behaviours.DetailFragmentBehaviour;
import com.skepticalone.mecachecker.behaviours.Shift;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

class ShiftData extends AbstractData {

    private final Callbacks mCallbacks;
    private Interval mShift;
    private LocalDate mDate;
    @NonNull
    private final View.OnClickListener
            mDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallbacks.showDialogFragment(getShiftDatePickerDialogFragment(mDate, mShift.getStart().toLocalTime(), mShift.getEnd().toLocalTime()), LifecycleConstants.DATE_DIALOG);
        }
    },
            mStartListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.showDialogFragment(getShiftTimePickerDialogFragment(true), LifecycleConstants.DATE_DIALOG);
                }
            },
            mEndListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.showDialogFragment(getShiftTimePickerDialogFragment(false), LifecycleConstants.DATE_DIALOG);
                }
            };
    private ShiftType mShiftType;

    ShiftData(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @NonNull
    LocalDate getShiftDate() {
        return mDate;
    }

    long getShiftStartMillis() {
        return mShift.getStartMillis();
    }

    long getShiftEndMillis() {
        return mShift.getEndMillis();
    }

    @CallSuper
    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        mShift = new Interval(cursor.getLong(mCallbacks.getColumnIndexStartOrDate()), cursor.getLong(mCallbacks.getColumnIndexEnd()));
        mDate = mShift.getStart().toLocalDate();
        mShiftType = mCallbacks.getShiftType(mShift);
    }

    @CallSuper
    @Nullable
    @Override
    ViewHolderType getViewHolderType(int position) {
        if (position == mCallbacks.getRowNumberDate() || position == mCallbacks.getRowNumberStart() || position == mCallbacks.getRowNumberEnd() || position == mCallbacks.getRowNumberShiftType()) {
            return ViewHolderType.PLAIN;
        } else {
            return null;
        }
    }

    @CallSuper
    @Override
    public boolean bindToHolder(Context context, PlainListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberDate()) {
            holder.rootBind(context, R.drawable.ic_calendar_black_24dp, R.string.date, DateTimeUtils.getFullDateString(mShift.getStart()), mDateListener);
        } else if (position == mCallbacks.getRowNumberStart()) {
            holder.rootBind(context, R.drawable.ic_play_black_24dp, R.string.start, DateTimeUtils.getTimeString(mShift.getStart()), mStartListener);
        } else if (position == mCallbacks.getRowNumberEnd()) {
            holder.rootBind(context, R.drawable.ic_stop_black_24dp, R.string.end, DateTimeUtils.getTimeString(mShift.getEnd(), getShiftDate()), mEndListener);
        } else if (position == mCallbacks.getRowNumberShiftType()) {
            holder.rootBind(context, ShiftUtil.getShiftTypeIcon(mShiftType), R.string.shift_type, DateTimeUtils.getShiftTypeWithDurationString(context.getString(ShiftUtil.getShiftTypeTitle(mShiftType)), mShift), null);
        } else return false;
        return true;
    }

    @NonNull
    ShiftDatePickerDialogFragment getShiftDatePickerDialogFragment(@NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end) {
        return ShiftDatePickerDialogFragment.newInstance(mCallbacks.getUpdateContentUri(), date, start, end, mCallbacks.getColumnNameStartOrDate(), mCallbacks.getColumnNameEnd());
    }

    @NonNull
    private ShiftTimePickerDialogFragment getShiftTimePickerDialogFragment(boolean isStart) {
        return ShiftTimePickerDialogFragment.newInstance(mCallbacks.getUpdateContentUri(), isStart, mDate, mShift.getStart().toLocalTime(), mShift.getEnd().toLocalTime(), mCallbacks.getColumnNameStartOrDate(), mCallbacks.getColumnNameEnd());
    }

    interface Callbacks extends DetailFragmentBehaviour, Shift {
    }
}
