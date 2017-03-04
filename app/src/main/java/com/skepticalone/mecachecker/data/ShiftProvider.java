package com.skepticalone.mecachecker.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.skepticalone.mecachecker.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;

public final class ShiftProvider extends ContentProvider {

    private static final String TAG = "ShiftProvider";
    private static final String
            AUTHORITY = "com.skepticalone.mecachecker.provider",
            PROVIDER_TYPE = "/vnd.com.skepticalone.provider.",
            SHIFTS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + PROVIDER_TYPE + ShiftContract.RosteredShift.TABLE_NAME,
            SHIFT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + PROVIDER_TYPE + ShiftContract.RosteredShift.TABLE_NAME,
            WITH_COMPLIANCE = "_with_compliance",
            SHIFTS_WITH_COMPLIANCE_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + PROVIDER_TYPE + ShiftContract.RosteredShift.TABLE_NAME + WITH_COMPLIANCE,
            SHIFT_WITH_COMPLIANCE_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + PROVIDER_TYPE + ShiftContract.RosteredShift.TABLE_NAME + WITH_COMPLIANCE;
    private static final Uri baseContentUri = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).build();
    public static final Uri shiftsUri = baseContentUri.buildUpon().appendPath(ShiftContract.RosteredShift.TABLE_NAME).build();
    public static final Uri shiftsWithComplianceUri = baseContentUri.buildUpon().appendPath(ShiftContract.RosteredShift.TABLE_NAME + WITH_COMPLIANCE).build();
    private static final int SHIFTS = 1;
    private static final int SHIFT_ID = 2;
    private static final int SHIFTS_WITH_COMPLIANCE = 3;
    private static final int SHIFT_ID_WITH_COMPLIANCE = 4;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, ShiftContract.RosteredShift.TABLE_NAME, SHIFTS);
        sUriMatcher.addURI(AUTHORITY, ShiftContract.RosteredShift.TABLE_NAME + "/#", SHIFT_ID);
        sUriMatcher.addURI(AUTHORITY, ShiftContract.RosteredShift.TABLE_NAME + WITH_COMPLIANCE, SHIFTS_WITH_COMPLIANCE);
        sUriMatcher.addURI(AUTHORITY, ShiftContract.RosteredShift.TABLE_NAME + WITH_COMPLIANCE + "/#", SHIFT_ID_WITH_COMPLIANCE);
    }

    private ShiftDbHelper mDbHelper;
    private String normalDayStartKey,
            normalDayEndKey,
            longDayStartKey,
            longDayEndKey,
            nightShiftStartKey,
            nightShiftEndKey;
    private int
            normalDayStartDefault,
            normalDayEndDefault,
            longDayStartDefault,
            longDayEndDefault,
            nightShiftStartDefault,
            nightShiftEndDefault;

    public static Uri shiftUri(long shiftId) {
        return Uri.withAppendedPath(shiftsUri, Long.toString(shiftId));
    }

    public static Uri shiftWithComplianceUri(long shiftId) {
        return Uri.withAppendedPath(shiftsWithComplianceUri, Long.toString(shiftId));
    }

