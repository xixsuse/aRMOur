package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteQueryBuilder;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.entity.RosteredShiftEntity;
import com.skepticalone.armour.data.entity.ShiftData;
import com.skepticalone.armour.data.util.InstantConverter;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

import java.util.List;

@Dao
public abstract class RosteredShiftDao extends ItemDao<RosteredShiftEntity> {

    RosteredShiftDao(@NonNull AppDatabase database) {
        super(database);
    }

    @SuppressWarnings("NullableProblems")
    @Query("UPDATE " +
            Contract.RosteredShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_SHIFT_START +
            " = :start, " +
            Contract.COLUMN_NAME_SHIFT_END +
            " = :end, " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_START +
            " = :loggedStart, " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_END +
            " = :loggedEnd WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void setShiftTimesSync(long id, @NonNull Instant start, @NonNull Instant end, @Nullable Instant loggedStart, @Nullable Instant loggedEnd);

    synchronized public final long insertSync(@NonNull Pair<LocalTime, LocalTime> times, @NonNull ZoneId zoneId, boolean skipWeekends) {

        Cursor cursor = getDatabase().getOpenHelper().getReadableDatabase().query(
                SupportSQLiteQueryBuilder.builder(getTableName())
                        .columns(new String[]{Contract.COLUMN_NAME_SHIFT_END})
                        .orderBy(Contract.COLUMN_NAME_SHIFT_END + " DESC")
                        .limit("1")
                        .create()
        );
        Instant lastShiftEnd = cursor.moveToFirst() ? InstantConverter.epochSecondToInstant(cursor.getLong(0)) : null;
        cursor.close();
        ShiftData shiftData = ShiftData.withEarliestStart(times.first, times.second, lastShiftEnd, zoneId, skipWeekends);
        ContentValues values = new ContentValues();
        values.put(Contract.COLUMN_NAME_SHIFT_START, shiftData.getStart().getEpochSecond());
        values.put(Contract.COLUMN_NAME_SHIFT_END, shiftData.getEnd().getEpochSecond());
        return insertInTransaction(values);
    }


    @Query("UPDATE " +
            Contract.RosteredShifts.TABLE_NAME +
            " SET " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_START +
            " = " +
            Contract.COLUMN_NAME_SHIFT_START +
            ", " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_END +
            " = " +
            Contract.COLUMN_NAME_SHIFT_END +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void switchOnLogSync(long id);

    @Query("UPDATE " +
            Contract.RosteredShifts.TABLE_NAME +
            " SET " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_START +
            " = NULL, " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_END +
            " = NULL WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void switchOffLogSync(long id);

    @Override
    @Query("SELECT * FROM " +
            Contract.RosteredShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract LiveData<RosteredShiftEntity> getItem(long id);

    @Override
    @Query("SELECT * FROM " +
            Contract.RosteredShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    abstract RosteredShiftEntity getItemInternalSync(long id);

    @NonNull
    @Override
    final String getTableName() {
        return Contract.RosteredShifts.TABLE_NAME;
    }

    @Override
    @Query("SELECT * FROM " + Contract.RosteredShifts.TABLE_NAME + " ORDER BY " + Contract.COLUMN_NAME_SHIFT_START)
    public abstract LiveData<List<RosteredShiftEntity>> getItems();
}
