package com.skepticalone.armour.data.newData;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.List;

public final class RosteredShiftViewModel extends AndroidViewModel {

    @NonNull
    private final LiveRosteredShifts rosteredShifts;

    public RosteredShiftViewModel(Application application) {
        super(application);
        rosteredShifts = new LiveRosteredShifts(application);
        PreferenceManager.getDefaultSharedPreferences(application).registerOnSharedPreferenceChangeListener(rosteredShifts);
    }

    @NonNull
    public LiveData<List<RosteredShift>> getRosteredShifts() {
        return rosteredShifts;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        PreferenceManager.getDefaultSharedPreferences(getApplication()).unregisterOnSharedPreferenceChangeListener(rosteredShifts);
    }
}
