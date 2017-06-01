package com.skepticalone.mecachecker.components.summary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;

public class RosteredSummaryFragment extends SummaryFragment {
    @Override
    int getLoaderId() {
        return SummaryActivity.LOADER_ID_ROSTERED;
    }

    @Override
    SummaryAdapter getNewAdapter() {
        return null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }
}
