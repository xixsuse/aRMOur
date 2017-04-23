package com.skepticalone.mecachecker.components;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;

import org.joda.time.Interval;

public class AdditionalShiftDetailFragment extends AbstractShiftDetailFragment {

    private static final String[] PROJECTION = new String[]{
            ShiftContract.AdditionalShifts._ID,
            ShiftContract.AdditionalShifts.COLUMN_NAME_START,
            ShiftContract.AdditionalShifts.COLUMN_NAME_END,
            ShiftContract.AdditionalShifts.COLUMN_NAME_RATE,
            ShiftContract.AdditionalShifts.COLUMN_NAME_CLAIMED,
            ShiftContract.AdditionalShifts.COLUMN_NAME_PAID,
            ShiftContract.AdditionalShifts.COLUMN_NAME_COMMENT,
    };
    private final static int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_START = 1,
            COLUMN_INDEX_END = 2,
            COLUMN_INDEX_RATE = 3,
            COLUMN_INDEX_CLAIMED = 4,
            COLUMN_INDEX_PAID = 5,
            COLUMN_INDEX_COMMENT = 6;
    private final Adapter mAdapter = new Adapter();

    static AdditionalShiftDetailFragment create(long id) {
        AdditionalShiftDetailFragment fragment = new AdditionalShiftDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(ShiftDetailActivity.SHIFT_ID, id);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoggedTimesContainer.setVisibility(View.GONE);
        mToggleLoggedTimesView.setVisibility(View.GONE);
    }

    @Override
    RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    int getLoaderId() {
        return LOADER_ID_ADDITIONAL_DETAIL;
    }

    @Override
    int getTitle() {
        return R.string.additional_shift;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.additionalShiftUri(mShiftId), PROJECTION, null, null, ShiftContract.AdditionalShifts.COLUMN_NAME_START);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            Interval currentShift = new Interval(cursor.getLong(COLUMN_INDEX_START), cursor.getLong(COLUMN_INDEX_END));
            mDateView.setText(getString(R.string.day_date_format, currentShift.getStartMillis()));
            mRosteredStartTimeView.setText(getString(R.string.time_format, currentShift.getStartMillis()));
            long dateAtMidnight = currentShift.getStart().withTimeAtStartOfDay().getMillis();
            mRosteredEndTimeView.setText(getString(currentShift.getEnd().withTimeAtStartOfDay().isEqual(dateAtMidnight) ? R.string.time_format : R.string.time_format_with_day, currentShift.getEndMillis()));
        }
        mAdapter.swapCursor(cursor);

    }

    private class Adapter extends CursorAdapter<Cursor> {

        @Override
        public void onBindViewHolder(TwoLineViewHolder holder, int position) {
            if (mCursor != null && mCursor.moveToFirst()) {
                switch (position) {
                    case 0:
                        holder.primaryIconView.setImageResource(R.drawable.ic_dollar_black_24dp);
                        holder.primaryTextView.setText("Rate");
                        holder.secondaryTextView.setText("$" + mCursor.getInt(COLUMN_INDEX_RATE));
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }
}
