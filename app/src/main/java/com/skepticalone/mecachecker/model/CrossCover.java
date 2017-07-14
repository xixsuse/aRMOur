package com.skepticalone.mecachecker.model;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

public interface CrossCover extends PayableItem {

    @NonNull
    LocalDate getDate();

}
