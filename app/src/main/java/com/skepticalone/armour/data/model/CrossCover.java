package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;

public interface CrossCover extends Payable {

    @NonNull
    LocalDate getDate();

}
