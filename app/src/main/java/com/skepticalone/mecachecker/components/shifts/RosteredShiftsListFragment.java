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
import com.skepticalone.mecachecker.data.Compliance;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.util.AppConstants;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.Interval;

public class RosteredShiftsListFragment extends ShiftTypeAwareItemListFragment {

    private Listener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Listener) context;
    }

    @Override
    int getTitle() {
        return R.string.rostered_shifts;
    }

    @Override
    int getLoaderId() {
        return MainActivity.LOADER_ID_ROSTERED_LIST;
    }

    @Override
    Uri getContentUri() {
        return Provider.rosteredShiftsWithComplianceUri;
    }

    @Override
    Uri getItemUri(long id) {
        return Provider.rosteredShiftUri(id);
    }

    @Nullable
    @Override
    String[] getProjection() {
        return null;
    }

    @Override
    int getColumnIndexId() {
        return Compliance.COLUMN_INDEX_ID;
    }

    @Override
    int getColumnIndexShiftEnd() {
        return Compliance.COLUMN_INDEX_ROSTERED_END;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, new Compliance.Wrapper(data));
    }

    @Override
    void bindViewHolderToCursor(PlainListItemViewHolder holder, @NonNull Cursor unwrappedCursor) {
        Compliance.Wrapper cursor = (Compliance.Wrapper) unwrappedCursor;
        Interval rosteredShift = cursor.getRosteredShift(), loggedShift = cursor.getLoggedShift();
        holder.primaryIcon.setImageResource(getShiftTypeIcon(getShiftType(rosteredShift)));
        holder.setText(
                DateTimeUtils.getFullDateString(rosteredShift.getStart()),
                DateTimeUtils.getTimeSpanString(rosteredShift),
                loggedShift == null ? null : getString(R.string.logged_format, DateTimeUtils.getTimeSpanString(loggedShift))
        );
        boolean error = AppConstants.hasInsufficientIntervalBetweenShifts(cursor.getIntervalBetweenShifts()) ||
                AppConstants.exceedsDurationOverDay(cursor.getDurationOverDay()) ||
                AppConstants.exceedsDurationOverWeek(cursor.getDurationOverWeek()) ||
                AppConstants.exceedsDurationOverFortnight(cursor.getDurationOverFortnight()) ||
                cursor.consecutiveWeekendsWorked();
        holder.secondaryIcon.setImageResource(error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
    }

    @Override
    boolean shouldSkipWeekendsOnAdding(ShiftType shiftType) {
        int skipWeekendsKeyId, defaultSkipWeekendsId;
        switch (shiftType) {
            case NORMAL_DAY:
                skipWeekendsKeyId = R.string.key_skip_weekend_normal_day;
                defaultSkipWeekendsId = R.bool.default_skip_weekend_normal_day;
                break;
            case LONG_DAY:
                skipWeekendsKeyId = R.string.key_skip_weekend_long_day;
                defaultSkipWeekendsId = R.bool.default_skip_weekend_long_day;
                break;
            case NIGHT_SHIFT:
                skipWeekendsKeyId = R.string.key_skip_weekend_night_shift;
                defaultSkipWeekendsId = R.bool.default_skip_weekend_night_shift;
                break;
            default:
                throw new IllegalStateException();
        }
        return PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getBoolean(getString(skipWeekendsKeyId), getResources().getBoolean(defaultSkipWeekendsId));
    }

    @Override
    DateTime getEarliestStartForNewShift(@Nullable Instant lastShiftEnd) {
        return lastShiftEnd == null ? new DateTime().withTimeAtStartOfDay() : lastShiftEnd.plus(AppConstants.MINIMUM_DURATION_BETWEEN_SHIFTS).toDateTime();
    }

    @Override
    void addShift(@NonNull Interval newShift) {
        ContentValues values = new ContentValues();
        values.put(Contract.RosteredShifts.COLUMN_NAME_ROSTERED_START, newShift.getStartMillis());
        values.put(Contract.RosteredShifts.COLUMN_NAME_ROSTERED_END, newShift.getEndMillis());
        if (getActivity().getContentResolver().insert(Provider.rosteredShiftsUri, values) != null) {
            scrollToEndAtNextLoad();
        }
    }

    @Override
    void onItemClicked(long id) {
        mListener.onRosteredShiftClicked(id);
    }

    interface Listener {
        void onRosteredShiftClicked(long id);
    }

}
