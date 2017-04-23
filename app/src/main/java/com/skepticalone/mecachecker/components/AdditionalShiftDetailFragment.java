package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
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
import com.skepticalone.mecachecker.data.ShiftType;

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
            final Interval currentShift = new Interval(cursor.getLong(COLUMN_INDEX_START), cursor.getLong(COLUMN_INDEX_END));
            mDateView.setText(getString(R.string.day_date_format, currentShift.getStartMillis()));
            mDateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createDatePicker(mShiftId, false, currentShift, null).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            mRosteredStartTimeView.setText(getString(R.string.time_format, currentShift.getStartMillis()));
            mRosteredStartTimeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createTimePicker(mShiftId, false, currentShift, null, true, false).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            long dateAtMidnight = currentShift.getStart().withTimeAtStartOfDay().getMillis();
            mRosteredEndTimeView.setText(getString(currentShift.getEnd().withTimeAtStartOfDay().isEqual(dateAtMidnight) ? R.string.time_format : R.string.time_format_with_day, currentShift.getEndMillis()));
            mRosteredEndTimeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createTimePicker(mShiftId, false, currentShift, null, false, false).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            if (cursor.isNull(COLUMN_INDEX_PAID)) {
                final boolean isClaimed = !cursor.isNull(COLUMN_INDEX_CLAIMED);
                if (isClaimed) {
                    mToggleButtonView.setText("Mark as Paid");
                } else {
                    mToggleButtonView.setText("Mark as Claimed!");
                }
                mToggleButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues values = new ContentValues();
                        values.put(isClaimed ? ShiftContract.AdditionalShifts.COLUMN_NAME_PAID : ShiftContract.AdditionalShifts.COLUMN_NAME_CLAIMED, System.currentTimeMillis());
                        getActivity().getContentResolver().update(ShiftProvider.additionalShiftUri(mShiftId), values, null, null);
                    }
                });
                mToggleButtonView.setVisibility(View.VISIBLE);
            } else {
                mToggleButtonView.setVisibility(View.GONE);
            }
        }
        mAdapter.swapCursor(cursor);
    }

    private class Adapter extends CursorAdapter<Cursor> {

        @Override
        public void onBindViewHolder(TwoLineViewHolder holder, int position) {
            if (mCursor != null && mCursor.moveToFirst()) {
                holder.itemView.setOnClickListener(null);
                switch (position) {
                    case 0:
                        holder.primaryTextView.setText(R.string.shift_type);
                        Interval shift = new Interval(mCursor.getLong(COLUMN_INDEX_START), mCursor.getLong(COLUMN_INDEX_END));
                        ShiftType shiftType = getShiftType(shift.getStart().getMinuteOfDay(), shift.getEnd().getMinuteOfDay());
                        int shiftTypeStringId, shiftTypeDrawableId;
                        if (shiftType == ShiftType.NORMAL_DAY) {
                            shiftTypeDrawableId = R.drawable.ic_normal_day_black_24dp;
                            shiftTypeStringId = R.string.normal_day;
                        } else if (shiftType == ShiftType.LONG_DAY) {
                            shiftTypeDrawableId = R.drawable.ic_long_day_black_24dp;
                            shiftTypeStringId = R.string.long_day;
                        } else if (shiftType == ShiftType.NIGHT_SHIFT) {
                            shiftTypeDrawableId = R.drawable.ic_night_shift_black_24dp;
                            shiftTypeStringId = R.string.night_shift;
                        } else {
                            shiftTypeDrawableId = R.drawable.ic_custom_shift_black_24dp;
                            shiftTypeStringId = R.string.custom;
                        }
                        holder.primaryIconView.setImageResource(shiftTypeDrawableId);
                        holder.secondaryTextView.setText(getString(R.string.shift_type_duration_format, getString(shiftTypeStringId), periodFormatter.print(shift.toPeriod())));
                        break;
                    case 1:
                        holder.primaryIconView.setImageResource(R.drawable.ic_dollar_black_24dp);
                        holder.primaryTextView.setText(R.string.hourly_rate);
                        final int hourlyRate = mCursor.getInt(COLUMN_INDEX_RATE);
                        holder.secondaryTextView.setText(getString(R.string.hourly_rate_format, hourlyRate / 100f));
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HourlyRateEditFragment.create(mShiftId, hourlyRate).show(getFragmentManager(), ShiftDetailActivity.HOURLY_RATE_FRAGMENT);
                            }
                        });
                        break;
                    case 2:
                        holder.primaryIconView.setImageResource(R.drawable.ic_check_box_half_black_24dp);
                        holder.primaryTextView.setText(R.string.claimed);
                        holder.secondaryTextView.setText(mCursor.isNull(COLUMN_INDEX_CLAIMED) ? getString(R.string.not_applicable) : getString(R.string.datetime_format, mCursor.getLong(COLUMN_INDEX_CLAIMED)));
                        break;
                    case 3:
                        holder.primaryIconView.setImageResource(R.drawable.ic_check_box_full_black_24dp);
                        holder.primaryTextView.setText(R.string.paid);
                        holder.secondaryTextView.setText(getString(R.string.datetime_format, mCursor.getLong(COLUMN_INDEX_PAID)));
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.isNull(COLUMN_INDEX_PAID) ? mCursor.isNull(COLUMN_INDEX_CLAIMED) ? 2 : 3 : 4;
        }
    }
}
