package com.skepticalone.mecachecker.behaviours;

import android.support.annotation.NonNull;

public interface LoggableShift extends Shift {

    int getRowNumberLoggedSwitch();

    @NonNull
    String getColumnNameLoggedStart();

    int getColumnIndexLoggedStart();

    int getRowNumberLoggedStart();

    @NonNull
    String getColumnNameLoggedEnd();

    int getColumnIndexLoggedEnd();

    int getRowNumberLoggedEnd();

}
