package com.skepticalone.mecachecker.components.summary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;

import java.util.Locale;

public class RosteredSummaryFragment extends SummaryFragment {

    @StringRes
    static final int tabTitle = R.string.rostered;

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
        return new CursorLoader(getActivity(), Provider.rosteredShiftsUri, PROJECTION, null, null, null);
    }

    @Override
    int getRowCount() {
        return 3;
    }

    @Override
    void bindViewHolderToCursor(SummaryViewHolder holder, int row, @NonNull Cursor cursor) {
        switch (row) {
            case 0:
                onBindShiftNumber(holder, cursor);
                break;
            case 1:
                onBindShiftsDuration(holder, cursor, false);
                break;
            case 2:
                onBindShiftsDuration(holder, cursor, true);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void onBindShiftNumber(SummaryViewHolder holder, @NonNull Cursor cursor) {
        int logged = 0, not_logged = 0;
        if (cursor.moveToFirst()) {
            do {
                if (cursor.isNull(COLUMN_INDEX_LOGGED_START) || cursor.isNull(COLUMN_INDEX_LOGGED_END)) {
                    not_logged++;
                } else {
                    logged++;
                }
            } while (cursor.moveToNext());
        }
        int total = logged + not_logged;
        holder.titleView.setText(R.string.rostered_shifts);
        holder.totalView.setText(String.format(Locale.US, "%d", total));
        holder.unclaimedView.setText(String.format(Locale.US, "Not logged: %d (%d%%)", not_logged, 100 * not_logged / total));
        holder.claimedView.setText(String.format(Locale.US, "Logged: %d (%d%%)", logged, 100 * logged / total));
        holder.paidView.setVisibility(View.GONE);
        holder.subtotalsView.setVisibility(View.VISIBLE);
        holder.pieView.set(not_logged, logged);
        holder.pieView.setVisibility(View.VISIBLE);
    }

    private void onBindShiftsDuration(SummaryViewHolder holder, @NonNull Cursor cursor, boolean logged) {
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
        holder.titleView.setText(logged ? R.string.logged_hours : R.string.rostered_hours);
        holder.totalView.setText(String.format(Locale.US, "%.1f", (float) duration.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR));
        holder.subtotalsView.setVisibility(View.GONE);
        holder.pieView.setVisibility(View.GONE);
    }

}
