package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Compliance;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.util.AppConstants;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.Interval;

public class RosteredShiftDetailFragment extends DetailFragment implements RosteredShiftData.Callbacks, ComplianceData.Callbacks {

    //    private static final String[] PROJECTION = {
//            Contract.RosteredShifts.COLUMN_NAME_ROSTERED_START,
//            Contract.RosteredShifts.COLUMN_NAME_ROSTERED_END,
//            Contract.RosteredShifts.COLUMN_NAME_LOGGED_START,
//            Contract.RosteredShifts.COLUMN_NAME_LOGGED_END
//    };
    private final static int
//            COLUMN_INDEX_START = 0,
//            COLUMN_INDEX_END = 1,
//            COLUMN_INDEX_LOGGED_START = 2,
//            COLUMN_INDEX_LOGGED_END = 3,
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_START = 1,
            ROW_NUMBER_END = 2,
            ROW_NUMBER_SHIFT_TYPE = 3,
            ROW_NUMBER_LOGGED_SWITCH = 4,
            ROW_NUMBER_LOGGED_START = 5,
            ROW_NUMBER_LOGGED_END = 6,
            ROW_NUMBER_INTERVAL_BETWEEN_SHIFTS = 7,
            ROW_NUMBER_DURATION_WORKED_OVER_DAY = 8,
            ROW_NUMBER_DURATION_WORKED_OVER_WEEK = 9,
            ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT = 10,
            ROW_NUMBER_LAST_WEEKEND_WORKED = 11,
            ROW_COUNT = 12;

    private final RosteredShiftData mShiftData = new RosteredShiftData(this);
    private final ComplianceData mComplianceData = new ComplianceData(this);
    private ShiftUtil.Calculator mCalculator;

    static RosteredShiftDetailFragment create(long id) {
        RosteredShiftDetailFragment fragment = new RosteredShiftDetailFragment();
        fragment.setArguments(createArguments(id));
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCalculator = new ShiftUtil.Calculator(context);
    }

    @Override
    public ShiftType getShiftType(Interval shift) {
        return mCalculator.getShiftType(shift);
    }

    @Override
    int getTitle() {
        return R.string.rostered_shift;
    }

    @Override
    int getLoaderId() {
        return LifecycleConstants.LOADER_ID_ROSTERED_DETAIL;
    }

    @Override
    public Uri getContentUri() {
        return Provider.rosteredShiftWithComplianceUri(getItemId());
    }

    @NonNull
    @Override
    public String getColumnNameStartOrDate() {
        return Contract.RosteredShifts.COLUMN_NAME_ROSTERED_START;
    }

    @NonNull
    @Override
    public String getColumnNameEnd() {
        return Contract.RosteredShifts.COLUMN_NAME_ROSTERED_END;
    }

    @NonNull
    @Override
    public String getColumnNameLoggedStart() {
        return Contract.RosteredShifts.COLUMN_NAME_LOGGED_START;
    }

    @NonNull
    @Override
    public String getColumnNameLoggedEnd() {
        return Contract.RosteredShifts.COLUMN_NAME_LOGGED_END;
    }

    @Nullable
    @Override
    String[] getProjection() {
        return null;
    }

    @Override
    public int getColumnIndexStartOrDate() {
        return Compliance.COLUMN_INDEX_ROSTERED_START;
    }

    @Override
    public int getColumnIndexEnd() {
        return Compliance.COLUMN_INDEX_ROSTERED_END;
    }

    @Override
    public int getColumnIndexLoggedStart() {
        return Compliance.COLUMN_INDEX_LOGGED_START;
    }

    @Override
    public int getColumnIndexLoggedEnd() {
        return Compliance.COLUMN_INDEX_LOGGED_END;
    }

    @Override
    public int getRowNumberDate() {
        return ROW_NUMBER_DATE;
    }

    @Override
    public int getRowNumberStart() {
        return ROW_NUMBER_START;
    }

    @Override
    public int getRowNumberEnd() {
        return ROW_NUMBER_END;
    }

    @Override
    public int getRowNumberShiftType() {
        return ROW_NUMBER_SHIFT_TYPE;
    }

