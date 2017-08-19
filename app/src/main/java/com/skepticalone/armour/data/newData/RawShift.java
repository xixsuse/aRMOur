package com.skepticalone.armour.data.newData;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.db.Contract;

import org.joda.time.Interval;

class RawShift {

    @ColumnInfo(name = Contract.COLUMN_NAME_SHIFT_START)
    final long start;
    @ColumnInfo(name = Contract.COLUMN_NAME_SHIFT_END)
    final long end;

    @NonNull
    @Ignore
    final Interval interval;

    public RawShift(long start, long end) {
        this.start = start;
        this.end = end;
        this.interval = new Interval(start, end);
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }
    //    RawShift(@NonNull Interval interval) {
//        this.interval = interval;
//    }

    @Override
    public String toString() {
        return interval.toString();
    }

}
