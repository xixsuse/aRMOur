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

public final class Provider extends ContentProvider {

    private static final String TAG = "Provider";
    private static final String
            AUTHORITY = "com.skepticalone.mecachecker.provider",
            PROVIDER_TYPE = "/vnd.com.skepticalone.provider.",
            ROSTERED_SHIFTS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + PROVIDER_TYPE + Contract.RosteredShifts.TABLE_NAME,
            ROSTERED_SHIFT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + PROVIDER_TYPE + Contract.RosteredShifts.TABLE_NAME,
            ADDITIONAL_SHIFTS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + PROVIDER_TYPE + Contract.AdditionalShifts.TABLE_NAME,
            ADDITIONAL_SHIFT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + PROVIDER_TYPE + Contract.AdditionalShifts.TABLE_NAME,
            CROSS_COVER_SHIFTS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + PROVIDER_TYPE + Contract.CrossCoverShifts.TABLE_NAME,
            CROSS_COVER_SHIFT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + PROVIDER_TYPE + Contract.CrossCoverShifts.TABLE_NAME,
            EXPENSES_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + PROVIDER_TYPE + Contract.Expenses.TABLE_NAME,
            EXPENSE_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + PROVIDER_TYPE + Contract.Expenses.TABLE_NAME;
    private static final Uri baseContentUri = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).build();
    public static final Uri
            rosteredShiftsUri = baseContentUri.buildUpon().appendPath(Contract.RosteredShifts.TABLE_NAME).build(),
            rosteredShiftsWithComplianceUri = rosteredShiftsUri.buildUpon().appendPath("compliance").build(),
            additionalShiftsUri = baseContentUri.buildUpon().appendPath(Contract.AdditionalShifts.TABLE_NAME).build(),
            crossCoverShiftsUri = baseContentUri.buildUpon().appendPath(Contract.CrossCoverShifts.TABLE_NAME).build(),
            expensesUri = baseContentUri.buildUpon().appendPath(Contract.Expenses.TABLE_NAME).build(),
            additionalShiftsDistinctCommentsUri = additionalShiftsUri.buildUpon().appendPath("distinct").build(),
            crossCoverShiftsDistinctCommentsUri = crossCoverShiftsUri.buildUpon().appendPath("distinct").build();
    private static final int
            ROSTERED_SHIFTS = 1,
            ROSTERED_SHIFT = 2,
            ROSTERED_SHIFTS_WITH_COMPLIANCE = 3,
            ROSTERED_SHIFT_WITH_COMPLIANCE = 4,
            ADDITIONAL_SHIFTS = 5,
            ADDITIONAL_SHIFTS_DISTINCT_COMMENTS = 6,
            ADDITIONAL_SHIFT = 7,
            CROSS_COVER_SHIFTS = 8,
            CROSS_COVER_SHIFTS_DISTINCT_COMMENTS = 9,
            CROSS_COVER_SHIFT = 10,
            EXPENSES = 11,
            EXPENSE = 12;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, Contract.RosteredShifts.TABLE_NAME, ROSTERED_SHIFTS);
        sUriMatcher.addURI(AUTHORITY, Contract.RosteredShifts.TABLE_NAME + "/#", ROSTERED_SHIFT);
        sUriMatcher.addURI(AUTHORITY, Contract.RosteredShifts.TABLE_NAME + "/compliance", ROSTERED_SHIFTS_WITH_COMPLIANCE);
        sUriMatcher.addURI(AUTHORITY, Contract.RosteredShifts.TABLE_NAME + "/compliance/#", ROSTERED_SHIFT_WITH_COMPLIANCE);
        sUriMatcher.addURI(AUTHORITY, Contract.AdditionalShifts.TABLE_NAME, ADDITIONAL_SHIFTS);
        sUriMatcher.addURI(AUTHORITY, Contract.AdditionalShifts.TABLE_NAME + "/distinct", ADDITIONAL_SHIFTS_DISTINCT_COMMENTS);
        sUriMatcher.addURI(AUTHORITY, Contract.AdditionalShifts.TABLE_NAME + "/#", ADDITIONAL_SHIFT);
        sUriMatcher.addURI(AUTHORITY, Contract.CrossCoverShifts.TABLE_NAME, CROSS_COVER_SHIFTS);
        sUriMatcher.addURI(AUTHORITY, Contract.CrossCoverShifts.TABLE_NAME + "/distinct", CROSS_COVER_SHIFTS_DISTINCT_COMMENTS);
        sUriMatcher.addURI(AUTHORITY, Contract.CrossCoverShifts.TABLE_NAME + "/#", CROSS_COVER_SHIFT);
        sUriMatcher.addURI(AUTHORITY, Contract.Expenses.TABLE_NAME, EXPENSES);
        sUriMatcher.addURI(AUTHORITY, Contract.Expenses.TABLE_NAME + "/#", EXPENSE);
    }

    private DbHelper mDbHelper;

    public static Uri rosteredShiftUri(long shiftId) {
        return Uri.withAppendedPath(rosteredShiftsUri, Long.toString(shiftId));
    }

    public static Uri rosteredShiftWithComplianceUri(long shiftId) {
        return Uri.withAppendedPath(rosteredShiftsWithComplianceUri, Long.toString(shiftId));
    }

    private static Uri rosteredShiftWithComplianceUri(Uri rosteredShiftUri) {
        return rosteredShiftWithComplianceUri(Long.valueOf(rosteredShiftUri.getLastPathSegment()));
    }

    public static Uri additionalShiftUri(long shiftId) {
        return Uri.withAppendedPath(additionalShiftsUri, Long.toString(shiftId));
    }

    public static Uri crossCoverShiftUri(long shiftId) {
        return Uri.withAppendedPath(crossCoverShiftsUri, Long.toString(shiftId));
    }

    public static Uri expenseUri(long id) {
        return Uri.withAppendedPath(expensesUri, Long.toString(id));
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query() called with: uri: " + uri);
        String table;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ROSTERED_SHIFTS_WITH_COMPLIANCE:
            case ROSTERED_SHIFT_WITH_COMPLIANCE:
                Cursor cursor = Compliance.getCursor(mDbHelper.getReadableDatabase(), match == ROSTERED_SHIFT_WITH_COMPLIANCE ? Long.parseLong(uri.getLastPathSegment()) : null);
                //noinspection ConstantConditions
                cursor.setNotificationUri(getContext().getContentResolver(), Provider.rosteredShiftsWithComplianceUri);
                return cursor;
            case ROSTERED_SHIFT:
                selection = Contract.RosteredShifts._ID + "=" + uri.getLastPathSegment();
                selectionArgs = null;
                // intentional fallthrough
            case ROSTERED_SHIFTS:
                table = Contract.RosteredShifts.TABLE_NAME;
                break;
            case ADDITIONAL_SHIFT:
                selection = Contract.AdditionalShifts._ID + "=" + uri.getLastPathSegment();
                selectionArgs = null;
                // intentional fallthrough
            case ADDITIONAL_SHIFTS:
            case ADDITIONAL_SHIFTS_DISTINCT_COMMENTS:
                table = Contract.AdditionalShifts.TABLE_NAME;
                break;
            case CROSS_COVER_SHIFT:
                selection = Contract.CrossCoverShifts._ID + "=" + uri.getLastPathSegment();
                selectionArgs = null;
                // intentional fallthrough
            case CROSS_COVER_SHIFTS:
            case CROSS_COVER_SHIFTS_DISTINCT_COMMENTS:
                table = Contract.CrossCoverShifts.TABLE_NAME;
                break;
            case EXPENSE:
                selection = Contract.Expenses._ID + "=" + uri.getLastPathSegment();
                selectionArgs = null;
                // intentional fallthrough
            case EXPENSES:
                table = Contract.Expenses.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
        Cursor cursor = mDbHelper.getReadableDatabase().query(
                match == ADDITIONAL_SHIFTS_DISTINCT_COMMENTS || match == CROSS_COVER_SHIFTS_DISTINCT_COMMENTS,
                table,
                projection,
                selection,
                selectionArgs,
                match == ADDITIONAL_SHIFTS_DISTINCT_COMMENTS ? Contract.AdditionalShifts.COLUMN_NAME_COMMENT : match == CROSS_COVER_SHIFTS_DISTINCT_COMMENTS ? Contract.CrossCoverShifts.COLUMN_NAME_COMMENT : null,
                null,
                sortOrder,
                null
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
            case ROSTERED_SHIFTS_WITH_COMPLIANCE:
                return ROSTERED_SHIFTS_TYPE;
            case ROSTERED_SHIFT:
            case ROSTERED_SHIFT_WITH_COMPLIANCE:
                return ROSTERED_SHIFT_TYPE;
            case ADDITIONAL_SHIFTS:
            case ADDITIONAL_SHIFTS_DISTINCT_COMMENTS:
                return ADDITIONAL_SHIFTS_TYPE;
            case ADDITIONAL_SHIFT:
                return ADDITIONAL_SHIFT_TYPE;
            case CROSS_COVER_SHIFTS:
            case CROSS_COVER_SHIFTS_DISTINCT_COMMENTS:
                return CROSS_COVER_SHIFTS_TYPE;
            case CROSS_COVER_SHIFT:
                return CROSS_COVER_SHIFT_TYPE;
            case EXPENSES:
                return EXPENSES_TYPE;
            case EXPENSE:
                return EXPENSE_TYPE;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri newUri;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ROSTERED_SHIFTS:
                newUri = rosteredShiftUri(mDbHelper.getWritableDatabase().insert(Contract.RosteredShifts.TABLE_NAME, null, values));
                break;
            case ADDITIONAL_SHIFTS:
                newUri = additionalShiftUri(mDbHelper.getWritableDatabase().insert(Contract.AdditionalShifts.TABLE_NAME, null, values));
                break;
            case CROSS_COVER_SHIFTS:
                newUri = additionalShiftUri(mDbHelper.getWritableDatabase().insert(Contract.CrossCoverShifts.TABLE_NAME, null, values));
                break;
            case EXPENSES:
                newUri = expenseUri(mDbHelper.getWritableDatabase().insertOrThrow(Contract.Expenses.TABLE_NAME, null, values));
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(uri, null);
        if (match == ROSTERED_SHIFTS) {
            getContext().getContentResolver().notifyChange(rosteredShiftsWithComplianceUri, null);
        }
        return newUri;

    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        String tableName;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ROSTERED_SHIFT:
                tableName = Contract.RosteredShifts.TABLE_NAME;
                break;
            case ADDITIONAL_SHIFT:
                tableName = Contract.AdditionalShifts.TABLE_NAME;
                break;
            case CROSS_COVER_SHIFT:
                tableName = Contract.CrossCoverShifts.TABLE_NAME;
                break;
            case EXPENSE:
                tableName = Contract.Expenses.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
        int deleted = mDbHelper.getWritableDatabase().delete(tableName, BaseColumns._ID + "=" + uri.getLastPathSegment(), null);
        if (deleted > 0) {
            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
            if (match == ROSTERED_SHIFT) {
                getContext().getContentResolver().notifyChange(rosteredShiftWithComplianceUri(uri), null);
            }
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ROSTERED_SHIFT:
                tableName = Contract.RosteredShifts.TABLE_NAME;
                break;
            case ADDITIONAL_SHIFT:
                tableName = Contract.AdditionalShifts.TABLE_NAME;
                break;
            case CROSS_COVER_SHIFT:
                tableName = Contract.CrossCoverShifts.TABLE_NAME;
                break;
            case EXPENSE:
                tableName = Contract.Expenses.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
        try {
            int updated = mDbHelper.getWritableDatabase().update(tableName, values, BaseColumns._ID + "=" + uri.getLastPathSegment(), null);
            if (updated > 0) {
                //noinspection ConstantConditions
                getContext().getContentResolver().notifyChange(uri, null);
                if (match == ROSTERED_SHIFT) {
                    getContext().getContentResolver().notifyChange(rosteredShiftWithComplianceUri(uri), null);
                }
            }
            return updated;
        } catch (SQLiteConstraintException e) {
            return 0;
        }
    }

}
