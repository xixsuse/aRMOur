package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.model.Item;

abstract class ItemEntity implements Item {
    @Nullable
    @ColumnInfo(name = Contract.COLUMN_NAME_COMMENT)
    private final String comment;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BaseColumns._ID)
    private long id = 0L;

    ItemEntity(@Nullable String comment) {
        this.comment = comment;
    }

    @Override
    public final long getId() {
        return id;
    }

    public final void setId(long id) {
        this.id = id;
    }

    @Nullable
    @Override
    public final String getComment() {
        return comment;
    }
}
