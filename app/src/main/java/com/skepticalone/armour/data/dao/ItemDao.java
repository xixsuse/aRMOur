package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.Insert;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.db.Contract;

import java.util.List;

public abstract class ItemDao<Entity> {
    @NonNull
    private final AppDatabase database;

    ItemDao(@NonNull AppDatabase database) {
        this.database = database;
    }

    @NonNull
    final SupportSQLiteStatement getUpdateStatement(@NonNull String columnName) {
        return getDatabase().compileStatement("UPDATE " +
                getTableName() +
                " SET " +
                columnName +
                " = ? WHERE " +
                BaseColumns._ID +
                " = ?");
    }

    final void updateInTransaction(@NonNull SupportSQLiteStatement boundStatement) {
        getDatabase().beginTransaction();
        try {
            if (boundStatement.executeUpdateDelete() == 1) {
                getDatabase().setTransactionSuccessful();
            }
        } finally {
            getDatabase().endTransaction();
        }
    }

    @NonNull
    final AppDatabase getDatabase() {
        return database;
    }

    @SuppressWarnings("EmptyMethod")
    public abstract LiveData<Entity> getItem(long id);

    @Insert
    public abstract long restoreItemSync(Entity item);

    @SuppressWarnings("EmptyMethod")
    abstract Entity getItemInternalSync(long id);

    @SuppressWarnings("EmptyMethod")
    public abstract LiveData<List<Entity>> getItems();

    @Nullable
    public final Entity deleteSync(long id){
        Entity item = getItemInternalSync(id);
        if (item != null) {
            SupportSQLiteStatement deleteStatement = getDatabase().compileStatement("DELETE FROM " +
                    getTableName() +
                    " WHERE " +
                    BaseColumns._ID +
                    " = ?");
            deleteStatement.bindLong(1, id);
            getDatabase().beginTransaction();
            try {
                if (deleteStatement.executeUpdateDelete() == 1) {
                    getDatabase().setTransactionSuccessful();
                    return item;
                }
            } finally {
                getDatabase().endTransaction();
            }
        }
        return null;
    }

    public final void setCommentSync(long id, @Nullable String comment){
        SupportSQLiteStatement setCommentStatement = getUpdateStatement(Contract.COLUMN_NAME_COMMENT);
        setCommentStatement.bindString(1, comment);
        setCommentStatement.bindLong(2, id);
        updateInTransaction(setCommentStatement);
    }

    @NonNull
    abstract String getTableName();

}
