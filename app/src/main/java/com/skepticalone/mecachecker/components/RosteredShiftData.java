package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.behaviours.LoggableShift;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

class RosteredShiftData extends ShiftData implements SwitchListItemViewHolder.Callbacks {

    private final Callbacks mCallbacks;
    @Nullable
    private Interval mLoggedShift;
    @NonNull
    private final View.OnClickListener
            mLoggedStartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallbacks.showDialogFragment(getLoggedShiftTimePickerDialogFragment(true), LifecycleConstants.DATE_DIALOG);
        }
    },
            mLoggedEndListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.showDialogFragment(getLoggedShiftTimePickerDialogFragment(false), LifecycleConstants.DATE_DIALOG);
                }
            };

    RosteredShiftData(Callbacks callbacks) {
        super(callbacks);
        mCallbacks = callbacks;
    }

    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        super.readFromPositionedCursor(cursor);
        mLoggedShift = (cursor.isNull(mCallbacks.getColumnIndexLoggedStart()) || cursor.isNull(mCallbacks.getColumnIndexLoggedEnd())) ? null : new Interval(cursor.getLong(mCallbacks.getColumnIndexLoggedStart()), cursor.getLong(mCallbacks.getColumnIndexLoggedEnd()));
    }

    @Nullable
    @Override
    ViewHolderType getViewHolderType(int position) {
        if (position == mCallbacks.getRowNumberLoggedStart() || position == mCallbacks.getRowNumberLoggedEnd()) {
            return ViewHolderType.PLAIN;
        } else if (position == mCallbacks.getRowNumberLoggedSwitch()) {
            return ViewHolderType.SWITCH;
        } else {
            return super.getViewHolderType(position);
        }
    }

    @Override
    public boolean bindToHolder(Context context, PlainListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberLoggedStart()) {
            assert mLoggedShift != null;
            holder.rootBind(context, R.drawable.ic_clipboard_play_black_24dp, R.string.logged_start, DateTimeUtils.getTimeString(mLoggedShift.getStart(), getShiftDate()), mLoggedStartListener);
        } else if (position == mCallbacks.getRowNumberLoggedEnd()) {
            assert mLoggedShift != null;
            holder.rootBind(context, R.drawable.ic_clipboard_stop_black_24dp, R.string.logged_end, DateTimeUtils.getTimeString(mLoggedShift.getEnd(), getShiftDate()), mLoggedEndListener);
        } else return super.bindToHolder(context, holder, position);
        return true;
    }

    @Override
    boolean bindToHolder(Context context, SwitchListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberLoggedSwitch()) {
            holder.bindLogged(context, mLoggedShift != null);
        } else return super.bindToHolder(context, holder, position);
        return true;
    }

    @Override
    public void onCheckedChanged(SwitchListItemViewHolder.SwitchType switchType, boolean isChecked) {
        if (switchType == SwitchListItemViewHolder.SwitchType.LOGGED) {
            ContentValues contentValues = new ContentValues();
            if (isChecked) {
                contentValues.put(mCallbacks.getColumnNameLoggedStart(), getShiftStartMillis());
                contentValues.put(mCallbacks.getColumnNameLoggedEnd(), getShiftEndMillis());
            } else {
                contentValues.putNull(mCallbacks.getColumnNameLoggedStart());
                contentValues.putNull(mCallbacks.getColumnNameLoggedEnd());
            }
            mCallbacks.update(contentValues);
        } else {
            throw new IllegalStateException();
        }
    }

    boolean isLogged() {
        return mLoggedShift != null;
    }

    @NonNull
    @Override
    ShiftDatePickerDialogFragment getShiftDatePickerDialogFragment(@NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end) {
        return RosteredShiftDatePickerDialogFragment.newInstance(
                mCallbacks.getUpdateContentUri(),
                date,
                start,
                end,
                mLoggedShift == null ? null : mLoggedShift.getStart().toLocalTime(),
                mLoggedShift == null ? null : mLoggedShift.getEnd().toLocalTime(),
                mCallbacks.getColumnNameStartOrDate(),
                mCallbacks.getColumnNameEnd(),
                mCallbacks.getColumnNameLoggedStart(),
                mCallbacks.getColumnNameLoggedEnd()
        );
    }

    @NonNull
    private ShiftTimePickerDialogFragment getLoggedShiftTimePickerDialogFragment(boolean isStart) {
        assert mLoggedShift != null;
        return ShiftTimePickerDialogFragment.newInstance(mCallbacks.getUpdateContentUri(), isStart, getShiftDate(), mLoggedShift.getStart().toLocalTime(), mLoggedShift.getEnd().toLocalTime(), mCallbacks.getColumnNameLoggedStart(), mCallbacks.getColumnNameLoggedEnd());
    }

    interface Callbacks extends ShiftData.Callbacks, LoggableShift {
    }
}
