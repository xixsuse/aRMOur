package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

final class DateData extends AbstractData {
    private final Callbacks mCallbacks;
    private DateTime mStart;

    private final View.OnClickListener mDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallbacks.showDialogFragment(
                    MidnightDatePickerDialogFragment.newInstance(mStart.toLocalDate(), mCallbacks.getContentUri(), mCallbacks.getColumnNameStart()),
                    LifecycleConstants.DATE_DIALOG
            );
        }
    };

    DateData(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        mStart = new DateTime(cursor.getLong(mCallbacks.getColumnIndexStart()));
    }

    @Override
    boolean isSwitchType(int position) throws IllegalStateException {
        if (position == mCallbacks.getRowNumberStart()) {
            return false;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean bindToHolder(Context context, PlainListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberStart()) {
            holder.bind(context, R.drawable.ic_calendar_black_24dp, R.string.date, DateTimeUtils.getFullDateString(mStart), mDateListener);
        } else return false;
        return true;
    }

    interface Callbacks extends Starts, HasContentUri, ShowsDialog {
    }

}
