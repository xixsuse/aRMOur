package com.skepticalone.mecachecker.components.summary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;

import java.util.Locale;

public class RosteredSummaryFragment extends SummaryFragment {

    @StringRes
    static final int tabTitle = R.string.rostered_shifts;

    private static final String[] PROJECTION = {
            Contract.RosteredShifts.COLUMN_NAME_ROSTERED_START,
            Contract.RosteredShifts.COLUMN_NAME_ROSTERED_END,
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_START,
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_END
    };
    private static final int
            COLUMN_INDEX_ROSTERED_START = 0,
            COLUMN_INDEX_ROSTERED_END = 1,
            COLUMN_INDEX_LOGGED_START = 2,
            COLUMN_INDEX_LOGGED_END = 3;

    @Override
    int getLoaderId() {
        return SummaryActivity.LOADER_ID_ROSTERED;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Provider.rosteredShiftsUri, PROJECTION, null, null, Contract.RosteredShifts.COLUMN_NAME_ROSTERED_START);
    }

    @Override
    int getRowCount() {
        return 4;
    }

    @Override
    void bindViewHolderToCursor(SummaryViewHolder holder, int row, @NonNull Cursor cursor) {
        switch (row) {
            case 0:
                onBindShiftNumber(holder, cursor, false);
                break;
            case 1:
                onBindShiftNumber(holder, cursor, true);
                break;
            case 2:
                onBindShiftsDuration(holder, cursor, false);
                break;
            case 3:
                onBindShiftsDuration(holder, cursor, true);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void onBindShiftNumber(SummaryViewHolder holder, @NonNull Cursor cursor, boolean logged) {
        holder.titleView.setText(logged ? R.string.logged_shifts : R.string.rostered_shifts);
        int count;
        if (logged) {
            count = 0;
            if (cursor.moveToFirst()) {
                do {
                    if (!cursor.isNull(COLUMN_INDEX_LOGGED_START) && !cursor.isNull(COLUMN_INDEX_LOGGED_END)) {
                        count++;
                    }
                } while (cursor.moveToNext());
            }
        } else {
            count = cursor.getCount();
        }
        holder.totalView.setText(String.format(Locale.US, "%d", count));
    }

    private void onBindShiftsDuration(SummaryViewHolder holder, @NonNull Cursor cursor, boolean logged) {
        holder.titleView.setText(logged ? R.string.logged_hours : R.string.rostered_hours);
        int
                columnIndexStart = logged ? COLUMN_INDEX_LOGGED_START : COLUMN_INDEX_ROSTERED_START,
                columnIndexEnd = logged ? COLUMN_INDEX_LOGGED_END : COLUMN_INDEX_ROSTERED_END;
        Duration duration = Duration.ZERO;
        if (cursor.moveToFirst()) {
            do {
                if (!logged || (!cursor.isNull(columnIndexStart) && !cursor.isNull(columnIndexEnd))) {
                    duration = duration.plus(new Duration(cursor.getLong(columnIndexStart), cursor.getLong(columnIndexEnd)));
                }
            } while (cursor.moveToNext());
        }
        holder.totalView.setText(String.format(Locale.US, "%.1f", (float) duration.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR));
    }

}