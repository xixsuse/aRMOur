package com.skepticalone.mecachecker.model;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

public interface CrossCover extends Item, Payable {

    @NonNull
    LocalDate getDate();

}
