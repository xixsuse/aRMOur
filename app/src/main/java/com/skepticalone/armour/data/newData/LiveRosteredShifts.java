package com.skepticalone.armour.data.newData;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.skepticalone.armour.data.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

final class LiveRosteredShifts extends MediatorLiveData<List<RosteredShift>> implements SharedPreferences.OnSharedPreferenceChangeListener {
    //
//    @NonNull
//    private final String
//            keyCheckDurationOverDay,
//            keyCheckDurationOverWeek,
//            keyCheckDurationOverFortnight,
//            keyCheckDurationBetweenShifts,
//            keyCheckConsecutiveWeekends;
//
//    @NonNull
//    private final String
//            keyNormalDayStart,
//            keyNormalDayEnd,
//            keyLongDayStart,
//            keyLongDayEnd,
//            keyNightShiftStart,
//            keyNightShiftEnd;
//
//    private final int
//            defaultNormalDayStart,
//            defaultNormalDayEnd,
//            defaultLongDayStart,
//            defaultLongDayEnd,
//            defaultNightShiftStart,
//            defaultNightShiftEnd;
//
//    private final boolean
//            defaultCheckDurationOverDay,
//            defaultCheckDurationOverWeek,
//            defaultCheckDurationOverFortnight,
//            defaultCheckDurationBetweenShifts,
//            defaultCheckConsecutiveWeekends;
//
//    @NonNull
//    private final SharedPreferences preferences;
//    @NonNull
//    private final SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
//        @Override
//        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//
//        }
//    };
    private static final String TAG = "LiveRosteredShifts";
    @NonNull
    private final LiveData<List<RawShift>> rawShifts;
    @NonNull
    private final LiveShiftTypeCalculator shiftTypeCalculator;
    @NonNull
    private final LiveComplianceChecker complianceChecker;

    public LiveRosteredShifts(@NonNull Application application) {
        super();
        rawShifts = AppDatabase.getInstance(application).newRosteredShiftDao().getShifts();
        shiftTypeCalculator = new LiveShiftTypeCalculator(application);
        complianceChecker = new LiveComplianceChecker(application);
        addSource(rawShifts, new Observer<List<RawShift>>() {
            @Override
            public void onChanged(@Nullable List<RawShift> rawShifts) {
                updateSelf(rawShifts, shiftTypeCalculator.getValue(), complianceChecker.getValue());
            }
        });
        addSource(shiftTypeCalculator, new Observer<ShiftTypeCalculator>() {
            @Override
            public void onChanged(@Nullable ShiftTypeCalculator shiftTypeCalculator) {
                updateSelf(rawShifts.getValue(), shiftTypeCalculator, complianceChecker.getValue());
            }
        });
        addSource(complianceChecker, new Observer<ComplianceChecker>() {
            @Override
            public void onChanged(@Nullable ComplianceChecker complianceChecker) {
                updateSelf(rawShifts.getValue(), shiftTypeCalculator.getValue(), complianceChecker);
            }
        });
    }

    private void updateSelf(@Nullable List<RawShift> rawShifts, @Nullable ShiftTypeCalculator shiftTypeCalculator, @Nullable ComplianceChecker complianceChecker) {
        Log.d(TAG, String.format("updateSelf: %s, %s, %s", rawShifts != null, shiftTypeCalculator != null, complianceChecker != null));
        if (rawShifts != null && shiftTypeCalculator != null && complianceChecker != null) {
            Log.d(TAG, "updateSelf: passed barrier, updating result");
            ArrayList<RosteredShift> result = new ArrayList<>(rawShifts.size());
            for (RawShift rawShift : rawShifts) {
                result.add(RosteredShift.from(rawShift, shiftTypeCalculator));
            }
            complianceChecker.process(result);
            setValue(result);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        shiftTypeCalculator.onSharedPreferenceChanged(sharedPreferences, key);
        complianceChecker.onSharedPreferenceChanged(sharedPreferences, key);
    }

    //
//    final void onLifecycleStarted
//
//    @Override
//    protected void onActive() {
//        super.onActive();
//        preferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
//    }
//
//    @Override
//    protected void onInactive() {
//        super.onInactive();
//        preferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
//    }
}
