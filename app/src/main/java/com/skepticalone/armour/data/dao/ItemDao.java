package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.util.List;

@SuppressWarnings("NullableProblems")
public abstract class ItemDao<Entity> {

    @Insert
    public abstract long insertSync(@NonNull Entity item);

    @SuppressWarnings({"EmptyMethod", "unused"})
    @NonNull
    public abstract LiveData<List<Entity>> fetchItems();

    @SuppressWarnings("EmptyMethod")
    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    @Nullable
    abstract Entity fetchItemInternalSync(@SuppressWarnings("unused") long id);

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    @Delete
    abstract int deleteItemInternalSync(@NonNull Entity item);

    @SuppressWarnings("EmptyMethod")
    public abstract void setCommentSync(@SuppressWarnings("unused") long id, @SuppressWarnings("unused") @Nullable String comment);

    @Nullable
    public final Entity deleteAndReturnItemSync(long id) {
        Entity item = fetchItemInternalSync(id);
        return (item != null && deleteItemInternalSync(item) == 1) ? item : null;
    }

}