    @Override
    public int getRowNumberLoggedSwitch() {
        return ROW_NUMBER_LOGGED_SWITCH;
    }

    @Override
    public int getRowNumberLoggedStart() {
        return mShiftData.isLogged() ? ROW_NUMBER_LOGGED_START : NO_ROW_NUMBER;
    }

    @Override
    public int getRowNumberLoggedEnd() {
        return mShiftData.isLogged() ? ROW_NUMBER_LOGGED_END : NO_ROW_NUMBER;
    }

    @Override
    public int getRowNumberIntervalBetweenShifts() {
        return mShiftData.isLogged() ? ROW_NUMBER_INTERVAL_BETWEEN_SHIFTS : (ROW_NUMBER_INTERVAL_BETWEEN_SHIFTS - 2);
    }

    @Override
    public int getRowNumberDurationWorkedOverDay() {
        return mShiftData.isLogged() ? ROW_NUMBER_DURATION_WORKED_OVER_DAY : (ROW_NUMBER_DURATION_WORKED_OVER_DAY - 2);
    }

    @Override
    public int getRowNumberDurationWorkedOverWeek() {
        return mShiftData.isLogged() ? ROW_NUMBER_DURATION_WORKED_OVER_WEEK : (ROW_NUMBER_DURATION_WORKED_OVER_WEEK - 2);
    }

    @Override
    public int getRowNumberDurationWorkedOverFortnight() {
        return mShiftData.isLogged() ? ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT : (ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT - 2);
    }

    @Override
    public int getRowNumberLastWeekendWorked() {
        if (!mComplianceData.isWeekend()) return NO_ROW_NUMBER;
        return mShiftData.isLogged() ? ROW_NUMBER_LAST_WEEKEND_WORKED : (ROW_NUMBER_LAST_WEEKEND_WORKED - 2);
    }

    @Override
    int getRowCountIfLoaded() {
        int count = ROW_COUNT;
        if (!mComplianceData.isWeekend()) {
            count -= 1;
        }
        if (!mShiftData.isLogged()) {
            count -= 2;
        }
        return count;
    }

    @Override
    void readFromPositionedCursor(@NonNull Cursor cursor) {
        mShiftData.readFromPositionedCursor(cursor);
        mComplianceData.readFromPositionedCursor(cursor);
    }

    @Nullable
    @Override
    ViewHolderType getViewHolderType(int position) {
        return AbstractData.getViewHolderType(position, mShiftData, mComplianceData);
    }

    @Override
    SwitchListItemViewHolder createSwitchListItemViewHolder(ViewGroup parent) {
        return new SwitchListItemViewHolder(parent, mShiftData);
    }

    @Override
    boolean bindPlainListItemViewHolder(PlainListItemViewHolder holder, int position) {
        if (mShiftData.bindToHolder(getActivity(), holder, position)) {
            holder.secondaryIcon.setVisibility(View.GONE);
        } else if (mComplianceData.bindToHolder(getActivity(), holder, position)) {
            holder.secondaryIcon.setVisibility(View.VISIBLE);
        } else return false;
        return true;
    }

    @Override
    boolean bindSwitchListItemViewHolder(SwitchListItemViewHolder holder, int position) {
        return mShiftData.bindToHolder(getActivity(), holder, position);
    }

    @NonNull
    @Override
    public String getMecaIntervalBetweenShifts() {
        return getString(R.string.meca_minimum_hours_between_shifts, AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS);
    }

    @NonNull
    @Override
    public String getMecaDurationOverDay() {
        return getString(R.string.meca_maximum_hours_over_day, AppConstants.MAXIMUM_HOURS_OVER_DAY);
    }

    @NonNull
    @Override
    public String getMecaDurationOverWeek() {
        return getString(R.string.meca_maximum_hours_over_week, AppConstants.MAXIMUM_HOURS_OVER_WEEK);
    }

    @NonNull
    @Override
    public String getMecaDurationOverFortnight() {
        return getString(R.string.meca_maximum_hours_over_fortnight, AppConstants.MAXIMUM_HOURS_OVER_FORTNIGHT);
    }

    @NonNull
    @Override
    public String getMecaPreviousWeekend() {
        return getString(R.string.meca_consecutive_weekends);
    }
}