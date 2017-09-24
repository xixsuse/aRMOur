//package com.skepticalone.armour.data.model;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.res.Resources;
//import android.preference.PreferenceManager;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.util.Pair;
//
//import com.skepticalone.armour.R;
//import com.skepticalone.armour.data.model.ShiftEntity;
//import com.skepticalone.armour.settings.TimePreference;
//
//import org.threeten.bp.LocalTime;
//import org.threeten.bp.ZoneId;
//
//public final class LiveShiftConfig extends LiveConfig<> implements SharedPreferences.OnSharedPreferenceChangeListener {
//
//    @Nullable
//    private static LiveShiftConfig calculatorInstance;
//
//    @NonNull
//    private final String
//            keyNormalDayStart,
//            keyNormalDayEnd,
//            keyLongDayStart,
//            keyLongDayEnd,
//            keyNightShiftStart,
//            keyNightShiftEnd,
//            keyTimeZoneId;
//
//    private final int
//            defaultNormalDayStart,
//            defaultNormalDayEnd,
//            defaultLongDayStart,
//            defaultLongDayEnd,
//            defaultNightShiftStart,
//            defaultNightShiftEnd;
//
//    private LiveShiftConfig(@NonNull Resources resources) {
//        keyNormalDayStart = resources.getString(R.string.key_start_normal_day);
//        keyNormalDayEnd = resources.getString(R.string.key_end_normal_day);
//        keyLongDayStart = resources.getString(R.string.key_start_long_day);
//        keyLongDayEnd = resources.getString(R.string.key_end_long_day);
//        keyNightShiftStart = resources.getString(R.string.key_start_night_shift);
//        keyNightShiftEnd = resources.getString(R.string.key_end_night_shift);
//        keyTimeZoneId = resources.getString(R.string.key_time_zone_id);
//        defaultNormalDayStart = resources.getInteger(R.integer.default_start_normal_day);
//        defaultNormalDayEnd = resources.getInteger(R.integer.default_end_normal_day);
//        defaultLongDayStart = resources.getInteger(R.integer.default_start_long_day);
//        defaultLongDayEnd = resources.getInteger(R.integer.default_end_long_day);
//        defaultNightShiftStart = resources.getInteger(R.integer.default_start_night_shift);
//        defaultNightShiftEnd = resources.getInteger(R.integer.default_end_night_shift);
//    }
//
//    @NonNull
//    public static LiveShiftConfig getInstance(@NonNull Context context) {
//        if (calculatorInstance == null) {
//            synchronized (LiveShiftConfig.class) {
//                if (calculatorInstance == null) {
//                    calculatorInstance = new LiveShiftConfig(context.getResources());
//                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//                    calculatorInstance.updateCalculator(sharedPreferences);
//                    sharedPreferences.registerOnSharedPreferenceChangeListener(calculatorInstance);
//                }
//            }
//        }
//        return calculatorInstance;
//    }
//
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if (
//                key.equals(keyNormalDayStart) ||
//                        key.equals(keyNormalDayEnd) ||
//                        key.equals(keyLongDayStart) ||
//                        key.equals(keyLongDayEnd) ||
//                        key.equals(keyNightShiftStart) ||
//                        key.equals(keyNightShiftEnd)
//                ) {
//            updateCalculator(sharedPreferences);
//        }
//    }
//
//    private void updateCalculator(@NonNull SharedPreferences sharedPreferences) {
//        setValue(new ShiftEntity.ShiftTypeConfiguration(
//                sharedPreferences.getInt(keyNormalDayStart, defaultNormalDayStart),
//                sharedPreferences.getInt(keyNormalDayEnd, defaultNormalDayEnd),
//                sharedPreferences.getInt(keyLongDayStart, defaultLongDayStart),
//                sharedPreferences.getInt(keyLongDayEnd, defaultLongDayEnd),
//                sharedPreferences.getInt(keyNightShiftStart, defaultNightShiftStart),
//                sharedPreferences.getInt(keyNightShiftEnd, defaultNightShiftEnd),
//                getFreshZoneId(sharedPreferences)
//        ));
//    }
//
//    @NonNull
//    public ZoneId getFreshZoneId(@NonNull SharedPreferences sharedPreferences) {
//        String zoneId = sharedPreferences.getString(keyTimeZoneId, null);
//        return zoneId == null ? ZoneId.systemDefault() : ZoneId.of(zoneId);
//    }
//
//    @NonNull
//    public ZoneId getFreshZoneId(@NonNull Context context) {
//        return getFreshZoneId(PreferenceManager.getDefaultSharedPreferences(context));
//    }
//
//    @NonNull
//    public Pair<LocalTime, LocalTime> getPair(@NonNull Shift.ShiftType shiftType, @NonNull SharedPreferences sharedPreferences) {
//        @NonNull final String keyStart, keyEnd;
//        final int defaultStart, defaultEnd;
//        switch (shiftType) {
//            case NORMAL_DAY:
//                keyStart = keyNormalDayStart;
//                defaultStart = defaultNormalDayStart;
//                keyEnd = keyNormalDayEnd;
//                defaultEnd = defaultNormalDayEnd;
//                break;
//            case LONG_DAY:
//                keyStart = keyLongDayStart;
//                defaultStart = defaultLongDayStart;
//                keyEnd = keyLongDayEnd;
//                defaultEnd = defaultLongDayEnd;
//                break;
//            case NIGHT_SHIFT:
//                keyStart = keyNightShiftStart;
//                defaultStart = defaultNightShiftStart;
//                keyEnd = keyNightShiftEnd;
//                defaultEnd = defaultNightShiftEnd;
//                break;
//            default:
//                throw new IllegalStateException();
//        }
//        return new Pair<>(TimePreference.getTime(sharedPreferences.getInt(keyStart, defaultStart)), TimePreference.getTime(sharedPreferences.getInt(keyEnd, defaultEnd)));
//    }
////
////    @NonNull
////    public Pair<LocalTime, LocalTime> getNormalDay(@NonNull SharedPreferences sharedPreferences){
////        return getPair(sharedPreferences.getInt(keyNormalDayStart, defaultNormalDayStart), sharedPreferences.getInt(keyNormalDayEnd, defaultNormalDayEnd));
////    }
////
////    @NonNull
////    public Pair<LocalTime, LocalTime> getLongDay(@NonNull SharedPreferences sharedPreferences){
////        return getPair(sharedPreferences.getInt(keyLongDayStart, defaultLongDayStart), sharedPreferences.getInt(keyLongDayEnd, defaultLongDayEnd));
////    }
////
////    @NonNull
////    public Pair<LocalTime, LocalTime> getNightShift(@NonNull SharedPreferences sharedPreferences){
////        return getPair(sharedPreferences.getInt(keyNightShiftStart, defaultNightShiftStart), sharedPreferences.getInt(keyNightShiftEnd, defaultNightShiftEnd));
////    }
////
////    @NonNull
////    public Pair<LocalTime, LocalTime> getTimes(@NonNull ShiftType shiftType, @NonNull SharedPreferences sharedPreferences) {
////        final int start, end;
////        if (shiftType == ShiftType.NORMAL_DAY) {
////            start = sharedPreferences.getInt(keyNormalDayStart, defaultNormalDayStart);
////            end = sharedPreferences.getInt(keyNormalDayEnd, defaultNormalDayEnd);
////        } else if (shiftType == ShiftType.LONG_DAY) {
////            start = sharedPreferences.getInt(keyLongDayStart, defaultLongDayStart);
////            end = sharedPreferences.getInt(keyLongDayEnd, defaultLongDayEnd);
////        } else if (shiftType == ShiftType.NIGHT_SHIFT) {
////            start = sharedPreferences.getInt(keyNightShiftStart, defaultNightShiftStart);
////            end = sharedPreferences.getInt(keyNightShiftEnd, defaultNightShiftEnd);
////        } else {
////            throw new IllegalStateException();
////        }
////        return new Pair<>(DateTimeUtils.getTime(start), DateTimeUtils.getTime(end));
////    }
//
//}
