package com.skepticalone.mecachecker.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.provider.BaseColumns;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.List;

@Dao
interface CrossCoverDao {

    @WorkerThread
    @Insert
    void insertCrossCoverShift(CrossCoverEntity crossCover);

    @MainThread
    @Query("SELECT * FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE)
    LiveData<List<CrossCoverEntity>> getCrossCoverShifts();

    @MainThread
    @Query("SELECT * FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    LiveData<CrossCoverEntity> getCrossCoverShift(long id);

    @WorkerThread
    @Query("SELECT " + Contract.CrossCoverShifts.COLUMN_NAME_DATE + " FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE +
            " DESC LIMIT 1")
    LocalDate getLastCrossCoverShiftDate();

    @WorkerThread
    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE +
            " = :date WHERE " +
            BaseColumns._ID +
            " = :id")
    void setDate(long id, LocalDate date);

    @WorkerThread
    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_PAYMENT +
            " = :payment WHERE " +
            BaseColumns._ID +
            " = :id")
    void setPayment(long id, BigDecimal payment);

    @WorkerThread
    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_COMMENT +
            " = :comment WHERE " +
            BaseColumns._ID +
            " = :id")
    void setComment(long id, String comment);

    @WorkerThread
    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_CLAIMED +
            " = :claimed WHERE " +
            BaseColumns._ID +
            " = :id")
    void setClaimed(long id, DateTime claimed);

    @WorkerThread
    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_PAID +
            " = :paid WHERE " +
            BaseColumns._ID +
            " = :id")
    void setPaid(long id, DateTime paid);

    @WorkerThread
    @Query("DELETE FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    void deleteCrossCoverShift(long id);

}
