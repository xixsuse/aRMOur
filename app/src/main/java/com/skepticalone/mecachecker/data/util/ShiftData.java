package com.skepticalone.mecachecker.data.util;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.db.Contract;

import org.joda.time.DateTime;

public final class ShiftData {

    @NonNull
    @ColumnInfo(name = Contract.COLUMN_NAME_SHIFT_START)
    private final DateTime start;

    @NonNull
    @ColumnInfo(name = Contract.COLUMN_NAME_SHIFT_END)
    private final DateTime end;

    public ShiftData(
            @NonNull DateTime start,
            @NonNull DateTime end
    ) {
        this.start = start;
        this.end = end;
    }

    @NonNull
    public DateTime getStart() {
        return start;
    }

    @NonNull
    public DateTime getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ShiftData) {
            ShiftData other = (ShiftData) object;
            return start.getMillis() == other.start.getMillis() && end.getMillis() == other.end.getMillis();
        }
        return false;
    }

}
