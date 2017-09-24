package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteQueryBuilder;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.model.RawCrossCoverEntity;
import com.skepticalone.armour.data.util.LocalDateConverter;

import org.threeten.bp.LocalDate;

import java.util.List;

@Dao
public abstract class CrossCoverDao extends ItemDao<RawCrossCoverEntity> {

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
        ContentValues values = new ContentValues();
        values.put(Contract.CrossCoverShifts.COLUMN_NAME_DATE, LocalDateConverter.dateToEpochDay(date));
        updateInTransaction(id, values);
    }

    @NonNull
    @Override
    final String getTableName() {
        return Contract.CrossCoverShifts.TABLE_NAME;
    }

    synchronized public final long insertSync(int paymentInCents){
        Cursor cursor = getDatabase().getOpenHelper().getReadableDatabase().query(
                SupportSQLiteQueryBuilder.builder(getTableName())
                        .columns(new String[]{Contract.CrossCoverShifts.COLUMN_NAME_DATE})
                        .orderBy(Contract.CrossCoverShifts.COLUMN_NAME_DATE + " DESC")
                        .limit("1")
                        .create()
        );
        LocalDate lastDate = cursor.moveToFirst() ? LocalDateConverter.epochDayToDate(cursor.getLong(0)) : null;
        cursor.close();
        ContentValues values = new ContentValues();
        values.put(Contract.COLUMN_NAME_PAYMENT, paymentInCents);
        values.put(Contract.CrossCoverShifts.COLUMN_NAME_DATE, LocalDateConverter.dateToEpochDay(RawCrossCoverEntity.getNewDate(lastDate)));
        return insertInTransaction(values);
    }

    @Override
    @Query("SELECT * FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract LiveData<RawCrossCoverEntity> getItem(long id);

    @Override
    @Query("SELECT * FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    abstract RawCrossCoverEntity getItemInternalSync(long id);

    @Override
    @Query("SELECT * FROM " + Contract.CrossCoverShifts.TABLE_NAME + " ORDER BY " + Contract.CrossCoverShifts.COLUMN_NAME_DATE)
    public abstract LiveData<List<RawCrossCoverEntity>> getItems();

}
