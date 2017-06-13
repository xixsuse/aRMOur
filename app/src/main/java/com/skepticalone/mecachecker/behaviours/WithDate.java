package com.skepticalone.mecachecker.behaviours;

import android.support.annotation.NonNull;

public interface WithDate {

    @NonNull
    String getColumnNameStartOrDate();

    int getColumnIndexStartOrDate();

    int getRowNumberDate();

}
