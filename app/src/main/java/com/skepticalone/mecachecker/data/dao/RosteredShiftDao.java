package com.skepticalone.mecachecker.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
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

@SuppressWarnings("ALL")
@Dao
public interface RosteredShiftDao extends ItemDaoContract<RosteredShiftEntity> {

    @Override
    @Insert
    long insertItemSync(@NonNull RosteredShiftEntity item);

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

    @Nullable
    @Override
    @Query("SELECT * FROM " +
            Contract.RosteredShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    RosteredShiftEntity getItemSync(long id);

    @Override
    @Delete
    int deleteItemSync(@NonNull RosteredShiftEntity item);

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
    @WorkerThread
    @Query("SELECT " + Contract.COLUMN_NAME_SHIFT_END + " FROM " +
            Contract.RosteredShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.COLUMN_NAME_SHIFT_END +
            " DESC LIMIT 1")
    DateTime getLastShiftEndSync();

    @WorkerThread
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
    void setShiftTimesSync(long id, @NonNull DateTime start, @NonNull DateTime end, @Nullable DateTime loggedStart, @Nullable DateTime loggedEnd);

    @WorkerThread
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
    void switchOnLogSync(long id);

    @WorkerThread
    @Query("UPDATE " +
            Contract.RosteredShifts.TABLE_NAME +
            " SET " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_START +
            " = NULL, " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_END +
            " = NULL WHERE " +
            BaseColumns._ID +
            " = :id")
    void switchOffLogSync(long id);

}
