package com.skepticalone.mecachecker.composition;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.behaviours.DetailFragmentBehaviour;
import com.skepticalone.mecachecker.behaviours.WithDate;
import com.skepticalone.mecachecker.components.ListItemViewHolder;
import com.skepticalone.mecachecker.dialog.MidnightDatePickerDialogFragment;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.LifecycleConstants;

import org.joda.time.DateTime;

public final class DateData extends AbstractData {
    private final Callbacks mCallbacks;
    private DateTime mStart;
    @NonNull
    private final View.OnClickListener mDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallbacks.showDialogFragment(
                    MidnightDatePickerDialogFragment.newInstance(mCallbacks.getUpdateContentUri(), mStart.toLocalDate(), mCallbacks.getColumnNameStartOrDate()),
                    LifecycleConstants.DATE_DIALOG
            );
        }
    };

    public DateData(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        mStart = new DateTime(cursor.getLong(mCallbacks.getColumnIndexStartOrDate()));
    }

    @Override
    public boolean bindToHolder(Context context, ListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberDate()) {
            holder.bindPlain(R.drawable.ic_calendar_black_24dp, context.getString(R.string.date), DateTimeUtils.getFullDateString(mStart), null, 0);
            holder.itemView.setOnClickListener(mDateListener);
        } else return false;
        return true;
    }

    public interface Callbacks extends DetailFragmentBehaviour, WithDate {
    }

}
