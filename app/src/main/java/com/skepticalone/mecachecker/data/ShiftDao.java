package com.skepticalone.mecachecker.data;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.skepticalone.mecachecker.model.Shift;

import org.joda.time.DateTime;

public interface ShiftDao<ItemType extends Shift> extends BaseItemDao<Shift> {

    @WorkerThread
    void setShiftTimesSync(long id, @NonNull DateTime shiftStart, @NonNull DateTime shiftEnd);

    @WorkerThread
    @NonNull
    DateTime getLastShiftEndTime();

}
