package com.skepticalone.mecachecker.components;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ComplianceCursor;
import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.util.AppConstants;

import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class ShiftDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String DATE_PICKER_FRAGMENT = "DATE_PICKER_FRAGMENT";
    private static final String TIME_PICKER_FRAGMENT = "TIME_PICKER_FRAGMENT";
    private static final int LOADER_DETAIL_ID = 1;
    private final static PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
            .appendYears()
            .appendSuffix(" year", " years")
            .appendSeparator(", ")
            .appendMonths()
            .appendSuffix(" month", " months")
            .appendSeparator(", ")
            .appendWeeks()
            .appendSuffix(" week", " weeks")
            .appendSeparator(", ")
            .appendDays()
            .appendSuffix(" day", " days")
            .appendSeparator(", ")
            .appendHours()
            .appendSuffix(" hour", " hours")
            .appendSeparator(", ")
            .appendMinutes()
            .appendSuffix(" minute", " minutes")
            .toFormatter();
    private long mShiftId;
    private TextView
            mDateView,
            mStartTimeView,
            mEndTimeView;
    //    ,
//            mShiftTypeView,
//            mTimeBetweenShiftsView,
//            mDurationWorkedOverDayView,
//            mDurationWorkedOverWeekView,
//            mDurationWorkedOverFortnightView,
//            mCurrentWeekendView,
//            mLastWeekendWorkedLabelView,
//            mLastWeekendWorkedView;
    private RecyclerView mRecyclerView;

    static ShiftDetailFragment create(long id) {
        ShiftDetailFragment fragment = new ShiftDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(ShiftDetailActivity.SHIFT_ID, id);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShiftId = getArguments().getLong(ShiftDetailActivity.SHIFT_ID, ShiftDetailActivity.NO_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.shift_detail_fragment, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler);
        mDateView = (TextView) layout.findViewById(R.id.date);
        mStartTimeView = (TextView) layout.findViewById(R.id.start_time);
        mEndTimeView = (TextView) layout.findViewById(R.id.end_time);
//        mShiftTypeView = (TextView) layout.findViewById(R.id.shift_type);
//        mTimeBetweenShiftsView = (TextView) layout.findViewById(R.id.time_between_shifts);
//        mDurationWorkedOverDayView = (TextView) layout.findViewById(R.id.duration_worked_over_day);
//        mDurationWorkedOverWeekView = (TextView) layout.findViewById(R.id.duration_worked_over_week);
//        mDurationWorkedOverFortnightView = (TextView) layout.findViewById(R.id.duration_worked_over_fortnight);
//        mCurrentWeekendView = (TextView) layout.findViewById(R.id.current_weekend);
//        mLastWeekendWorkedLabelView = (TextView) layout.findViewById(R.id.last_weekend_worked_label);
//        mLastWeekendWorkedView = (TextView) layout.findViewById(R.id.last_weekend_worked);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_DETAIL_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.shiftWithComplianceUri(mShiftId), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        ComplianceCursor cursor = new ComplianceCursor(c);
        if (cursor.moveToFirst()) {
            final Interval shift = cursor.getShift();
            mDateView.setText(getString(R.string.day_date_format, shift.getStartMillis()));
            mDateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerFragment.create(mShiftId, shift).show(getFragmentManager(), DATE_PICKER_FRAGMENT);
                }
            });
            mStartTimeView.setText(getString(R.string.time_format, shift.getStartMillis()));
            mStartTimeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerFragment.create(mShiftId, shift, true).show(getFragmentManager(), TIME_PICKER_FRAGMENT);
                }
            });
            mEndTimeView.setText(getString(shift.getStart().getDayOfMonth() == shift.getEnd().getDayOfMonth() ? R.string.time_format : R.string.time_format_with_day, shift.getEndMillis()));
            mEndTimeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerFragment.create(mShiftId, shift, false).show(getFragmentManager(), TIME_PICKER_FRAGMENT);
                }
            });
            mRecyclerView.setAdapter(new ShiftDetailAdapter(cursor));
