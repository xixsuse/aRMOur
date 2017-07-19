package com.skepticalone.mecachecker.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.skepticalone.mecachecker.data.db.Contract;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;

import org.joda.time.DateTime;

import java.util.List;

@Dao
public interface RosteredShiftDao extends ItemDaoContract<RosteredShiftEntity>, ShiftDaoContract {

    @Override
    @Insert
    void insertItemSync(@NonNull RosteredShiftEntity item);

    @NonNull
    @Override
    @Query("SELECT * FROM " + Contract.RosteredShifts.TABLE_NAME + " ORDER BY " + Contract.COLUMN_NAME_SHIFT_START)
    LiveData<List<RosteredShiftEntity>> getItems();

    @NonNull
    @Override
    @Query("SELECT * FROM " +
            Contract.RosteredShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    LiveData<RosteredShiftEntity> getItem(long id);

    @Override
    @Query("SELECT * FROM " +
            Contract.RosteredShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    RosteredShiftEntity getItemSync(long id);

    @Override
    @Query("DELETE FROM " +
            Contract.RosteredShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    int deleteItemSync(long id);

    @Override
    @Query("UPDATE " +
            Contract.RosteredShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_COMMENT +
            " = :comment WHERE " +
            BaseColumns._ID +
            " = :id")
    void setCommentSync(long id, @Nullable String comment);

    @Nullable
    @Override
    @Query("SELECT " + Contract.COLUMN_NAME_SHIFT_END + " FROM " +
            Contract.RosteredShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.COLUMN_NAME_SHIFT_END +
            " DESC LIMIT 1")
    DateTime getLastShiftEndTimeSync();

    @WorkerThread
    @Query("UPDATE " +
            Contract.RosteredShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_SHIFT_START +
            " = :shiftStart, " +
            Contract.COLUMN_NAME_SHIFT_END +
            " = :shiftEnd, " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_START +
            " = :loggedShiftStart, " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_END +
            " = :loggedShiftEnd WHERE " +
            BaseColumns._ID +
            " = :id")
    void setShiftTimesSync(long id, @NonNull DateTime shiftStart, @NonNull DateTime shiftEnd, @Nullable DateTime loggedShiftStart, @Nullable DateTime loggedShiftEnd);

}
