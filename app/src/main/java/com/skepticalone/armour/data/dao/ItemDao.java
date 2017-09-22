package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Insert;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

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
    final AppDatabase getDatabase() {
        return database;
    }

    public abstract LiveData<Entity> getItem(long id);

    @Insert
    public abstract long restoreItemSync(Entity item);

    abstract Entity getItemInternalSync(long id);

    public abstract LiveData<List<Entity>> getItems();

    public final void setCommentSync(long id, @Nullable String comment) {
        ContentValues values = new ContentValues();
        values.put(Contract.COLUMN_NAME_COMMENT, comment);
        updateInTransaction(id, values);
    }

    @NonNull
    abstract String getTableName();

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    final long insertInTransaction(@NonNull ContentValues values) {
        getDatabase().beginTransaction();
        try {
            long rowId = getDatabase().getOpenHelper().getWritableDatabase().insert(getTableName(), SQLiteDatabase.CONFLICT_ABORT, values);
            if (rowId != -1L) {
                getDatabase().setTransactionSuccessful();
            }
            return rowId;
        } finally {
            getDatabase().endTransaction();
        }
    }

    final void updateInTransaction(long id, @NonNull ContentValues values) {
        getDatabase().beginTransaction();
        try {
            int rowsAffected = getDatabase().getOpenHelper().getWritableDatabase().update(getTableName(), SQLiteDatabase.CONFLICT_ABORT, values, BaseColumns._ID + "=?", new Long[]{id});
            if (rowsAffected == 1) {
                getDatabase().setTransactionSuccessful();
            }
//            return rowsAffected;
        } finally {
            getDatabase().endTransaction();
        }
    }

    @Nullable
    public final Entity deleteInTransaction(long id) {
        Entity item = getItemInternalSync(id);
        if (item != null) {
            getDatabase().beginTransaction();
            try {
                if (1 == getDatabase().getOpenHelper().getWritableDatabase().delete(
                        getTableName(),
                        BaseColumns._ID + "=?",
                        new Long[]{id}
                )) {
                    getDatabase().setTransactionSuccessful();
                }
            } finally {
                getDatabase().endTransaction();
            }
        }
        return item;
    }

}
