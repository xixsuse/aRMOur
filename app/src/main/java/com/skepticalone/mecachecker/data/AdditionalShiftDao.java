package com.skepticalone.mecachecker.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.provider.BaseColumns;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

@Dao
interface AdditionalShiftDao {
    @WorkerThread
    @Insert
    void insertAdditionalShift(AdditionalShiftEntity additionalShift);

    @MainThread
    @Query("SELECT * FROM " + Contract.AdditionalShifts.TABLE_NAME + " ORDER BY " + Contract.COLUMN_NAME_SHIFT_START)
    LiveData<List<AdditionalShiftEntity>> getAdditionalShifts();

    @MainThread
    @Query("SELECT * FROM " +
            Contract.AdditionalShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    LiveData<AdditionalShiftEntity> getAdditionalShift(long id);

    @Nullable
    @WorkerThread
    @Query("SELECT " + Contract.COLUMN_NAME_SHIFT_END + " FROM " +
            Contract.AdditionalShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.COLUMN_NAME_SHIFT_END +
            " DESC LIMIT 1")
    DateTime getLastAdditionalShiftDate();

    @WorkerThread
    @Query("UPDATE " +
            Contract.AdditionalShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_SHIFT_START +
            " = :shiftStart, " +
            Contract.COLUMN_NAME_SHIFT_END +
            " = :shiftEnd WHERE " +
            BaseColumns._ID +
            " = :id")
    void setTimes(long id, @NonNull DateTime shiftStart, @NonNull DateTime shiftEnd);

    @WorkerThread
    @Query("UPDATE " +
            Contract.AdditionalShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_PAYMENT +
            " = :payment WHERE " +
            BaseColumns._ID +
            " = :id")
    void setPayment(long id, BigDecimal payment);

    @WorkerThread
    @Query("UPDATE " +
            Contract.AdditionalShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_COMMENT +
            " = :comment WHERE " +
            BaseColumns._ID +
            " = :id")
    void setComment(long id, String comment);

    @WorkerThread
    @Query("UPDATE " +
            Contract.AdditionalShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_CLAIMED +
            " = :claimed WHERE " +
            BaseColumns._ID +
            " = :id")
    void setClaimed(long id, DateTime claimed);

    @WorkerThread
    @Query("UPDATE " +
            Contract.AdditionalShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_PAID +
            " = :paid WHERE " +
            BaseColumns._ID +
            " = :id")
    void setPaid(long id, DateTime paid);

    @WorkerThread
    @Query("DELETE FROM " +
            Contract.AdditionalShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    void deleteAdditionalShift(long id);


}
