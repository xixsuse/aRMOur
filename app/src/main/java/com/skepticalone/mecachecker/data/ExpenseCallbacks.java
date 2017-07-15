package com.skepticalone.mecachecker.data;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

public interface ExpenseCallbacks extends ItemCallbacks<ExpenseEntity>, SingleAddCallbacks {

    @MainThread
    void setTitle(long id, @NonNull String title);

}
