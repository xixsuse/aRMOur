package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.util.ShiftTypeUtil;

import org.joda.time.Interval;

public class RosteredShiftDetailFragment extends DetailFragment implements LoggedShiftData.Callbacks {

    private static final String[] PROJECTION = {
            Contract.RosteredShifts.COLUMN_NAME_ROSTERED_START,
            Contract.RosteredShifts.COLUMN_NAME_ROSTERED_END,
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_START,
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_END
    };
    private final static int
            COLUMN_INDEX_START = 0,
            COLUMN_INDEX_END = 1,
            COLUMN_INDEX_LOGGED_START = 2,
            COLUMN_INDEX_LOGGED_END = 3,
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_START = 1,
            ROW_NUMBER_END = 2,
            ROW_NUMBER_SHIFT_TYPE = 3,
            ROW_NUMBER_LOGGED_START = 4,
            ROW_NUMBER_LOGGED_END = 5,
            ROW_COUNT = 6;

    private final LoggedShiftData mShiftData = new LoggedShiftData(this);
    private ShiftTypeUtil.Calculator mCalculator;

    static RosteredShiftDetailFragment create(long id) {
        RosteredShiftDetailFragment fragment = new RosteredShiftDetailFragment();
        fragment.setArguments(createArguments(id));
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCalculator = new ShiftTypeUtil.Calculator(context);
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
        return Provider.rosteredShiftUri(getItemId());
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
        return PROJECTION;
    }

    @Override
    public int getColumnIndexStartOrDate() {
        return COLUMN_INDEX_START;
    }

    @Override
    public int getColumnIndexEnd() {
        return COLUMN_INDEX_END;
    }

    @Override
    public int getColumnIndexLoggedStart() {
        return COLUMN_INDEX_LOGGED_START;
    }

    @Override
    public int getColumnIndexLoggedEnd() {
        return COLUMN_INDEX_LOGGED_END;
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
    public int getRowNumberLoggedStart() {
        return ROW_NUMBER_LOGGED_START;
    }

    @Override
    public int getRowNumberLoggedEnd() {
        return ROW_NUMBER_LOGGED_END;
    }

    @Override
    int getRowCountIfLoaded() {
        return ROW_COUNT;
    }

    @Override
    void readFromPositionedCursor(@NonNull Cursor cursor) {
        mShiftData.readFromPositionedCursor(cursor);
    }

    @Nullable
    @Override
    ViewHolderType getViewHolderType(int position) {
        return AbstractData.getViewHolderType(position, mShiftData);
    }

    @Override
    boolean bindPlainListItemViewHolder(PlainListItemViewHolder holder, int position) {
        return mShiftData.bindToHolder(getActivity(), holder, position);
    }

    @Override
    boolean bindSwitchListItemViewHolder(SwitchListItemViewHolder holder, int position) {
        return mShiftData.bindToHolder(getActivity(), holder, position);
    }
}