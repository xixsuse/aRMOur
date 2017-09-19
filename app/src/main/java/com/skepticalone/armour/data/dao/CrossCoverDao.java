package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.entity.CrossCoverEntity;
import com.skepticalone.armour.data.util.LocalDateConverter;

import org.threeten.bp.LocalDate;

import java.util.List;

@Dao
public abstract class CrossCoverDao extends ItemDao<CrossCoverEntity> {

    @NonNull
    private static final String GET_LAST_SHIFT_DATE =
            "SELECT " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE +
            " FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE +
            " DESC LIMIT 1";
    @NonNull
    private final PayableDaoHelper payableDaoHelper;

    CrossCoverDao(@NonNull AppDatabase database) {
        super(database);
        payableDaoHelper = new PayableDaoHelper(this);
    }

    @NonNull
    public final PayableDaoHelper getPayableDaoHelper() {
        return payableDaoHelper;
    }

    public final void setDateSync(long id, @NonNull LocalDate date) {
        SupportSQLiteStatement setDateStatement = getUpdateStatement(Contract.CrossCoverShifts.COLUMN_NAME_DATE);
        setDateStatement.bindLong(1, LocalDateConverter.dateToEpochDay(date));
        setDateStatement.bindLong(2, id);
        updateInTransaction(setDateStatement);
    }

    @NonNull
    @Override
    final String getTableName() {
        return Contract.CrossCoverShifts.TABLE_NAME;
    }

    synchronized public final long insertSync(int paymentInCents){
        SupportSQLiteStatement insertStatement = getDatabase().compileStatement("INSERT INTO " +
                Contract.CrossCoverShifts.TABLE_NAME +
                " (" +
                Contract.COLUMN_NAME_PAYMENT +
                ", " +
                Contract.CrossCoverShifts.COLUMN_NAME_DATE +
                ") VALUES (?,?)");
        insertStatement.bindLong(1, paymentInCents);
        Cursor cursor = getDatabase().query(GET_LAST_SHIFT_DATE, null);
        @Nullable final LocalDate lastDate = cursor.moveToFirst() ? LocalDateConverter.epochDayToDate(cursor.getLong(0)) : null;
        cursor.close();
        insertStatement.bindLong(2, LocalDateConverter.dateToEpochDay(CrossCoverEntity.getNewDate(lastDate)));
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
            Contract.CrossCoverShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract LiveData<CrossCoverEntity> getItem(long id);

    @Override
    @Query("SELECT * FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    abstract CrossCoverEntity getItemInternalSync(long id);

    @Override
    @Query("SELECT * FROM " + Contract.CrossCoverShifts.TABLE_NAME + " ORDER BY " + Contract.CrossCoverShifts.COLUMN_NAME_DATE)
    public abstract LiveData<List<CrossCoverEntity>> getItems();

}
