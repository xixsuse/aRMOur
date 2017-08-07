package com.skepticalone.mecachecker.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.db.Contract;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.util.DateTimeConverter;
import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.List;

@Dao
public abstract class AdditionalShiftDao extends ItemDao<AdditionalShiftEntity> {

    @NonNull
    private final PayableDaoHelper payableDaoHelper;

    @NonNull
    private static final String GET_LAST_SHIFT_END =
            "SELECT " + Contract.COLUMN_NAME_SHIFT_END + " FROM " +
            Contract.AdditionalShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.COLUMN_NAME_SHIFT_END +
            " DESC LIMIT 1";

    AdditionalShiftDao(@NonNull AppDatabase database) {
        super(database);
        payableDaoHelper = new PayableDaoHelper(this);
    }

    @NonNull
    public final PayableDaoHelper getPayableDaoHelper() {
        return payableDaoHelper;
    }

    public final void setTimesSync(long id, @NonNull DateTime start, @NonNull DateTime end) {
        SupportSQLiteStatement setTimesStatement = getDatabase().compileStatement("UPDATE " +
                Contract.AdditionalShifts.TABLE_NAME +
                " SET " +
                Contract.COLUMN_NAME_SHIFT_START +
                " = ?, " +
                Contract.COLUMN_NAME_SHIFT_END +
                " = ? WHERE " +
                BaseColumns._ID +
                " = ?");
        setTimesStatement.bindLong(1, start.getMillis());
        setTimesStatement.bindLong(2, end.getMillis());
        setTimesStatement.bindLong(3, id);
        updateInTransaction(setTimesStatement);
    }

    @NonNull
    @Override
    final String getTableName() {
        return Contract.AdditionalShifts.TABLE_NAME;
    }

    synchronized public final long insertSync(@NonNull LocalTime startTime, @NonNull LocalTime endTime, int paymentInCents){
        SupportSQLiteStatement insertStatement = getDatabase().compileStatement("INSERT INTO " +
                Contract.AdditionalShifts.TABLE_NAME +
                " (" +
                Contract.COLUMN_NAME_PAYMENT +
                ", " +
                Contract.COLUMN_NAME_SHIFT_START +
                ", " +
                Contract.COLUMN_NAME_SHIFT_END +
                ") VALUES (?,?,?)");
        insertStatement.bindLong(1, paymentInCents);
        Cursor cursor = getDatabase().query(GET_LAST_SHIFT_END, null);
        @Nullable final DateTime lastShiftEnd = cursor.moveToFirst() ? DateTimeConverter.millisToDateTime(cursor.getLong(0)) : null;
        cursor.close();
        ShiftData shiftData = ShiftData.withEarliestStart(startTime, endTime, lastShiftEnd, false);
        insertStatement.bindLong(2, shiftData.getStart().getMillis());
        insertStatement.bindLong(3, shiftData.getEnd().getMillis());
        getDatabase().beginTransaction();
        try {
            long id = insertStatement.executeInsert();
            getDatabase().setTransactionSuccessful();
            return id;
        } finally {
            getDatabase().endTransaction();
        }
    }

    @Override
    @Query("SELECT * FROM " +
            Contract.AdditionalShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract LiveData<AdditionalShiftEntity> getItem(long id);

    @Override
    @Query("SELECT * FROM " +
            Contract.AdditionalShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    abstract AdditionalShiftEntity getItemInternalSync(long id);

    @Override
    @Query("SELECT * FROM " + Contract.AdditionalShifts.TABLE_NAME + " ORDER BY " + Contract.COLUMN_NAME_SHIFT_START)
    public abstract LiveData<List<AdditionalShiftEntity>> getItems();

}
