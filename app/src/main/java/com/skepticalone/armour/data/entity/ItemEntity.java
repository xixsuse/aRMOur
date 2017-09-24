package com.skepticalone.armour.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.model.Item;

abstract class ItemEntity implements Item {
    @Nullable
    @ColumnInfo(name = Contract.COLUMN_NAME_COMMENT)
    private final String comment;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BaseColumns._ID)
    private final long id;

    ItemEntity(long id, @Nullable String comment) {
        this.id = id;
        this.comment = comment;
    }

    @Override
    public final long getId() {
        return id;
    }

    @Nullable
    @Override
    public final String getComment() {
        return comment;
    }
}
