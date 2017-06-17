package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.LifecycleConstants;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.Interval;

import static com.skepticalone.mecachecker.util.ShiftUtil.getClaimStatusIcon;

public class AdditionalShiftsListFragment extends ShiftTypeAwareItemListFragment {

    private static final String[] PROJECTION = {
            Contract.AdditionalShifts._ID,
            Contract.AdditionalShifts.COLUMN_NAME_START,
            Contract.AdditionalShifts.COLUMN_NAME_END,
            Contract.AdditionalShifts.COLUMN_NAME_CLAIMED,
            Contract.AdditionalShifts.COLUMN_NAME_PAID,
            Contract.AdditionalShifts.COLUMN_NAME_COMMENT,
    };
    private final static int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_START = 1,
            COLUMN_INDEX_END = 2,
            COLUMN_INDEX_CLAIMED = 3,
            COLUMN_INDEX_PAID = 4,
            COLUMN_INDEX_COMMENT = 5;

    @Override
    int getTitle() {
        return R.string.additional_shifts;
    }

    @Override
    int getLoaderId() {
        return LifecycleConstants.LOADER_ID_ADDITIONAL_LIST;
    }

    @Override
    public Uri getReadContentUri() {
        return Provider.additionalShiftsUri;
    }

    @Nullable
    @Override
    String[] getProjection() {
        return PROJECTION;
    }

    @Nullable
    @Override
    String getSortOrder() {
        return Contract.AdditionalShifts.COLUMN_NAME_START;
    }

    @Override
    int getColumnIndexId() {
        return COLUMN_INDEX_ID;
    }

    @Override
    void bindViewHolderToCursor(ListItemViewHolder holder, @NonNull Cursor cursor) {
        Interval shift = new Interval(cursor.getLong(COLUMN_INDEX_START), cursor.getLong(COLUMN_INDEX_END));
        holder.bindPlain(
                ShiftUtil.getShiftTypeIcon(getShiftType(shift)),
                DateTimeUtils.getFullDateString(shift.getStart()),
                DateTimeUtils.getTimeSpanString(shift),
                cursor.isNull(COLUMN_INDEX_COMMENT) ? null : cursor.getString(COLUMN_INDEX_COMMENT),
                getClaimStatusIcon(cursor, COLUMN_INDEX_CLAIMED, COLUMN_INDEX_PAID)
        );
    }

    @Override
    int getColumnIndexShiftEnd() {
        return COLUMN_INDEX_END;
    }

    @Override
    DateTime getEarliestStartForNewShift(@Nullable Instant lastShiftEnd) {
        DateTime minStart = new DateTime().withTimeAtStartOfDay();
        if (lastShiftEnd != null && lastShiftEnd.isAfter(minStart)) {
            return lastShiftEnd.toDateTime();
        }
        return minStart;
    }

    @Override
    void addShift(@NonNull Interval newShift) {
        ContentValues values = new ContentValues();
        values.put(Contract.AdditionalShifts.COLUMN_NAME_START, newShift.getStartMillis());
        values.put(Contract.AdditionalShifts.COLUMN_NAME_END, newShift.getEndMillis());
        values.put(Contract.AdditionalShifts.COLUMN_NAME_RATE, PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getInt(getString(R.string.key_hourly_rate), getResources().getInteger(R.integer.default_hourly_rate))
        );
        insert(values);
    }

    @Override
    void onItemClicked(long id) {
        listCallbacks.launch(LifecycleConstants.ITEM_TYPE_ADDITIONAL_SHIFT, id);
    }

}
