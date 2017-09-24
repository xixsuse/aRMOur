//package com.skepticalone.armour.data.model;
//
//import android.arch.lifecycle.LiveData;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.res.Resources;
//import android.preference.PreferenceManager;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//
//import com.skepticalone.armour.R;
//
//final class LiveComplianceConfig extends LiveData<ComplianceConfig> implements SharedPreferences.OnSharedPreferenceChangeListener {
//
//    @Nullable
//    private static LiveComplianceConfig checkerInstance;
//
//    @NonNull
//    private final String
//            keyCheckDurationOverDay,
//            keyCheckDurationOverWeek,
//            keyCheckDurationOverFortnight,
//            keyCheckDurationBetweenShifts,
//            keyCheckConsecutiveWeekends;
//
//    private final boolean
//            defaultCheckDurationOverDay,
//            defaultCheckDurationOverWeek,
//            defaultCheckDurationOverFortnight,
//            defaultCheckDurationBetweenShifts,
//            defaultCheckConsecutiveWeekends;
//
//    private LiveComplianceConfig(@NonNull Resources resources) {
//        keyCheckDurationOverDay = resources.getString(R.string.key_check_duration_over_day);
//        keyCheckDurationOverWeek = resources.getString(R.string.key_check_duration_over_week);
//        keyCheckDurationOverFortnight = resources.getString(R.string.key_check_duration_over_fortnight);
//        keyCheckDurationBetweenShifts = resources.getString(R.string.key_check_duration_between_shifts);
//        keyCheckConsecutiveWeekends = resources.getString(R.string.key_check_consecutive_weekends);
//        defaultCheckDurationOverDay = resources.getBoolean(R.bool.default_check_duration_over_day);
//        defaultCheckDurationOverWeek = resources.getBoolean(R.bool.default_check_duration_over_week);
//        defaultCheckDurationOverFortnight = resources.getBoolean(R.bool.default_check_duration_over_fortnight);
//        defaultCheckDurationBetweenShifts = resources.getBoolean(R.bool.default_check_duration_between_shifts);
//        defaultCheckConsecutiveWeekends = resources.getBoolean(R.bool.default_check_consecutive_weekends);
//    }
//
//    @NonNull
//    static LiveComplianceConfig getInstance(@NonNull Context context) {
//        if (checkerInstance == null) {
//            synchronized (LiveComplianceConfig.class) {
//                if (checkerInstance == null) {
//                    checkerInstance = new LiveComplianceConfig(context.getResources());
//                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//                    checkerInstance.updateChecker(sharedPreferences);
//                    sharedPreferences.registerOnSharedPreferenceChangeListener(checkerInstance);
//                }
//            }
//        }
//        return checkerInstance;
//    }
//
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if (
//                key.equals(keyCheckDurationOverDay) ||
//                        key.equals(keyCheckDurationOverWeek) ||
//                        key.equals(keyCheckDurationOverFortnight) ||
//                        key.equals(keyCheckDurationBetweenShifts) ||
//                        key.equals(keyCheckConsecutiveWeekends)
//                ) {
//            updateChecker(sharedPreferences);
//        }
//    }
//
//    private void updateChecker(@NonNull SharedPreferences sharedPreferences) {
//        setValue(new RawRosteredShiftEntity.ComplianceConfiguration(
//                sharedPreferences.getBoolean(keyCheckDurationOverDay, defaultCheckDurationOverDay),
//                sharedPreferences.getBoolean(keyCheckDurationOverWeek, defaultCheckDurationOverWeek),
//                sharedPreferences.getBoolean(keyCheckDurationOverFortnight, defaultCheckDurationOverFortnight),
//                sharedPreferences.getBoolean(keyCheckDurationBetweenShifts, defaultCheckDurationBetweenShifts),
//                sharedPreferences.getBoolean(keyCheckConsecutiveWeekends, defaultCheckConsecutiveWeekends)
//        ));
//    }
//
//}
