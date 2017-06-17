package com.skepticalone.mecachecker.composition;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.behaviours.DetailFragmentBehaviour;
import com.skepticalone.mecachecker.behaviours.Shift;
import com.skepticalone.mecachecker.components.ListItemViewHolder;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.dialog.ShiftDatePickerDialogFragment;
import com.skepticalone.mecachecker.dialog.ShiftTimePickerDialogFragment;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.LifecycleConstants;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class ShiftData extends AbstractData {

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

    public ShiftData(Callbacks callbacks) {
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
    @Override
    public boolean bindToHolder(Context context, ListItemViewHolder holder, int position) {
        int primaryIconRes;
        int keyRes;
        String value;
        View.OnClickListener listener;
        if (position == mCallbacks.getRowNumberDate()) {
            primaryIconRes = R.drawable.ic_calendar_black_24dp;
            keyRes = R.string.date;
            value = DateTimeUtils.getFullDateString(mShift.getStart());
            listener = mDateListener;
        } else if (position == mCallbacks.getRowNumberStart()) {
            primaryIconRes = R.drawable.ic_play_black_24dp;
            keyRes = R.string.start;
            value = DateTimeUtils.getTimeString(mShift.getStart());
            listener = mStartListener;
        } else if (position == mCallbacks.getRowNumberEnd()) {
            primaryIconRes = R.drawable.ic_stop_black_24dp;
            keyRes = R.string.end;
            value = DateTimeUtils.getTimeString(mShift.getEnd(), getShiftDate());
            listener = mEndListener;
        } else if (position == mCallbacks.getRowNumberShiftType()) {
            primaryIconRes = ShiftUtil.getShiftTypeIcon(mShiftType);
            keyRes = R.string.shift_type;
            value = DateTimeUtils.getShiftTypeWithDurationString(context.getString(ShiftUtil.getShiftTypeTitle(mShiftType)), mShift);
            listener = null;
        } else return false;
        holder.bindPlain(primaryIconRes, context.getString(keyRes), value, null, 0);
        holder.itemView.setOnClickListener(listener);
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

    public interface Callbacks extends DetailFragmentBehaviour, Shift {
    }
}
