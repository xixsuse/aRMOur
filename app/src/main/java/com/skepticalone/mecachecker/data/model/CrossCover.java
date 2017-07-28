package com.skepticalone.mecachecker.data.model;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

public interface CrossCover extends PayableItem {

    @NonNull
    LocalDate getDate();

}
