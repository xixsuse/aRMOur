package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.util.List;
import java.util.Set;

@SuppressWarnings("NullableProblems")
public abstract class ItemDao<Entity> {

    @Insert
    public abstract long insertSync(@NonNull Entity item);

    @Insert
    public abstract void insertItemsSync(@NonNull List<Entity> items);

    @SuppressWarnings({"EmptyMethod", "unused"})
    @NonNull
    public abstract LiveData<List<Entity>> fetchItems();

    @SuppressWarnings({"EmptyMethod", "unused"})
    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    @NonNull
    abstract List<Entity> fetchItemsInternalSync(@NonNull Set<Long> ids);

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    @Delete
    abstract void deleteItemsInternalSync(@NonNull List<Entity> items);

    @SuppressWarnings("EmptyMethod")
    public abstract void setCommentSync(@SuppressWarnings("unused") long id, @SuppressWarnings("unused") @Nullable String comment);

    @NonNull
    public final List<Entity> deleteAndReturnDeletedItemsSync(@NonNull Set<Long> ids) {
        List<Entity> items = fetchItemsInternalSync(ids);
        deleteItemsInternalSync(items);
        return items;
    }

}
