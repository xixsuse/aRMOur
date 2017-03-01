package com.skepticalone.mecachecker.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.util.AppConstants;

public final class ShiftProvider extends ContentProvider {

    private static final String
            AUTHORITY = "com.skepticalone.mecachecker.provider",
            PROVIDER_TYPE = "/vnd.com.skepticalone.provider.",
            SHIFT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + PROVIDER_TYPE + ShiftContract.Shift.TABLE_NAME,
            SHIFTS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + PROVIDER_TYPE + ShiftContract.Shift.TABLE_NAME;
    private static final Uri baseContentUri = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).build();
    public static final Uri shiftsUri = baseContentUri.buildUpon().appendPath(ShiftContract.Shift.TABLE_NAME).build();
    private static final int SHIFTS = 1;
    private static final int SHIFT_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, ShiftContract.Shift.TABLE_NAME, SHIFTS);
        sUriMatcher.addURI(AUTHORITY, ShiftContract.Shift.TABLE_NAME + "/#", SHIFT_ID);
    }

    private String
            normalDayStartKey,
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
    private ShiftDbHelper mDbHelper;

    public static Uri shiftUri(long shiftId) {
        return Uri.withAppendedPath(shiftsUri, Long.toString(shiftId));
    }

    public static ContentValues getContentValues(long start, long end) {
        ContentValues values = new ContentValues();
        values.put(ShiftContract.Shift.COLUMN_NAME_START, start);
        values.put(ShiftContract.Shift.COLUMN_NAME_END, end);
        return values;
    }

    public static ContentValues getContentValues(long start, long end, ShiftCategory category) {
        ContentValues values = getContentValues(start, end);
        values.put(ShiftContract.Shift.COLUMN_NAME_CATEGORY, AppConstants.getShiftCategory(category));
        return values;
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
        Long shiftId;
        switch (sUriMatcher.match(uri)) {
            case SHIFT_ID:
                shiftId = Long.valueOf(uri.getLastPathSegment());
                break;
            case SHIFTS:
                shiftId = null;
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Cursor cursor = new ComplianceCursor.ComplianceMatrixCursor(
                mDbHelper.getReadableDatabase().query(
                        ShiftContract.Shift.TABLE_NAME,
                        ComplianceCursor.PROJECTION,
                        null,
                        null,
                        null,
                        null,
                        ShiftContract.Shift.COLUMN_NAME_START
                ),
                shiftId,
                preferences.getInt(normalDayStartKey, normalDayStartDefault),
                preferences.getInt(normalDayEndKey, normalDayEndDefault),
                preferences.getInt(longDayStartKey, longDayStartDefault),
                preferences.getInt(longDayEndKey, longDayEndDefault),
                preferences.getInt(nightShiftStartKey, nightShiftStartDefault),
                preferences.getInt(nightShiftEndKey, nightShiftEndDefault)
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
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case SHIFTS:
                long shiftId = mDbHelper.getWritableDatabase().insertOrThrow(ShiftContract.Shift.TABLE_NAME, null, values);
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
                int deleted = mDbHelper.getWritableDatabase().delete(ShiftContract.Shift.TABLE_NAME, ShiftContract.Shift._ID + "=" + uri.getLastPathSegment(), null);
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
                    int updated = mDbHelper.getWritableDatabase().update(ShiftContract.Shift.TABLE_NAME, values, ShiftContract.Shift._ID + "=" + uri.getLastPathSegment(), null);
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
}
