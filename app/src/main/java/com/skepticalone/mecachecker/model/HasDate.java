package com.skepticalone.mecachecker.model;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

interface HasDate {

    @NonNull
    LocalDate getDate();

}
