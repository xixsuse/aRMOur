package com.skepticalone.mecachecker.dialog;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public interface AdditionalShiftTimeSetter {

    void setShiftTimes(long id, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end);

}
