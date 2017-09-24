package com.skepticalone.armour.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.Contract;

public abstract class Item {
    @Nullable
    @ColumnInfo(name = Contract.COLUMN_NAME_COMMENT)
    private final String comment;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BaseColumns._ID)
    private final long id;

    public Item(long id, @Nullable String comment) {
        this.id = id;
        this.comment = comment;
    }

    public final long getId() {
        return id;
    }

    @Nullable
    public final String getComment() {
        return comment;
    }
}
