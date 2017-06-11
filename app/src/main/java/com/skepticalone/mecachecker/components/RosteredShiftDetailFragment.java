package com.skepticalone.mecachecker.components;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

public class RosteredShiftDetailFragment extends DetailFragment implements ShiftData.Callbacks {

    private static final String[] PROJECTION = {
            Contract.RosteredShifts.COLUMN_NAME_ROSTERED_START,
            Contract.RosteredShifts.COLUMN_NAME_ROSTERED_END,
    };
    private final static int
            COLUMN_INDEX_START = 0,
            COLUMN_INDEX_END = 1,
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_START = 1,
            ROW_NUMBER_END = 2,
            ROW_COUNT = 3;
    private final ShiftData mShiftData = new ShiftData(this);

    static RosteredShiftDetailFragment create(long id) {
        RosteredShiftDetailFragment fragment = new RosteredShiftDetailFragment();
        fragment.setArguments(createArguments(id));
        return fragment;
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
    public String getColumnNameStart() {
        return Contract.RosteredShifts.COLUMN_NAME_ROSTERED_START;
    }

    @NonNull
    @Override
    public String getColumnNameEnd() {
        return Contract.RosteredShifts.COLUMN_NAME_ROSTERED_END;
    }

    @Nullable
    @Override
    String[] getProjection() {
        return PROJECTION;
    }

    @Override
    public int getColumnIndexStart() {
        return COLUMN_INDEX_START;
    }

    @Override
    public int getColumnIndexEnd() {
        return COLUMN_INDEX_END;
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
    int getRowCountIfLoaded() {
        return ROW_COUNT;
    }

    @Override
    void readFromPositionedCursor(@NonNull Cursor cursor) {
        mShiftData.readFromPositionedCursor(cursor);
    }

    @Override
    boolean isSwitchType(int position) {
        return AbstractData.isSwitchType(position, mShiftData);
    }

    @Override
    boolean bindPlainListItemViewHolder(PlainListItemViewHolder holder, int position) {
        return mShiftData.bindToHolder(getActivity(), holder, position);
    }

    @Override
    boolean bindSwitchListItemViewHolder(SwitchListItemViewHolder holder, int position) {
        return false;
    }
}