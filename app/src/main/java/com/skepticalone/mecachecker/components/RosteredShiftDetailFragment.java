package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Compliance;
import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.util.AppConstants;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.Duration;
import org.joda.time.Interval;

public class RosteredShiftDetailFragment extends AbstractShiftDetailFragment {

    private static final String MESSAGE_FRAGMENT = "MESSAGE_FRAGMENT";
    private final Adapter mAdapter = new Adapter();

    static RosteredShiftDetailFragment create(long id) {
        RosteredShiftDetailFragment fragment = new RosteredShiftDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(ShiftDetailActivity.SHIFT_ID, id);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    int getLoaderId() {
        return LOADER_ID_ROSTERED_DETAIL;
    }

    @Override
    int getTitle() {
        return R.string.rostered_shift;
    }

    @Override
    RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.rosteredShiftUri(mShiftId), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        Compliance.Wrapper cursor = new Compliance.Wrapper(c);
        if (cursor.moveToFirst()) {
            final Interval rosteredShift = cursor.getRosteredShift();
            final Interval loggedShift = cursor.getLoggedShift();
            mDateView.setText(DateTimeUtils.getFullDateString(rosteredShift.getStart()));
            mDateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createDatePicker(mShiftId, true, rosteredShift, loggedShift).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            mStartTimeView.setText(DateTimeUtils.getTimeString(rosteredShift, true, null));
            mStartTimeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createTimePicker(mShiftId, true, rosteredShift, loggedShift, true, false).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            mEndTimeView.setText(DateTimeUtils.getTimeString(rosteredShift, false, null));
            mEndTimeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createTimePicker(mShiftId, true, rosteredShift, loggedShift, false, false).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            if (loggedShift == null) {
                mLoggedTimesContainer.setVisibility(View.GONE);
                mToggleButtonView.setText(R.string.log_hours);
                mToggleButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues values = new ContentValues();
                        values.put(ShiftContract.RosteredShifts.COLUMN_NAME_LOGGED_START, rosteredShift.getStartMillis());
                        values.put(ShiftContract.RosteredShifts.COLUMN_NAME_LOGGED_END, rosteredShift.getEndMillis());
                        getActivity().getContentResolver().update(ShiftProvider.rosteredShiftUri(mShiftId), values, null, null);
                    }
                });
            } else {
                mLoggedStartTimeView.setText(DateTimeUtils.getTimeString(loggedShift, true, rosteredShift.getStart()));
                mLoggedStartTimeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PickerFragment.createTimePicker(mShiftId, true, rosteredShift, loggedShift, true, true).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                    }
                });
                mLoggedEndTimeView.setText(DateTimeUtils.getTimeString(loggedShift, false, rosteredShift.getStart()));
                mLoggedEndTimeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PickerFragment.createTimePicker(mShiftId, true, rosteredShift, loggedShift, false, true).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                    }
                });
                mLoggedTimesContainer.setVisibility(View.VISIBLE);
                mToggleButtonView.setText(R.string.remove_log);
                mToggleButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues values = new ContentValues();
                        values.putNull(ShiftContract.RosteredShifts.COLUMN_NAME_LOGGED_START);
                        values.putNull(ShiftContract.RosteredShifts.COLUMN_NAME_LOGGED_END);
                        getActivity().getContentResolver().update(ShiftProvider.rosteredShiftUri(mShiftId), values, null, null);
                    }
                });
            }
            mAdapter.swapCursor(cursor);
        }
    }

    private class Adapter extends CursorAdapter<Compliance.Wrapper> {

        @Override
        public TwoLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TwoLineViewHolder holder = new TwoLineViewHolder(parent);
            holder.secondaryIconView.setVisibility(View.GONE);
            return holder;
        }

        @Override
        public int getItemCount() {
            return mCursor != null && mCursor.moveToFirst() ? mCursor.getPreviousWeekend() != null ? 6 : 5 : 0;
        }

        @Override
        public void onBindViewHolder(TwoLineViewHolder holder, int position) {
            if (mCursor != null && mCursor.moveToFirst()) {
                switch (position) {
                    case 0:
                        holder.primaryTextView.setText(R.string.shift_type);
                        Interval shift = mCursor.getRosteredShift();
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
                        holder.secondaryTextView.setText(getString(R.string.shift_type_duration_format, getString(shiftTypeStringId), DateTimeUtils.getPeriodString(shift.toPeriod())));
                        holder.itemView.setOnClickListener(null);
                        break;
                    case 1:
                        holder.primaryTextView.setText(R.string.time_between_shifts);
                        Interval intervalBetweenShifts = mCursor.getIntervalBetweenShifts();
                        if (intervalBetweenShifts == null) {
                            holder.secondaryTextView.setText(R.string.not_applicable);
                            holder.primaryIconView.setImageResource(0);
                        } else {
                            holder.secondaryTextView.setText(DateTimeUtils.getPeriodString(intervalBetweenShifts.toPeriod()));
                            holder.primaryIconView.setImageResource(AppConstants.hasInsufficientIntervalBetweenShifts(intervalBetweenShifts) ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
                        }
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MessageFragment.create(getString(R.string.meca_minimum_hours_between_shifts, AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS)).show(getFragmentManager(), MESSAGE_FRAGMENT);
                            }
                        });
                        break;
                    case 2:
                        holder.primaryTextView.setText(R.string.duration_worked_over_day);
                        Duration durationOverDay = mCursor.getDurationOverDay();
                        holder.secondaryTextView.setText(DateTimeUtils.getPeriodString(durationOverDay.toPeriod()));
                        holder.primaryIconView.setImageResource(AppConstants.exceedsDurationOverDay(durationOverDay) ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MessageFragment.create(getString(R.string.meca_maximum_hours_over_day, AppConstants.MAXIMUM_HOURS_OVER_DAY)).show(getFragmentManager(), MESSAGE_FRAGMENT);
                            }
                        });
                        break;
                    case 3:
                        holder.primaryTextView.setText(R.string.duration_worked_over_week);
                        Duration durationOverWeek = mCursor.getDurationOverWeek();
                        holder.secondaryTextView.setText(DateTimeUtils.getPeriodString(durationOverWeek.toPeriod()));
                        holder.primaryIconView.setImageResource(AppConstants.exceedsDurationOverWeek(durationOverWeek) ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MessageFragment.create(getString(R.string.meca_maximum_hours_over_week, AppConstants.MAXIMUM_HOURS_OVER_WEEK)).show(getFragmentManager(), MESSAGE_FRAGMENT);
                            }
                        });
                        break;
                    case 4:
                        holder.primaryTextView.setText(R.string.duration_worked_over_fortnight);
                        Duration durationOverFortnight = mCursor.getDurationOverFortnight();
                        holder.secondaryTextView.setText(DateTimeUtils.getPeriodString(durationOverFortnight.toPeriod()));
                        holder.primaryIconView.setImageResource(AppConstants.exceedsDurationOverFortnight(durationOverFortnight) ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MessageFragment.create(getString(R.string.meca_maximum_hours_over_fortnight, AppConstants.MAXIMUM_HOURS_OVER_FORTNIGHT)).show(getFragmentManager(), MESSAGE_FRAGMENT);
                            }
                        });
                        break;
                    case 5:
                        holder.primaryTextView.setText(R.string.last_weekend_worked);
                        Interval previousWeekend = mCursor.getPreviousWeekend();
                        //noinspection ConstantConditions
                        holder.secondaryTextView.setText(DateTimeUtils.getDateSpanString(previousWeekend));
//                        holder.secondaryTextView.setText(getString(R.string.period_format, previousWeekend.getStartMillis(), previousWeekend.getEndMillis() - 1));
                        holder.primaryIconView.setImageResource(mCursor.consecutiveWeekendsWorked() ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MessageFragment.create(getString(R.string.meca_consecutive_weekends)).show(getFragmentManager(), MESSAGE_FRAGMENT);
                            }
                        });
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        }

    }

}
