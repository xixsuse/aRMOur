package com.skepticalone.mecachecker.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public interface RosteredShiftTimeSetter {

    void setShiftTimes(long id, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end, @Nullable LocalTime loggedStart, @Nullable LocalTime loggedEnd);

}
