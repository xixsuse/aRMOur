package com.skepticalone.mecachecker.data;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

public interface CrossCoverCallbacks extends ItemCallbacks<CrossCoverEntity>, SingleAddCallbacks {

    @MainThread
    void setDate(long id, @NonNull LocalDate date);

}
