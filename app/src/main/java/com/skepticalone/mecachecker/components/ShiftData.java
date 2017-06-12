package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftTypeUtil;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

class ShiftData extends AbstractData {

    private final Callbacks mCallbacks;
    private Interval mShift;
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
                    mCallbacks.showDialogFragment(getShiftTimePickerDialogFragment(true, mShift.getStart().toLocalDate(), mShift.getStart().toLocalTime(), mShift.getEnd().toLocalTime()), LifecycleConstants.DATE_DIALOG);
                }
            },
            mEndListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.showDialogFragment(getShiftTimePickerDialogFragment(false, mShift.getStart().toLocalDate(), mShift.getStart().toLocalTime(), mShift.getEnd().toLocalTime()), LifecycleConstants.DATE_DIALOG);
                }
            };
    private ShiftType mShiftType;

    ShiftData(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        mShift = new Interval(cursor.getLong(mCallbacks.getColumnIndexStart()), cursor.getLong(mCallbacks.getColumnIndexEnd()));
        mShiftType = mCallbacks.getShiftType(mShift);

    }

    @Override
    boolean isSwitchType(int position) throws IllegalStateException {
        if (
                position == mCallbacks.getRowNumberDate() ||
                        position == mCallbacks.getRowNumberStart() ||
                        position == mCallbacks.getRowNumberEnd() ||
                        position == mCallbacks.getRowNumberShiftType()
                ) {
            return false;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean bindToHolder(Context context, PlainListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberDate()) {
            holder.bind(context, R.drawable.ic_calendar_black_24dp, R.string.date, DateTimeUtils.getFullDateString(mShift.getStart()), mDateListener);
        } else if (position == mCallbacks.getRowNumberStart()) {
            holder.bind(context, R.drawable.ic_play_arrow_black_24dp, R.string.start, DateTimeUtils.getTimeString(mShift, true, null), mStartListener);
        } else if (position == mCallbacks.getRowNumberEnd()) {
            holder.bind(context, R.drawable.ic_stop_black_24dp, R.string.end, DateTimeUtils.getTimeString(mShift, false, null), mEndListener);
        } else if (position == mCallbacks.getRowNumberShiftType()) {
            holder.bind(context, ShiftTypeUtil.getShiftTypeIcon(mShiftType), R.string.shift_type, context.getString(ShiftTypeUtil.getShiftTypeTitle(mShiftType)), null);
        } else return false;
        return true;
    }

    private ShiftDatePickerDialogFragment getShiftDatePickerDialogFragment(@NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end) {
        return ShiftDatePickerDialogFragment.newInstance(mCallbacks.getContentUri(), date, start, end, mCallbacks.getColumnNameStart(), mCallbacks.getColumnNameEnd());
    }

    private ShiftTimePickerDialogFragment getShiftTimePickerDialogFragment(boolean isStart, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end) {
        return ShiftTimePickerDialogFragment.newInstance(mCallbacks.getContentUri(), isStart, date, start, end, mCallbacks.getColumnNameStart(), mCallbacks.getColumnNameEnd());
    }

    interface Callbacks extends Shift, HasContentUri, ShowsDialog {
    }
}
