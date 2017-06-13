package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.behaviours.DetailFragmentBehaviour;
import com.skepticalone.mecachecker.behaviours.WithDate;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

final class DateData extends AbstractData {
    private final Callbacks mCallbacks;
    private DateTime mStart;

    private final View.OnClickListener mDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallbacks.showDialogFragment(
                    MidnightDatePickerDialogFragment.newInstance(mCallbacks.getContentUri(), mStart.toLocalDate(), mCallbacks.getColumnNameStartOrDate()),
                    LifecycleConstants.DATE_DIALOG
            );
        }
    };

    DateData(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        mStart = new DateTime(cursor.getLong(mCallbacks.getColumnIndexStartOrDate()));
    }

    @Nullable
    @Override
    ViewHolderType getViewHolderType(int position) {
        if (position == mCallbacks.getRowNumberDate()) {
            return ViewHolderType.PLAIN;
        } else {
            return null;
        }
    }

    @Override
    public boolean bindToHolder(Context context, PlainListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberDate()) {
            holder.bind(context, R.drawable.ic_calendar_black_24dp, R.string.date, DateTimeUtils.getFullDateString(mStart), mDateListener);
        } else return false;
        return true;
    }

    interface Callbacks extends DetailFragmentBehaviour, WithDate {
    }

}
