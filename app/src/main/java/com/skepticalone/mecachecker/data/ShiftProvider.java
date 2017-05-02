package com.skepticalone.mecachecker.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public final class ShiftProvider extends ContentProvider {

    private static final String TAG = "ShiftProvider";
    private static final String
            AUTHORITY = "com.skepticalone.mecachecker.provider",
            PROVIDER_TYPE = "/vnd.com.skepticalone.provider.",
            ROSTERED_SHIFTS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + PROVIDER_TYPE + ShiftContract.RosteredShifts.TABLE_NAME,
            ROSTERED_SHIFT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + PROVIDER_TYPE + ShiftContract.RosteredShifts.TABLE_NAME,
            ADDITIONAL_SHIFTS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + PROVIDER_TYPE + ShiftContract.AdditionalShifts.TABLE_NAME,
            ADDITIONAL_SHIFT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + PROVIDER_TYPE + ShiftContract.AdditionalShifts.TABLE_NAME,
            CROSS_COVER_SHIFTS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + PROVIDER_TYPE + ShiftContract.CrossCoverShifts.TABLE_NAME,
            CROSS_COVER_SHIFT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + PROVIDER_TYPE + ShiftContract.CrossCoverShifts.TABLE_NAME;
    private static final Uri baseContentUri = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).build();
    public static final Uri rosteredShiftsUri = baseContentUri.buildUpon().appendPath(ShiftContract.RosteredShifts.TABLE_NAME).build();
    public static final Uri additionalShiftsUri = baseContentUri.buildUpon().appendPath(ShiftContract.AdditionalShifts.TABLE_NAME).build();
    public static final Uri crossCoverShiftsUri = baseContentUri.buildUpon().appendPath(ShiftContract.CrossCoverShifts.TABLE_NAME).build();
    private static final int ROSTERED_SHIFTS = 1;
    private static final int ROSTERED_SHIFT = 2;
    private static final int ADDITIONAL_SHIFTS = 3;
    private static final int ADDITIONAL_SHIFT = 4;
    private static final int CROSS_COVER_SHIFTS = 5;
    private static final int CROSS_COVER_SHIFT = 6;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, ShiftContract.RosteredShifts.TABLE_NAME, ROSTERED_SHIFTS);
        sUriMatcher.addURI(AUTHORITY, ShiftContract.RosteredShifts.TABLE_NAME + "/#", ROSTERED_SHIFT);
        sUriMatcher.addURI(AUTHORITY, ShiftContract.AdditionalShifts.TABLE_NAME, ADDITIONAL_SHIFTS);
        sUriMatcher.addURI(AUTHORITY, ShiftContract.AdditionalShifts.TABLE_NAME + "/#", ADDITIONAL_SHIFT);
        sUriMatcher.addURI(AUTHORITY, ShiftContract.CrossCoverShifts.TABLE_NAME, CROSS_COVER_SHIFTS);
        sUriMatcher.addURI(AUTHORITY, ShiftContract.CrossCoverShifts.TABLE_NAME + "/#", CROSS_COVER_SHIFT);
    }

    private ShiftDbHelper mDbHelper;

    public static Uri rosteredShiftUri(long shiftId) {
        return Uri.withAppendedPath(rosteredShiftsUri, Long.toString(shiftId));
    }

    public static Uri additionalShiftUri(long shiftId) {
        return Uri.withAppendedPath(additionalShiftsUri, Long.toString(shiftId));
    }

    public static Uri crossCoverShiftUri(long shiftId) {
        return Uri.withAppendedPath(crossCoverShiftsUri, Long.toString(shiftId));
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new ShiftDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query() called with: uri: " + uri);
        String table;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ROSTERED_SHIFTS:
            case ROSTERED_SHIFT:
                Cursor cursor = Compliance.getCursor(mDbHelper.getReadableDatabase(), match == ROSTERED_SHIFT ? Long.parseLong(uri.getLastPathSegment()) : null);
                //noinspection ConstantConditions
                cursor.setNotificationUri(getContext().getContentResolver(), ShiftProvider.rosteredShiftsUri);
                return cursor;
            case ADDITIONAL_SHIFT:
                selection = ShiftContract.AdditionalShifts._ID + "=" + uri.getLastPathSegment();
                selectionArgs = null;
                // intentional fallthrough
            case ADDITIONAL_SHIFTS:
                table = ShiftContract.AdditionalShifts.TABLE_NAME;
                break;
            case CROSS_COVER_SHIFT:
                selection = ShiftContract.CrossCoverShifts._ID + "=" + uri.getLastPathSegment();
                selectionArgs = null;
                // intentional fallthrough
            case CROSS_COVER_SHIFTS:
                table = ShiftContract.CrossCoverShifts.TABLE_NAME;
                break;
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
            case ROSTERED_SHIFTS:
                return ROSTERED_SHIFTS_TYPE;
            case ROSTERED_SHIFT:
                return ROSTERED_SHIFT_TYPE;
            case ADDITIONAL_SHIFTS:
                return ADDITIONAL_SHIFTS_TYPE;
            case ADDITIONAL_SHIFT:
                return ADDITIONAL_SHIFT_TYPE;
            case CROSS_COVER_SHIFTS:
                return CROSS_COVER_SHIFTS_TYPE;
            case CROSS_COVER_SHIFT:
                return CROSS_COVER_SHIFT_TYPE;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri newUri;
        switch (sUriMatcher.match(uri)) {
            case ROSTERED_SHIFTS:
                newUri = rosteredShiftUri(mDbHelper.getWritableDatabase().insertOrThrow(ShiftContract.RosteredShifts.TABLE_NAME, null, values));
                break;
            case ADDITIONAL_SHIFTS:
                newUri = additionalShiftUri(mDbHelper.getWritableDatabase().insertOrThrow(ShiftContract.AdditionalShifts.TABLE_NAME, null, values));
                break;
            case CROSS_COVER_SHIFTS:
                newUri = additionalShiftUri(mDbHelper.getWritableDatabase().insertOrThrow(ShiftContract.CrossCoverShifts.TABLE_NAME, null, values));
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(uri, null);
        return newUri;

    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        String tableName;
        switch (sUriMatcher.match(uri)) {
            case ROSTERED_SHIFT:
                tableName = ShiftContract.RosteredShifts.TABLE_NAME;
                break;
            case ADDITIONAL_SHIFT:
                tableName = ShiftContract.AdditionalShifts.TABLE_NAME;
                break;
            case CROSS_COVER_SHIFT:
                tableName = ShiftContract.CrossCoverShifts.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
        int deleted = mDbHelper.getWritableDatabase().delete(tableName, BaseColumns._ID + "=" + uri.getLastPathSegment(), null);
        if (deleted > 0) {
            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName;
        switch (sUriMatcher.match(uri)) {
            case ROSTERED_SHIFT:
                tableName = ShiftContract.RosteredShifts.TABLE_NAME;
                break;
            case ADDITIONAL_SHIFT:
                tableName = ShiftContract.AdditionalShifts.TABLE_NAME;
                break;
            case CROSS_COVER_SHIFT:
                tableName = ShiftContract.CrossCoverShifts.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
        try {
            int updated = mDbHelper.getWritableDatabase().update(tableName, values, BaseColumns._ID + "=" + uri.getLastPathSegment(), null);
            if (updated > 0) {
                //noinspection ConstantConditions
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return updated;
        } catch (SQLiteConstraintException e) {
            return 0;
        }
    }

}
