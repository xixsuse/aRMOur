package com.skepticalone.mecachecker.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.Insert;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.db.Contract;

import java.util.List;

public abstract class CustomDao<Entity> {
    @NonNull
    private final AppDatabase database;
    @NonNull
    private final SupportSQLiteStatement deleteStatement, setCommentStatement;

    CustomDao(@NonNull AppDatabase database) {
        this.database = database;
        deleteStatement = database.compileStatement("DELETE FROM " +
                getTableName() +
                " WHERE " +
                BaseColumns._ID +
                " = ?");
        setCommentStatement = getUpdateStatement(Contract.COLUMN_NAME_COMMENT);
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

    public abstract LiveData<Entity> getItem(long id);

    @Insert
    public abstract long restoreItemSync(Entity item);

    abstract Entity getItemInternalSync(long id);

    public abstract LiveData<List<Entity>> getItems();

    @Nullable
    public final Entity deleteSync(long id){
        Entity item = getItemInternalSync(id);
        if (item != null) {
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
        setCommentStatement.bindString(1, comment);
        setCommentStatement.bindLong(2, id);
        updateInTransaction(setCommentStatement);
    }

    @NonNull
    abstract String getTableName();

}
