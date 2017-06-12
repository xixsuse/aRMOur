package com.skepticalone.mecachecker.components;

import android.support.annotation.NonNull;

interface Logged {
    @NonNull
    String getColumnNameLoggedStart();

    int getColumnIndexLoggedStart();

    int getRowNumberLoggedStart();

    @NonNull
    String getColumnNameLoggedEnd();

    int getColumnIndexLoggedEnd();

    int getRowNumberLoggedEnd();
}
