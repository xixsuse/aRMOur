package com.skepticalone.mecachecker.model;

import android.support.annotation.NonNull;

public interface Expense extends PayableItem {

    @NonNull
    String getTitle();

}
