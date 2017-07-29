package com.skepticalone.mecachecker.data.model;

import android.support.annotation.NonNull;

public interface Expense extends Payable {

    @NonNull
    String getTitle();

}
