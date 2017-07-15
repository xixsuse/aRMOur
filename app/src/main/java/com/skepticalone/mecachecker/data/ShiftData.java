package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;

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
}
