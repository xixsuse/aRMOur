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

abstract public class AbstractShiftDetailFragment extends ShiftTypeVariableFragment {
    long mShiftId;
    TextView
            mDateView,
            mStartTimeView,
            mEndTimeView,
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
        mStartTimeView = (TextView) layout.findViewById(R.id.start_time);
        mEndTimeView = (TextView) layout.findViewById(R.id.end_time);
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
