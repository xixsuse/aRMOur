package com.skepticalone.mecachecker.model;

import android.support.annotation.Nullable;

public interface Item {

    long getId();

    @Nullable
    String getComment();

}
