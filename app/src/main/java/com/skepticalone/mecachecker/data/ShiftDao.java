package com.skepticalone.mecachecker.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import org.joda.time.DateTime;

public interface ShiftDao {

    @WorkerThread
    void setShiftTimesSync(long id, @NonNull DateTime shiftStart, @NonNull DateTime shiftEnd);

    @WorkerThread
    @Nullable
    DateTime getLastShiftEndTime();

}
