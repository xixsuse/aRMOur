package com.skepticalone.armour.util;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;


@SuppressWarnings("WeakerAccess")
public final class ArmourApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
