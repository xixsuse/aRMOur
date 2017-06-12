package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.Interval;

class LoggedShiftData extends ShiftData {

    private Logged mCallbacks;
    @Nullable
    private Interval mLoggedShift;

    LoggedShiftData(Callbacks callbacks) {
        super(callbacks);
        mCallbacks = callbacks;
    }

    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        super.readFromPositionedCursor(cursor);
        mLoggedShift = (cursor.isNull(mCallbacks.getColumnIndexLoggedStart()) || cursor.isNull(mCallbacks.getColumnIndexLoggedEnd())) ? null : new Interval(cursor.getLong(mCallbacks.getColumnIndexLoggedStart()), cursor.getLong(mCallbacks.getColumnIndexLoggedEnd()));
    }

    @Override
    boolean isSwitchType(int position) throws IllegalStateException {
        return !(position == mCallbacks.getRowNumberLoggedStart() || position == mCallbacks.getRowNumberLoggedEnd()) && super.isSwitchType(position);
    }

    @Override
    public boolean bindToHolder(Context context, PlainListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberLoggedStart()) {
            holder.bind(context, R.drawable.ic_play_arrow_grey_24dp, R.string.logged_start, mLoggedShift == null ? context.getString(R.string.not_applicable) : DateTimeUtils.getFullDateString(mLoggedShift.getStart()), null);
        } else if (position == mCallbacks.getRowNumberLoggedEnd()) {
            holder.bind(context, R.drawable.ic_stop_grey_24dp, R.string.logged_end, mLoggedShift == null ? context.getString(R.string.not_applicable) : DateTimeUtils.getFullDateString(mLoggedShift.getEnd()), null);
        } else return super.bindToHolder(context, holder, position);
        return true;
    }

    interface Callbacks extends ShiftData.Callbacks, Logged {
    }
}
