package com.skepticalone.armour.data.model;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;


abstract class LiveConfig<T> extends LiveData<T> implements SharedPreferences.OnSharedPreferenceChangeListener {

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    final void init(@NonNull Context context) {
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

    abstract String[] getWatchKeys();
    @NonNull
    public abstract T getNewValue(@NonNull SharedPreferences sharedPreferences);
}
