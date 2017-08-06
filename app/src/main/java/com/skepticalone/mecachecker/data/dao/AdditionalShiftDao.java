//package com.skepticalone.mecachecker.data.dao;
//
//import android.arch.lifecycle.LiveData;
//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Delete;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.Query;
//import android.provider.BaseColumns;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.annotation.WorkerThread;
//
//import com.skepticalone.mecachecker.data.db.Contract;
//import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
//
//import org.joda.time.DateTime;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@SuppressWarnings("NullableProblems")
//@Dao
//public interface AdditionalShiftDao extends PayableDaoContract<AdditionalShiftEntity> {
//
//    @Override
//    @Insert
//    long insertItemSync(@NonNull AdditionalShiftEntity shift);
//
//    @NonNull
//    @Override
//    @Query("SELECT * FROM " + Contract.AdditionalShifts.TABLE_NAME + " ORDER BY " + Contract.COLUMN_NAME_SHIFT_START)
//    LiveData<List<AdditionalShiftEntity>> getItems();
//
//    @NonNull
//    @Override
//    @Query("SELECT * FROM " +
//            Contract.AdditionalShifts.TABLE_NAME +
//            " WHERE " +
//            BaseColumns._ID +
//            " = :id")
//    LiveData<AdditionalShiftEntity> getItem(long id);
//
//    @Nullable
//    @Override
//    @Query("SELECT * FROM " +
//            Contract.AdditionalShifts.TABLE_NAME +
//            " WHERE " +
//            BaseColumns._ID +
//            " = :id")
//    AdditionalShiftEntity getItemSync(long id);
//
//    @Override
//    @Delete
//    int deleteItemSync(@NonNull AdditionalShiftEntity shift);
//
//    @Override
//    @Query("UPDATE " +
//            Contract.AdditionalShifts.TABLE_NAME +
//            " SET " +
//            Contract.COLUMN_NAME_COMMENT +
//            " = :comment WHERE " +
//            BaseColumns._ID +
//            " = :id")
//    void setCommentSync(long id, @Nullable String comment);
//
//    @Override
//    @Query("UPDATE " +
//            Contract.AdditionalShifts.TABLE_NAME +
//            " SET " +
//            Contract.COLUMN_NAME_PAYMENT +
//            " = :payment WHERE " +
//            BaseColumns._ID +
//            " = :id")
//    void setPaymentSync(long id, @NonNull BigDecimal payment);
//
//    @Override
//    @Query("UPDATE " +
//            Contract.AdditionalShifts.TABLE_NAME +
//            " SET " +
//            Contract.COLUMN_NAME_CLAIMED +
//            " = :claimed WHERE " +
//            BaseColumns._ID +
//            " = :id")
//    void setClaimedSync(long id, @Nullable DateTime claimed);
//
//    @Override
//    @Query("UPDATE " +
//            Contract.AdditionalShifts.TABLE_NAME +
//            " SET " +
//            Contract.COLUMN_NAME_PAID +
//            " = :paid WHERE " +
//            BaseColumns._ID +
//            " = :id")
//    void setPaidSync(long id, @Nullable DateTime paid);
//
//    @Nullable
//    @WorkerThread
//    @Query("SELECT " + Contract.COLUMN_NAME_SHIFT_END + " FROM " +
//            Contract.AdditionalShifts.TABLE_NAME +
//            " ORDER BY " +
//            Contract.COLUMN_NAME_SHIFT_END +
//            " DESC LIMIT 1")
//    DateTime getLastShiftEndSync();
//
//    @WorkerThread
//    @Query("UPDATE " +
//            Contract.AdditionalShifts.TABLE_NAME +
//            " SET " +
//            Contract.COLUMN_NAME_SHIFT_START +
//            " = :start, " +
//            Contract.COLUMN_NAME_SHIFT_END +
//            " = :end WHERE " +
//            BaseColumns._ID +
//            " = :id")
//    void setShiftTimesSync(long id, @NonNull DateTime start, @NonNull DateTime end);
//
//}
