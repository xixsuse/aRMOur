package com.skepticalone.mecachecker.composition;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.behaviours.LoggableShift;
import com.skepticalone.mecachecker.components.ListItemViewHolder;
import com.skepticalone.mecachecker.dialog.RosteredShiftDatePickerDialogFragment;
import com.skepticalone.mecachecker.dialog.ShiftDatePickerDialogFragment;
import com.skepticalone.mecachecker.dialog.ShiftTimePickerDialogFragment;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.LifecycleConstants;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class RosteredShiftData extends ShiftData implements ListItemViewHolder.SwitchCallbacks {

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

    public RosteredShiftData(Callbacks callbacks) {
        super(callbacks);
        mCallbacks = callbacks;
    }

    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        super.readFromPositionedCursor(cursor);
        mLoggedShift = (cursor.isNull(mCallbacks.getColumnIndexLoggedStart()) || cursor.isNull(mCallbacks.getColumnIndexLoggedEnd())) ? null : new Interval(cursor.getLong(mCallbacks.getColumnIndexLoggedStart()), cursor.getLong(mCallbacks.getColumnIndexLoggedEnd()));
    }

    @Override
    public boolean bindToHolder(Context context, ListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberLoggedStart()) {
            assert mLoggedShift != null;
            holder.bindPlain(
                    R.drawable.ic_clipboard_play_black_24dp,
                    context.getString(R.string.logged_start),
                    DateTimeUtils.getTimeString(mLoggedShift.getStart(), getShiftDate()),
                    null, 0
            );
            holder.itemView.setOnClickListener(mLoggedStartListener);
        } else if (position == mCallbacks.getRowNumberLoggedEnd()) {
            assert mLoggedShift != null;
            holder.bindPlain(
                    R.drawable.ic_clipboard_stop_black_24dp,
                    context.getString(R.string.logged_end),
                    DateTimeUtils.getTimeString(mLoggedShift.getEnd(), getShiftDate()),
                    null, 0
            );
            holder.itemView.setOnClickListener(mLoggedEndListener);
        } else if (position == mCallbacks.getRowNumberLoggedSwitch()) {
            holder.bindLogged(context, mLoggedShift != null, this);
        } else return super.bindToHolder(context, holder, position);
        return true;
    }

    @Override
    public void onCheckedChanged(ListItemViewHolder.SwitchType switchType, boolean isChecked) {
        if (switchType == ListItemViewHolder.SwitchType.LOGGED) {
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

    public boolean isLogged() {
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

    public interface Callbacks extends ShiftData.Callbacks, LoggableShift {
    }
}
