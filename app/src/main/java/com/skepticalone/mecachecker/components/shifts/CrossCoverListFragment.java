package com.skepticalone.mecachecker.components.shifts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;
import org.joda.time.Instant;

public class CrossCoverListFragment extends SinglePaymentItemListFragment {

    private static final String[] PROJECTION = {
            Contract.CrossCoverShifts._ID,
            Contract.CrossCoverShifts.COLUMN_NAME_DATE,
            Contract.CrossCoverShifts.COLUMN_NAME_CLAIMED,
            Contract.CrossCoverShifts.COLUMN_NAME_PAID,
            Contract.CrossCoverShifts.COLUMN_NAME_COMMENT,
    };

    private static final int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_DATE = 1,
            COLUMN_INDEX_CLAIMED = 2,
            COLUMN_INDEX_PAID = 3,
            COLUMN_INDEX_COMMENT = 4;

    private Listener mListener;
    @Nullable
    private Instant mLastCrossCover = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Listener) context;
    }

    @Override
    int getTitle() {
        return R.string.cross_cover;
    }

    @Override
    int getLoaderId() {
        return MainActivity.LOADER_ID_CROSS_COVER_LIST;
    }

    @Override
    Uri getContentUri() {
        return Provider.crossCoverShiftsUri;
    }

    @Override
    Uri getItemUri(long id) {
        return Provider.crossCoverShiftUri(id);
    }

    @Override
    String[] getProjection() {
        return PROJECTION;
    }

    @Nullable
    @Override
    String getSortOrder() {
        return Contract.CrossCoverShifts.COLUMN_NAME_DATE;
    }

    @Override
    int getColumnIndexId() {
        return COLUMN_INDEX_ID;
    }

    @Override
    int getColumnIndexClaimed() {
        return COLUMN_INDEX_CLAIMED;
    }

    @Override
    int getColumnIndexPaid() {
        return COLUMN_INDEX_PAID;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Cursor cursor) {
        return DateTimeUtils.getFullDateString(new DateTime(cursor.getLong(COLUMN_INDEX_DATE)));
    }

    @Nullable
    @Override
    String getSecondLine(@NonNull Cursor cursor) {
        return cursor.isNull(COLUMN_INDEX_COMMENT) ? null : cursor.getString(COLUMN_INDEX_COMMENT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        mLastCrossCover = data.moveToLast() ? new Instant(data.getLong(COLUMN_INDEX_DATE)) : null;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        super.onLoaderReset(loader);
        mLastCrossCover = null;
    }

    @Override
    void addItem() {
        ContentValues values = new ContentValues();
        values.put(Contract.CrossCoverShifts.COLUMN_NAME_DATE, (mLastCrossCover == null ? new DateTime() : mLastCrossCover.toDateTime().plusDays(1)).withTimeAtStartOfDay().getMillis());
        values.put(Contract.CrossCoverShifts.COLUMN_NAME_PAYMENT, PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(getString(R.string.key_cross_cover_payment), getResources().getInteger(R.integer.default_cross_cover_payment)));
        getActivity().getContentResolver().insert(Provider.crossCoverShiftsUri, values);
    }

    @Override
    void onItemClicked(long id) {
        mListener.onCrossCoverClicked(id);
    }

    interface Listener {
        void onCrossCoverClicked(long id);
    }

}
