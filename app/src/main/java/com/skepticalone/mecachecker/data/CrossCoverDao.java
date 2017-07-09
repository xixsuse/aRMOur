package com.skepticalone.mecachecker.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;

@Dao
interface CrossCoverDao {
    @Insert
    void insertCrossCoverShift(CrossCoverEntity crossCover);

    @Query("SELECT * FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE)
    LiveData<List<CrossCoverEntity>> getCrossCoverShifts();

    @Query("SELECT * FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    LiveData<CrossCoverEntity> getCrossCoverShift(long id);

    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE +
            " = :date WHERE " +
            BaseColumns._ID +
            " = :id")
    void setDate(long id, @NonNull LocalDate date);

    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.CrossCoverShifts.COLUMN_NAME_CLAIMED +
            " = :claimed WHERE " +
            BaseColumns._ID +
            " = :id")
    void setClaimed(long id, @Nullable DateTime claimed);

    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.CrossCoverShifts.COLUMN_NAME_PAID +
            " = :paid WHERE " +
            BaseColumns._ID +
            " = :id")
    void setPaid(long id, @Nullable DateTime paid);

    @Query("DELETE FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    void deleteCrossCoverShift(long id);

}
