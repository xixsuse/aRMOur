package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.Interval;

class ShiftData extends AbstractData {

    private final Callbacks mCallbacks;
    private final View.OnClickListener
            mDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 11/06/17
        }
    },
            mStartListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 11/06/17
                }
            },
            mEndListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 11/06/17
                }
            };
    private Interval mShift;

    ShiftData(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        mShift = new Interval(cursor.getLong(mCallbacks.getColumnIndexStart()), cursor.getLong(mCallbacks.getColumnIndexEnd()));
    }

    @Override
    boolean isSwitchType(int position) throws IllegalStateException {
        if (position == mCallbacks.getRowNumberDate() || position == mCallbacks.getRowNumberStart() || position == mCallbacks.getRowNumberEnd()) {
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
        } else return false;
        return true;
    }

    interface Callbacks extends Shift, HasContentUri, ShowsDialog {
    }
}
