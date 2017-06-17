package com.skepticalone.mecachecker.behaviours;

import android.support.annotation.NonNull;

public interface LoggableShift extends Shift {

    int getRowNumberLoggedSwitch();

    @SuppressWarnings("SameReturnValue")
    @NonNull
    String getColumnNameLoggedStart();

    @SuppressWarnings("SameReturnValue")
    int getColumnIndexLoggedStart();

    int getRowNumberLoggedStart();

    @SuppressWarnings("SameReturnValue")
    @NonNull
    String getColumnNameLoggedEnd();

    @SuppressWarnings("SameReturnValue")
    int getColumnIndexLoggedEnd();

    int getRowNumberLoggedEnd();

}
