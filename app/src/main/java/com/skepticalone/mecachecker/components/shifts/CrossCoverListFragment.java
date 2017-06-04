package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.components.ShiftListActivity;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

public class CrossCoverListFragment extends AbstractSinglePaymentItemListFragment {

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

    @Override
    int getTitle() {
        return R.string.cross_cover;
    }

    @Override
    int getLoaderId() {
        return ShiftListActivity.LOADER_ID_CROSS_COVER_LIST;
    }

    @Override
    Uri getContentUri() {
        return Provider.crossCoverShiftsUri;
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

    @Override
    CharSequence getText(@NonNull Cursor cursor) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(DateTimeUtils.getFullDateString(new DateTime(cursor.getLong(COLUMN_INDEX_DATE))));
        ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (!cursor.isNull(COLUMN_INDEX_COMMENT)) {
            ssb.append('\n');
            ssb.append(cursor.getString(COLUMN_INDEX_COMMENT));
        }
        return ssb;
    }

}
