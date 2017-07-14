package com.skepticalone.mecachecker.model;

import android.support.annotation.NonNull;

import org.joda.time.Interval;

public interface Shift extends Item {

    @NonNull
    Interval getShift();

}
