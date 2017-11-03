package com.skepticalone.armour.util;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;


public abstract class LiveConfiguration<T> extends LiveData<T> implements SharedPreferences.OnSharedPreferenceChangeListener {

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    public final void init(@NonNull Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        setValue(getNewValue(sharedPreferences));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public final void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        for (String watchKey : getWatchKeys()) {
            if (key.equals(watchKey)) {
                setValue(getNewValue(sharedPreferences));
                return;
            }
        }
    }

    public abstract String[] getWatchKeys();
    @NonNull
    public abstract T getNewValue(@NonNull SharedPreferences sharedPreferences);
}
