package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.skepticalone.armour.data.db.AppDatabase;

import java.util.List;

public abstract class ItemDao<Entity> {

    @NonNull
    private final AppDatabase database;

    ItemDao(@NonNull AppDatabase database) {
        this.database = database;
    }

    @Insert
    public abstract long insertSync(@NonNull Entity item);

    @NonNull
    final AppDatabase getDatabase() {
        return database;
    }

    @NonNull
    public abstract LiveData<List<Entity>> fetchItems();

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    @Nullable
    abstract Entity fetchItemInternalSync(long id);

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    @Delete
    abstract int deleteItemInternalSync(@NonNull Entity item);

    public abstract void setCommentSync(long id, @Nullable String comment);

    @Nullable
    public final Entity deleteAndReturnItemSync(long id) {
        Entity item = fetchItemInternalSync(id);
        return (item != null && deleteItemInternalSync(item) == 1) ? item : null;
    }

}
