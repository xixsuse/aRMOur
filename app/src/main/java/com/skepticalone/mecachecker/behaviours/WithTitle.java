package com.skepticalone.mecachecker.behaviours;

import android.support.annotation.NonNull;

public interface WithTitle {

    @SuppressWarnings("SameReturnValue")
    @NonNull
    String getColumnNameTitle();

    int getColumnIndexTitle();

    int getRowNumberTitle();

}
