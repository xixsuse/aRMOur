package com.skepticalone.mecachecker.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public final class ShiftProvider extends ContentProvider {

    private static final String AUTHORITY = "com.skepticalone.mecachecker.provider";
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

    @Override
    public boolean onCreate() {
        mDbHelper = new ShiftDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case SHIFTS:
                sortOrder = ShiftContract.Shift.COLUMN_NAME_START;
                break;
            case SHIFT_ID:
                selection = ShiftContract.Shift._ID + "=" + uri.getLastPathSegment();
                selectionArgs = null;
                sortOrder = null;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        Cursor cursor = mDbHelper.getReadableDatabase().query(ShiftContract.Shift.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        Context context = getContext();
        if (context != null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case SHIFTS:
                long shiftId = mDbHelper.getWritableDatabase().insertOrThrow(ShiftContract.Shift.TABLE_NAME, null, values);
                Context context = getContext();
                if (context != null) {
                    context.getContentResolver().notifyChange(uri, null);
                }
                return shiftUri(shiftId);
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case SHIFT_ID:
                int deleted = mDbHelper.getWritableDatabase().delete(ShiftContract.Shift.TABLE_NAME, ShiftContract.Shift._ID + "=" + uri.getLastPathSegment(), null);
                if (deleted > 0) {
                    Context context = getContext();
                    if (context != null) {
                        context.getContentResolver().notifyChange(uri, null);
                    }
                }
                return deleted;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}
