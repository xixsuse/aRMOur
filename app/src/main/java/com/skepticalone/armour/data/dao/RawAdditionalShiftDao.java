package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteQueryBuilder;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.model.RawAdditionalShiftEntity;
import com.skepticalone.armour.data.model.RawShift;
import com.skepticalone.armour.data.util.InstantConverter;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

import java.util.List;

@Dao
public abstract class RawAdditionalShiftDao extends ItemDao<RawAdditionalShiftEntity> {

    @NonNull
    private final PayableDaoHelper payableDaoHelper;

    RawAdditionalShiftDao(@NonNull AppDatabase database) {
        super(database);
        payableDaoHelper = new PayableDaoHelper(this);
    }

    @NonNull
    public final PayableDaoHelper getPayableDaoHelper() {
        return payableDaoHelper;
    }

    public final void setTimesSync(long id, @NonNull Instant start, @NonNull Instant end) {
        ContentValues values = new ContentValues();
        values.put(Contract.COLUMN_NAME_SHIFT_START, start.getEpochSecond());
        values.put(Contract.COLUMN_NAME_SHIFT_END, end.getEpochSecond());
        updateInTransaction(id, values);
    }

    @NonNull
    @Override
    final String getTableName() {
        return Contract.AdditionalShifts.TABLE_NAME;
    }

    synchronized public final long insertSync(@NonNull Pair<LocalTime, LocalTime> times, @NonNull ZoneId zoneId, int paymentInCents) {
        Cursor cursor = getDatabase().getOpenHelper().getReadableDatabase().query(
                SupportSQLiteQueryBuilder.builder(getTableName())
                        .columns(new String[]{Contract.COLUMN_NAME_SHIFT_END})
                        .orderBy(Contract.COLUMN_NAME_SHIFT_END + " DESC")
                        .limit("1")
                        .create()
        );
        Instant lastShiftEnd = cursor.moveToFirst() ? InstantConverter.epochSecondToInstant(cursor.getLong(0)) : null;
        cursor.close();
        ContentValues values = new ContentValues();
        values.put(Contract.COLUMN_NAME_PAYMENT, paymentInCents);
        RawShift.RawShiftData rawShiftData = RawShift.RawShiftData.withEarliestStart(times.first, times.second, lastShiftEnd, zoneId, false);
        values.put(Contract.COLUMN_NAME_SHIFT_START, rawShiftData.getStart().getEpochSecond());
        values.put(Contract.COLUMN_NAME_SHIFT_END, rawShiftData.getEnd().getEpochSecond());
        return insertInTransaction(values);
    }

    // FIXME: 22/09/17 
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Override
    @Query("SELECT * FROM " +
            Contract.AdditionalShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract LiveData<RawAdditionalShiftEntity> getItem(long id);

    // FIXME: 22/09/17 
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Override
    @Query("SELECT * FROM " +
            Contract.AdditionalShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    abstract RawAdditionalShiftEntity getItemInternalSync(long id);

    // FIXME: 22/09/17 
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Override
    @Query("SELECT * FROM " + Contract.AdditionalShifts.TABLE_NAME + " ORDER BY " + Contract.COLUMN_NAME_SHIFT_START)
    public abstract LiveData<List<RawAdditionalShiftEntity>> getItems();

}
