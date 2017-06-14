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

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

class ShiftData extends AbstractData {

    private final Callbacks mCallbacks;
    private Interval mShift;
    @NonNull
    private final View.OnClickListener
            mDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallbacks.showDialogFragment(getShiftDatePickerDialogFragment(mShift.getStart().toLocalDate(), mShift.getStart().toLocalTime(), mShift.getEnd().toLocalTime()), LifecycleConstants.DATE_DIALOG);
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
    DateTime getShiftStart() {
        return mShift.getStart();
    }

    @NonNull
    DateTime getShiftEnd() {
        return mShift.getEnd();
    }

    @CallSuper
    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        mShift = new Interval(cursor.getLong(mCallbacks.getColumnIndexStartOrDate()), cursor.getLong(mCallbacks.getColumnIndexEnd()));
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
            holder.rootBind(context, R.drawable.ic_play_arrow_black_24dp, R.string.start, DateTimeUtils.getTimeString(mShift, true, null), mStartListener);
        } else if (position == mCallbacks.getRowNumberEnd()) {
            holder.rootBind(context, R.drawable.ic_stop_black_24dp, R.string.end, DateTimeUtils.getTimeString(mShift, false, null), mEndListener);
        } else if (position == mCallbacks.getRowNumberShiftType()) {
            holder.rootBind(context, ShiftUtil.getShiftTypeIcon(mShiftType), R.string.shift_type, context.getString(ShiftUtil.getShiftTypeTitle(mShiftType)), null);
        } else return false;
        return true;
    }

    @NonNull
    ShiftDatePickerDialogFragment getShiftDatePickerDialogFragment(@NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end) {
        return ShiftDatePickerDialogFragment.newInstance(mCallbacks.getContentUri(), date, start, end, mCallbacks.getColumnNameStartOrDate(), mCallbacks.getColumnNameEnd());
    }

    @NonNull
    private ShiftTimePickerDialogFragment getShiftTimePickerDialogFragment(boolean isStart) {
        return ShiftTimePickerDialogFragment.newInstance(mCallbacks.getContentUri(), isStart, mShift.getStart().toLocalDate(), mShift.getStart().toLocalTime(), mShift.getEnd().toLocalTime(), mCallbacks.getColumnNameStartOrDate(), mCallbacks.getColumnNameEnd());
    }

    interface Callbacks extends DetailFragmentBehaviour, Shift {
    }
}