//    public static ContentValues getContentValues(long scheduledStart, long scheduledEnd) {
//        ContentValues values = new ContentValues();
//        values.put(ShiftContract.RosteredShift.COLUMN_NAME_ROSTERED_START, scheduledStart);
//        values.put(ShiftContract.RosteredShift.COLUMN_NAME_ROSTERED_END, scheduledEnd);
//        return values;
//    }

    private static int getShiftType(
            int start,
            int end,
            int normalDayStart,
            int normalDayEnd,
            int longDayStart,
            int longDayEnd,
            int nightShiftStart,
            int nightShiftEnd
    ) {
        if (start == normalDayStart && end == normalDayEnd) {
            return ShiftContract.Compliance.SHIFT_TYPE_NORMAL_DAY;
        } else if (start == longDayStart && end == longDayEnd) {
            return ShiftContract.Compliance.SHIFT_TYPE_LONG_DAY;
        } else if (start == nightShiftStart && end == nightShiftEnd) {
            return ShiftContract.Compliance.SHIFT_TYPE_NIGHT_SHIFT;
        } else {
            return ShiftContract.Compliance.SHIFT_TYPE_OTHER;
        }
    }

    @NonNull
    private static Duration getDurationSince(Cursor cursor, int positionToCheck, Instant cutOff) {
        Duration totalDuration = Duration.ZERO;
        cursor.moveToPosition(positionToCheck);
        do {
            Instant end = new Instant(cursor.getLong(ShiftContract.Compliance.COLUMN_INDEX_ROSTERED_END));
            if (!end.isAfter(cutOff)) break;
            Instant start = new Instant(cursor.getLong(ShiftContract.Compliance.COLUMN_INDEX_ROSTERED_START));
            totalDuration = totalDuration.plus(new Duration(cutOff.isAfter(start) ? cutOff : start, end));
        } while (cursor.moveToPrevious());
        return totalDuration;
    }

    @Nullable
    private static Interval getWeekend(Interval shift) {
        DateTime weekendStart = shift.getStart().withDayOfWeek(DateTimeConstants.SATURDAY).withTimeAtStartOfDay();
        Interval weekend = new Interval(weekendStart, weekendStart.plusDays(2));
        return shift.overlaps(weekend) ? weekend : null;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new ShiftDbHelper(getContext());
        //noinspection ConstantConditions
        Resources resources = getContext().getResources();
        normalDayStartKey = resources.getString(R.string.key_start_normal_day);
        normalDayEndKey = resources.getString(R.string.key_end_normal_day);
        longDayStartKey = resources.getString(R.string.key_start_long_day);
        longDayEndKey = resources.getString(R.string.key_end_long_day);
        nightShiftStartKey = resources.getString(R.string.key_start_night_shift);
        nightShiftEndKey = resources.getString(R.string.key_end_night_shift);
        normalDayStartDefault = resources.getInteger(R.integer.default_start_normal_day);
        normalDayEndDefault = resources.getInteger(R.integer.default_end_normal_day);
        longDayStartDefault = resources.getInteger(R.integer.default_start_long_day);
        longDayEndDefault = resources.getInteger(R.integer.default_end_long_day);
        nightShiftStartDefault = resources.getInteger(R.integer.default_start_night_shift);
        nightShiftEndDefault = resources.getInteger(R.integer.default_end_night_shift);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query() called with: uri: " + uri);
        String table;
        switch (sUriMatcher.match(uri)) {
            case SHIFT_ID:
                selection = ShiftContract.RosteredShift._ID + "=" + uri.getLastPathSegment();
                selectionArgs = null;
                // intentional fallthrough
            case SHIFTS:
                table = ShiftContract.RosteredShift.TABLE_NAME;
                break;
            case SHIFTS_WITH_COMPLIANCE:
                return getComplianceCursor(null);
            case SHIFT_ID_WITH_COMPLIANCE:
                return getComplianceCursor(Long.parseLong(uri.getLastPathSegment()));
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
        Cursor cursor = mDbHelper.getReadableDatabase().query(
                table,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        //noinspection ConstantConditions
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case SHIFTS:
                return SHIFTS_TYPE;
            case SHIFT_ID:
                return SHIFT_TYPE;
            case SHIFTS_WITH_COMPLIANCE:
                return SHIFTS_WITH_COMPLIANCE_TYPE;
            case SHIFT_ID_WITH_COMPLIANCE:
                return SHIFT_WITH_COMPLIANCE_TYPE;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case SHIFTS:
                long shiftId = mDbHelper.getWritableDatabase().insertOrThrow(ShiftContract.RosteredShift.TABLE_NAME, null, values);
                //noinspection ConstantConditions
                getContext().getContentResolver().notifyChange(uri, null);
                return shiftUri(shiftId);
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case SHIFT_ID:
                int deleted = mDbHelper.getWritableDatabase().delete(ShiftContract.RosteredShift.TABLE_NAME, ShiftContract.RosteredShift._ID + "=" + uri.getLastPathSegment(), null);
                if (deleted > 0) {
                    //noinspection ConstantConditions
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return deleted;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case SHIFT_ID:
                try {
                    int updated = mDbHelper.getWritableDatabase().update(ShiftContract.RosteredShift.TABLE_NAME, values, ShiftContract.RosteredShift._ID + "=" + uri.getLastPathSegment(), null);
                    if (updated > 0) {
                        //noinspection ConstantConditions
                        getContext().getContentResolver().notifyChange(uri, null);
                    }
                    return updated;
                } catch (SQLiteConstraintException e) {
                    return 0;
                }
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
    }

    @NonNull
    private Cursor getComplianceCursor(@Nullable Long shiftId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int
                normalDayStart = preferences.getInt(normalDayStartKey, normalDayStartDefault),
                normalDayEnd = preferences.getInt(normalDayEndKey, normalDayEndDefault),
                longDayStart = preferences.getInt(longDayStartKey, longDayStartDefault),
                longDayEnd = preferences.getInt(longDayEndKey, longDayEndDefault),
                nightShiftStart = preferences.getInt(nightShiftStartKey, nightShiftStartDefault),
                nightShiftEnd = preferences.getInt(nightShiftEndKey, nightShiftEndDefault);
        Cursor initialCursor = mDbHelper.getReadableDatabase().query(
                ShiftContract.RosteredShift.TABLE_NAME,
                ShiftContract.Compliance.PROJECTION,
                null,
                null,
                null,
                null,
                ShiftContract.RosteredShift.COLUMN_NAME_ROSTERED_START
        );
        int initialCursorCount = initialCursor.getCount();
        MatrixCursor newCursor = new MatrixCursor(ShiftContract.Compliance.COLUMN_NAMES, shiftId == null ? initialCursorCount : 1);
        for (int i = 0; i < initialCursorCount; i++) {
            initialCursor.moveToPosition(i);
            long id = initialCursor.getLong(ShiftContract.Compliance.COLUMN_INDEX_ID);
            if (shiftId != null && shiftId != id) continue;
            Interval currentShift = new Interval(initialCursor.getLong(ShiftContract.Compliance.COLUMN_INDEX_ROSTERED_START), initialCursor.getLong(ShiftContract.Compliance.COLUMN_INDEX_ROSTERED_END));
            Interval loggedShift = (initialCursor.isNull(ShiftContract.Compliance.COLUMN_INDEX_LOGGED_START) || initialCursor.isNull(ShiftContract.Compliance.COLUMN_INDEX_LOGGED_END)) ?
                    null :
                    new Interval(initialCursor.getLong(ShiftContract.Compliance.COLUMN_INDEX_LOGGED_START), initialCursor.getLong(ShiftContract.Compliance.COLUMN_INDEX_LOGGED_END));
            MatrixCursor.RowBuilder builder = newCursor.newRow()
                    .add(id)
                    .add(currentShift.getStartMillis())
                    .add(currentShift.getEndMillis())
                    .add(loggedShift == null ? null : loggedShift.getStartMillis())
                    .add(loggedShift == null ? null : loggedShift.getEndMillis())
                    .add(getShiftType(currentShift.getStart().getMinuteOfDay(), currentShift.getEnd().getMinuteOfDay(), normalDayStart, normalDayEnd, longDayStart, longDayEnd, nightShiftStart, nightShiftEnd))
                    .add(initialCursor.moveToPrevious() ? initialCursor.getLong(ShiftContract.Compliance.COLUMN_INDEX_ROSTERED_END) : null)
                    .add(getDurationSince(initialCursor, i, currentShift.getEnd().minusDays(1).toInstant()).getMillis())
                    .add(getDurationSince(initialCursor, i, currentShift.getEnd().minusWeeks(1).toInstant()).getMillis())
                    .add(getDurationSince(initialCursor, i, currentShift.getEnd().minusWeeks(2).toInstant()).getMillis());
            Interval currentWeekend = getWeekend(currentShift);
            if (currentWeekend != null) {
                builder
                        .add(currentWeekend.getStartMillis())
                        .add(currentWeekend.getEndMillis());
                Interval previousWeekend = new Interval(currentWeekend.getStart().minusWeeks(1), currentWeekend.getEnd().minusWeeks(1));
                initialCursor.moveToPosition(i);
                while (initialCursor.moveToPrevious()) {
                    Interval weekendWorked = getWeekend(new Interval(initialCursor.getLong(ShiftContract.Compliance.COLUMN_INDEX_ROSTERED_START), initialCursor.getLong(ShiftContract.Compliance.COLUMN_INDEX_ROSTERED_END)));
                    if (weekendWorked != null && !currentWeekend.equals(weekendWorked)) {
                        builder
                                .add(weekendWorked.getStartMillis())
                                .add(weekendWorked.getEndMillis())
                                .add(weekendWorked.isEqual(previousWeekend) ? 1 : 0);
                        break;
                    }
                }
            }
            if (shiftId != null) break;
        }
        initialCursor.close();
        //noinspection ConstantConditions
        newCursor.setNotificationUri(getContext().getContentResolver(), ShiftProvider.shiftsUri);
        return newCursor;
    }

}
