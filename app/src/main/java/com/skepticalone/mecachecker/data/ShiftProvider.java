package com.skepticalone.mecachecker.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

public final class ShiftProvider extends ContentProvider {

    private static final String
            AUTHORITY = "com.skepticalone.mecachecker.provider",
            PROVIDER_TYPE = "/vnd.com.skepticalone.provider.",
            SHIFT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + PROVIDER_TYPE + ShiftContract.Shift.TABLE_NAME,
            SHIFTS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + PROVIDER_TYPE + ShiftContract.Shift.TABLE_NAME,
            SHIFT_OVERLAP_TOAST_MESSAGE = "Shift overlaps!";
    private static final Uri baseContentUri = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).build();
    public static final Uri shiftsUri = baseContentUri.buildUpon().appendPath(ShiftContract.Shift.TABLE_NAME).build();
    private static final int SHIFTS = 1;
    private static final int SHIFT_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, ShiftContract.Shift.TABLE_NAME, SHIFTS);
        sUriMatcher.addURI(AUTHORITY, ShiftContract.Shift.TABLE_NAME + "/#", SHIFT_ID);
    }

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

    @Override
    public boolean onCreate() {
        mDbHelper = new ShiftDbHelper(getContext());
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
        Cursor cursor = new ComplianceCursor.ComplianceMatrixCursor(mDbHelper.getReadableDatabase().query(
                ShiftContract.Shift.TABLE_NAME,
                ComplianceCursor.PROJECTION,
                null,
                null,
                null,
                null,
                ShiftContract.Shift.COLUMN_NAME_START
        ), shiftId);
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
                try {
                    long shiftId = mDbHelper.getWritableDatabase().insertOrThrow(ShiftContract.Shift.TABLE_NAME, null, values);
                    //noinspection ConstantConditions
                    getContext().getContentResolver().notifyChange(uri, null);
                    return shiftUri(shiftId);
                } catch (SQLiteConstraintException e) {
                    Toast.makeText(getContext(), SHIFT_OVERLAP_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
                    return null;
                }
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
                    Toast.makeText(getContext(), SHIFT_OVERLAP_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
                    return 0;
                }
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
    }
}
