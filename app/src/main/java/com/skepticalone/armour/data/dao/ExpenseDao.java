package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.model.ExpenseEntity;

import org.threeten.bp.Instant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@SuppressWarnings("NullableProblems")
@Dao
public abstract class ExpenseDao extends ItemDao<ExpenseEntity> implements PaymentDao {

    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.Expenses.COLUMN_NAME_TITLE +
            " = :title WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void setTitleSync(long id, @NonNull String title);

    @Override
    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_COMMENT +
            " = :comment WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void setCommentSync(long id, @Nullable String comment);

    @Override
    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_PAYMENT +
            " = :payment WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void setPaymentSync(long id, @NonNull BigDecimal payment);

    @Override
    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_CLAIMED +
            " = :claimed WHERE " +
            BaseColumns._ID +
            " = :id AND " +
            Contract.COLUMN_NAME_PAID +
            " IS NULL")
    public abstract void setClaimedSync(long id, @Nullable Instant claimed);

    @Override
    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_PAID +
            " = :paid WHERE " +
            BaseColumns._ID +
            " = :id AND " +
            Contract.COLUMN_NAME_CLAIMED +
            " IS NOT NULL")
    public abstract void setPaidSync(long id, @Nullable Instant paid);

    @NonNull
    @Override
    @Query("SELECT * FROM " +
            Contract.Expenses.TABLE_NAME +
            " ORDER BY CASE WHEN " +
            Contract.COLUMN_NAME_CLAIMED +
            " IS NULL THEN 2 WHEN " +
            Contract.COLUMN_NAME_PAID +
            " IS NULL THEN 1 ELSE 0 END, coalesce(" +
            Contract.COLUMN_NAME_PAID +
            ", " +
            Contract.COLUMN_NAME_CLAIMED +
            ", " +
            BaseColumns._ID +
            ")")
    public abstract LiveData<List<ExpenseEntity>> fetchItems();

    @NonNull
    @Override
    @Query("SELECT * FROM " +
            Contract.Expenses.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " IN(:ids)")
    abstract List<ExpenseEntity> fetchItemsInternalSync(@NonNull Set<Long> ids);

    public final long insertSync(@NonNull String title){
        return insertSync(ExpenseEntity.from(title));
    }
//
//    @Query("DELETE FROM " +
//            Contract.Expenses.TABLE_NAME +
//            " WHERE " +
//            BaseColumns._ID +
//            " IN(:ids)")
//    @Override
//    public abstract void deleteItemsSync(@NonNull Long[] ids);

}
