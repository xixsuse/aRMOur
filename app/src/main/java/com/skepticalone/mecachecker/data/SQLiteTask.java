package com.skepticalone.mecachecker.data;

import android.support.annotation.WorkerThread;

interface SQLiteTask {
    @WorkerThread
    void runSQLiteTask() throws ShiftOverlapException;
}