//            int shiftTypeDrawableId, shiftTypeStringId;
//            switch (cursor.getShiftCategory()) {
//                case ComplianceCursor.SHIFT_TYPE_NORMAL_DAY:
//                    shiftTypeDrawableId = R.drawable.ic_normal_day_black_24dp;
//                    shiftTypeStringId = R.string.normal_day;
//                    break;
//                case ComplianceCursor.SHIFT_TYPE_LONG_DAY:
//                    shiftTypeDrawableId = R.drawable.ic_long_day_black_24dp;
//                    shiftTypeStringId = R.string.long_day;
//                    break;
//                case ComplianceCursor.SHIFT_TYPE_NIGHT_SHIFT:
//                    shiftTypeDrawableId = R.drawable.ic_night_shift_black_24dp;
//                    shiftTypeStringId = R.string.night_shift;
//                    break;
//                default:
//                    shiftTypeDrawableId = R.drawable.ic_custom_shift_black_24dp;
//                    shiftTypeStringId = R.string.custom;
//                    break;
//            }
//            mShiftTypeView.setText(shiftTypeStringId);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mShiftTypeView, 0, 0, shiftTypeDrawableId, 0);
//            Duration duration = cursor.getTimeBetweenShifts();
//            if (duration == null) {
//                mTimeBetweenShiftsView.setText(R.string.not_applicable);
//                mTimeBetweenShiftsView.setTextColor(mTextColor);
//                mTimeBetweenShiftsView.setCompoundDrawables(null, null, null, null);
//            } else {
//                mTimeBetweenShiftsView.setText(periodFormatter.print(duration.toPeriodTo(shift.getStart())));
//                error = AppConstants.hasInsufficientTimeBetweenShifts(duration);
//                mTimeBetweenShiftsView.setTextColor(error ? mErrorColor : mTextColor);
//                TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mTimeBetweenShiftsView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
//            }
//            duration = cursor.getDurationOverDay();
//            mDurationWorkedOverDayView.setText(periodFormatter.print(duration.toPeriod()));
//            error = AppConstants.exceedsDurationOverDay(duration);
//            mDurationWorkedOverDayView.setTextColor(error ? mErrorColor : mTextColor);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverDayView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
//            duration = cursor.getDurationOverWeek();
//            mDurationWorkedOverWeekView.setText(periodFormatter.print(duration.toPeriod()));
//            error = AppConstants.exceedsDurationOverWeek(duration);
//            mDurationWorkedOverWeekView.setTextColor(error ? mErrorColor : mTextColor);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverWeekView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
//            duration = cursor.getDurationOverFortnight();
//            mDurationWorkedOverFortnightView.setText(periodFormatter.print(duration.toPeriod()));
//            error = AppConstants.exceedsDurationOverFortnight(duration);
//            mDurationWorkedOverFortnightView.setTextColor(error ? mErrorColor : mTextColor);
//            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mDurationWorkedOverFortnightView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
//            Interval currentWeekend = cursor.getCurrentWeekend();
//            if (currentWeekend != null) {
//                mCurrentWeekendView.setText(getString(R.string.period_format, currentWeekend.getStartMillis(), currentWeekend.getEndMillis() - 1));
//                mLastWeekendWorkedLabelView.setVisibility(View.VISIBLE);
//                Interval previousWeekend = cursor.getPreviousWeekend();
//                if (previousWeekend != null) {
//                    mLastWeekendWorkedView.setText(getString(R.string.period_format, previousWeekend.getStartMillis(), previousWeekend.getEndMillis() - 1));
//                    error = cursor.consecutiveWeekendsWorked();
//                    mLastWeekendWorkedView.setTextColor(error ? mErrorColor : mTextColor);
//                    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mLastWeekendWorkedView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
//                } else {
//                    mLastWeekendWorkedView.setText(R.string.not_applicable);
//                    mLastWeekendWorkedView.setTextColor(mTextColor);
//                    mLastWeekendWorkedView.setCompoundDrawables(null, null, null, null);
//                }
//                mLastWeekendWorkedView.setVisibility(View.VISIBLE);
//            } else {
//                mCurrentWeekendView.setText(R.string.not_applicable);
//                mLastWeekendWorkedLabelView.setVisibility(View.GONE);
//                mLastWeekendWorkedView.setVisibility(View.GONE);
//            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    class ShiftDetailAdapter extends AbstractTwoLineAdapter {

        private final ComplianceCursor mCursor;

        ShiftDetailAdapter(ComplianceCursor c) {
            super();
            mCursor = c;
            mCursor.moveToFirst();
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            Duration duration;
            switch (position) {
                case 0:
                    holder.primaryTextView.setText(R.string.shift_type);
                    int shiftTypeDrawableId, shiftTypeStringId;
                    switch (mCursor.getShiftType()) {
                        case NORMAL_DAY:
                            shiftTypeDrawableId = R.drawable.ic_normal_day_black_24dp;
                            shiftTypeStringId = R.string.normal_day;
                            break;
                        case LONG_DAY:
                            shiftTypeDrawableId = R.drawable.ic_long_day_black_24dp;
                            shiftTypeStringId = R.string.long_day;
                            break;
                        case NIGHT_SHIFT:
                            shiftTypeDrawableId = R.drawable.ic_night_shift_black_24dp;
                            shiftTypeStringId = R.string.night_shift;
                            break;
                        case OTHER:
                            shiftTypeDrawableId = R.drawable.ic_custom_shift_black_24dp;
                            shiftTypeStringId = R.string.custom;
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    holder.secondaryTextView.setText(shiftTypeStringId);
                    holder.primaryIconView.setImageResource(shiftTypeDrawableId);
//                    holder.primaryIconView.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    holder.primaryTextView.setText(R.string.time_between_shifts);
                    duration = mCursor.getTimeBetweenShifts();
                    if (duration == null) {
                        holder.secondaryTextView.setText(R.string.not_applicable);
//                        holder.primaryIconView.setVisibility(View.INVISIBLE);
                    } else {
                        // TODO: 1/03/17 messy
                        holder.secondaryTextView.setText(periodFormatter.print(duration.toPeriodTo(mCursor.getShift().getStart())));
                        holder.primaryIconView.setImageResource(AppConstants.hasInsufficientTimeBetweenShifts(duration) ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
//                        holder.primaryIconView.setVisibility(View.VISIBLE);
                    }
                    break;
                case 2:
                    holder.primaryTextView.setText(R.string.duration_worked_over_day);
                    duration = mCursor.getDurationOverDay();
                    holder.secondaryTextView.setText(periodFormatter.print(duration.toPeriod()));
                    holder.primaryIconView.setImageResource(AppConstants.exceedsDurationOverDay(duration) ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
//                    holder.primaryIconView.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    holder.primaryTextView.setText(R.string.duration_worked_over_week);
                    duration = mCursor.getDurationOverWeek();
                    holder.secondaryTextView.setText(periodFormatter.print(duration.toPeriod()));
                    holder.primaryIconView.setImageResource(AppConstants.exceedsDurationOverWeek(duration) ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
//                    holder.primaryIconView.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    holder.primaryTextView.setText(R.string.duration_worked_over_fortnight);
                    duration = mCursor.getDurationOverFortnight();
                    holder.secondaryTextView.setText(periodFormatter.print(duration.toPeriod()));
                    holder.primaryIconView.setImageResource(AppConstants.exceedsDurationOverFortnight(duration) ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
//                    holder.primaryIconView.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    holder.primaryTextView.setText(R.string.last_weekend_worked);
                    Interval previousWeekend = mCursor.getPreviousWeekend();
                    //noinspection ConstantConditions
                    holder.secondaryTextView.setText(getString(R.string.period_format, previousWeekend.getStartMillis(), previousWeekend.getEndMillis() - 1));
                    holder.primaryIconView.setImageResource(mCursor.consecutiveWeekendsWorked() ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
//
//                    if (previousWeekend != null) {
//                        holder.secondaryTextView.setText(getString(R.string.period_format, previousWeekend.getStartMillis(), previousWeekend.getEndMillis() - 1));
//                        holder.primaryIconView.setImageResource(mCursor.consecutiveWeekendsWorked() ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
////
////                        error = cursor.consecutiveWeekendsWorked();
////                        mLastWeekendWorkedView.setTextColor(error ? mErrorColor : mTextColor);
////                        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mLastWeekendWorkedView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
//                        holder.primaryIconView.setVisibility(View.VISIBLE);
//                    } else {
//                        holder.secondaryTextView.setText(R.string.not_applicable);
//                        holder.primaryIconView.setVisibility(View.INVISIBLE);
////
////                        mLastWeekendWorkedView.setTextColor(mTextColor);
////                        mLastWeekendWorkedView.setCompoundDrawables(null, null, null, null);
//                    }
//                    if (currentWeekend != null) {
//                mCurrentWeekendView.setText(getString(R.string.period_format, currentWeekend.getStartMillis(), currentWeekend.getEndMillis() - 1));
//                mLastWeekendWorkedLabelView.setVisibility(View.VISIBLE);
//                Interval previousWeekend = cursor.getPreviousWeekend();
//                if (previousWeekend != null) {
//                    mLastWeekendWorkedView.setText(getString(R.string.period_format, previousWeekend.getStartMillis(), previousWeekend.getEndMillis() - 1));
//                    error = cursor.consecutiveWeekendsWorked();
//                    mLastWeekendWorkedView.setTextColor(error ? mErrorColor : mTextColor);
//                    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mLastWeekendWorkedView, 0, 0, error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp, 0);
//                } else {
//                    mLastWeekendWorkedView.setText(R.string.not_applicable);
//                    mLastWeekendWorkedView.setTextColor(mTextColor);
//                    mLastWeekendWorkedView.setCompoundDrawables(null, null, null, null);
//                }
//                mLastWeekendWorkedView.setVisibility(View.VISIBLE);
//            } else {
//                mCurrentWeekendView.setText(R.string.not_applicable);
//                mLastWeekendWorkedLabelView.setVisibility(View.GONE);
//                mLastWeekendWorkedView.setVisibility(View.GONE);
//            }

                    break;
                default:
                    throw new IllegalArgumentException();
            }
            holder.secondaryIconView.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return (mCursor != null && mCursor.moveToFirst()) ? mCursor.getPreviousWeekend() != null ? 6 : 5 : 0;
        }
    }
}
