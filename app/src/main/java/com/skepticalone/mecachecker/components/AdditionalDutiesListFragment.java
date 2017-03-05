package com.skepticalone.mecachecker.components;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftType;

import org.joda.time.LocalTime;

public class AdditionalDutiesListFragment extends AbstractShiftListFragment {

    public static final String TAG = "AdditionalDuties";

    @Override
    int getTitle() {
        return R.string.additional;
    }

    @Override
    RecyclerView.Adapter getAdapter() {
        return null;
    }

    @Override
    void addShift(ShiftType shiftType, @NonNull LocalTime startTime, @NonNull LocalTime endTime) {
        Log.d(TAG, "addShift() called with: shiftType = [" + shiftType + "], startTime = [" + startTime + "], endTime = [" + endTime + "]");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
