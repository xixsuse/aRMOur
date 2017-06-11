package com.skepticalone.mecachecker.components;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

interface Payment {

    @NonNull
    String getColumnNameMoney();

    @NonNull
    String getColumnNameComment();

    @NonNull
    String getColumnNameClaimed();

    @NonNull
    String getColumnNamePaid();

    int getColumnIndexMoney();

    int getColumnIndexComment();

    int getColumnIndexClaimed();

    int getColumnIndexPaid();

    int getRowNumberMoney();

    int getRowNumberComment();

    int getRowNumberClaimed();

    int getRowNumberPaid();

    @StringRes
    int getMoneyTitle();

    @DrawableRes
    int getMoneyIcon();

}