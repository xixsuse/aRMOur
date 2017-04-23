package com.skepticalone.mecachecker.components;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;

import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

abstract public class AbstractShiftDetailFragment extends ShiftTypeVariableFragment {
    final static PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
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
    long mShiftId;
    TextView
            mDateView,
            mRosteredStartTimeView,
            mRosteredEndTimeView,
            mLoggedStartTimeView,
            mLoggedEndTimeView,
            mToggleButtonView;
    View mLoggedTimesContainer;

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShiftId = getArguments().getLong(ShiftDetailActivity.SHIFT_ID, ShiftDetailActivity.NO_ID);
    }

    @Override
    boolean shouldAddDivider() {
        return false;
    }

    @Override
    int getLayout() {
        return R.layout.shift_detail_fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        //noinspection ConstantConditions
        mDateView = (TextView) layout.findViewById(R.id.date);
        mRosteredStartTimeView = (TextView) layout.findViewById(R.id.rostered_start_time);
        mRosteredEndTimeView = (TextView) layout.findViewById(R.id.rostered_end_time);
        mLoggedTimesContainer = layout.findViewById(R.id.logged_times);
        mLoggedStartTimeView = (TextView) mLoggedTimesContainer.findViewById(R.id.logged_start_time);
        mLoggedEndTimeView = (TextView) mLoggedTimesContainer.findViewById(R.id.logged_end_time);
        mToggleButtonView = (TextView) layout.findViewById(R.id.toggle_button);
        return layout;
    }

    @Override
    public final void onLoaderReset(Loader<Cursor> loader) {
    }
}
