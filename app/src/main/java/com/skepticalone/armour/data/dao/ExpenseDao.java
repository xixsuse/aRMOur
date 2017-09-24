package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.content.ContentValues;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.model.RawExpenseEntity;

import java.util.List;

@Dao
public abstract class ExpenseDao extends ItemDao<RawExpenseEntity> {

    @NonNull
    private final PayableDaoHelper payableDaoHelper;

    ExpenseDao(@NonNull AppDatabase database) {
        super(database);
        payableDaoHelper = new PayableDaoHelper(this);
    }

    @NonNull
    public final PayableDaoHelper getPayableDaoHelper() {
        return payableDaoHelper;
    }

    public final void setTitleSync(long id, @NonNull String title){
        ContentValues values = new ContentValues();
        values.put(Contract.Expenses.COLUMN_NAME_TITLE, title);
        updateInTransaction(id, values);
    }

    @NonNull
    @Override
    final String getTableName() {
        return Contract.Expenses.TABLE_NAME;
    }

    public final long insertSync(@NonNull String title) {
        ContentValues values = new ContentValues();
        values.put(Contract.COLUMN_NAME_PAYMENT, 0);
        values.put(Contract.Expenses.COLUMN_NAME_TITLE, title);
        return insertInTransaction(values);
    }

    @Override
    @Query("SELECT * FROM " +
            Contract.Expenses.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract LiveData<RawExpenseEntity> getItem(long id);

    @Override
    @Query("SELECT * FROM " +
            Contract.Expenses.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    abstract RawExpenseEntity getItemInternalSync(long id);

    @Override
    @Query("SELECT * FROM " + Contract.Expenses.TABLE_NAME + " ORDER BY CASE WHEN " + Contract.COLUMN_NAME_CLAIMED + " IS NULL THEN 2 WHEN " + Contract.COLUMN_NAME_PAID + " IS NULL THEN 1 ELSE 0 END, coalesce(" + Contract.COLUMN_NAME_PAID + ", " + Contract.COLUMN_NAME_CLAIMED + ", " + BaseColumns._ID + ")")
    public abstract LiveData<List<RawExpenseEntity>> getItems();
}
