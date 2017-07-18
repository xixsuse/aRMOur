package com.skepticalone.mecachecker.data.dao;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import org.joda.time.DateTime;

public interface ShiftDaoContract {

    @WorkerThread
    @Nullable
    DateTime getLastShiftEndTimeSync();

}
