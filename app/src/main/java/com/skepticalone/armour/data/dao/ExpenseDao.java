package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.model.RawExpenseEntity;

import org.threeten.bp.Instant;

import java.math.BigDecimal;
import java.util.List;

@Dao
public abstract class ExpenseDao extends ItemDao<RawExpenseEntity> implements PayableDao {

    ExpenseDao(@NonNull AppDatabase database) {
        super(database);
    }

    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.Expenses.COLUMN_NAME_TITLE +
            " = :title WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void setTitleSync(long id, @NonNull String title);

//    {
//        ContentValues values = new ContentValues();
//        values.put(Contract.Expenses.COLUMN_NAME_TITLE, title);
//        updateInTransaction(id, values);
//    }
//
//    @NonNull
//    @Override
//    final String getTableName() {
//        return Contract.Expenses.TABLE_NAME;
//    }
//
//    public final long insertSync(@NonNull String title) {
//        ContentValues values = new ContentValues();
//        values.put(Contract.COLUMN_NAME_PAYMENT, 0);
//        values.put(Contract.Expenses.COLUMN_NAME_TITLE, title);
//        return insertInTransaction(values);
//    }
//
//    @Override
//    @Query("SELECT * FROM " +
//            Contract.Expenses.TABLE_NAME +
//            " WHERE " +
//            BaseColumns._ID +
//            " = :id")
//    public abstract LiveData<RawExpenseEntity> getItem(long id);


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

    //
//    @Override
//    @Query("SELECT * FROM " +
//            Contract.Expenses.TABLE_NAME +
//            " WHERE " +
//            BaseColumns._ID +
//            " = :id")
//    abstract RawExpenseEntity fetchItemInternalSync(long id);

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
    public abstract LiveData<List<RawExpenseEntity>> fetchItems();

    @Nullable
    @Override
    @Query("SELECT * FROM " +
            Contract.Expenses.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    abstract RawExpenseEntity fetchItemInternalSync(long id);

    public final long insertSync(@NonNull String title){
        return insertSync(RawExpenseEntity.from(title));
    }

}
