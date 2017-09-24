package com.skepticalone.armour.data.viewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.BoolRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.dao.RawRosteredShiftDao;
import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.model.LiveShiftConfig;
import com.skepticalone.armour.data.model.RawRosteredShiftEntity;
import com.skepticalone.armour.data.model.RawShift;
import com.skepticalone.armour.data.model.Shift;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

import java.util.List;

public final class RosteredShiftViewModel extends ItemViewModel<RawRosteredShiftEntity> implements ShiftViewModelContract<RawRosteredShiftEntity> {
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

    @NonNull
    private final LiveData<List<RawRosteredShiftEntity>> items;

//    @NonNull
//    private final MutableLiveData<RawRosteredShiftEntity.ComplianceChecker> complianceChecker = new MutableLiveData<>();
//
//    @NonNull
//    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
//        @Override
//        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//            if (
//                    key.equals(keyCheckDurationOverDay) ||
//                            key.equals(keyCheckDurationOverWeek) ||
//                            key.equals(keyCheckDurationOverFortnight) ||
//                            key.equals(keyCheckDurationBetweenShifts) ||
//                            key.equals(keyCheckConsecutiveWeekends)
//                    ) {
//                updateComplianceChecker(sharedPreferences);
//            }
//        }
//    };

    public RosteredShiftViewModel(@NonNull Application application) {
        super(application);
//        Resources resources = application.getResources();
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
//        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
//        updateComplianceChecker(defaultSharedPreferences);
//        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        items = new LiveRosteredShifts(application, getDao().getItems());
    }
//
//    private void updateComplianceChecker(@NonNull SharedPreferences sharedPreferences) {
//        complianceChecker.setValue(new RawRosteredShiftEntity.ComplianceChecker(
//                sharedPreferences.getBoolean(keyCheckDurationOverDay, defaultCheckDurationOverDay),
//                sharedPreferences.getBoolean(keyCheckDurationOverWeek, defaultCheckDurationOverWeek),
//                sharedPreferences.getBoolean(keyCheckDurationOverFortnight, defaultCheckDurationOverFortnight),
//                sharedPreferences.getBoolean(keyCheckDurationBetweenShifts, defaultCheckDurationBetweenShifts),
//                sharedPreferences.getBoolean(keyCheckConsecutiveWeekends, defaultCheckConsecutiveWeekends)
//        ));
//    }
//
//    @Override
//    protected void onCleared() {
//        super.onCleared();
//        Log.d("RosteredShiftViewModel", "onCleared: ");
//        PreferenceManager.getDefaultSharedPreferences(getApplication()).unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
//    }

    @NonNull
    @Override
    RawRosteredShiftDao getDao() {
        return AppDatabase.getInstance(getApplication()).rosteredShiftDao();
    }

    @NonNull
    @Override
    public LiveData<List<RawRosteredShiftEntity>> fetchItems() {
        return items;
    }

    @NonNull
    @Override
    LiveData<RawRosteredShiftEntity> fetchItem(final long id) {
        return Transformations.map(items, new Function<List<RawRosteredShiftEntity>, RawRosteredShiftEntity>() {
            @Override
            public RawRosteredShiftEntity apply(List<RawRosteredShiftEntity> shifts) {
                if (shifts != null) {
                    for (RawRosteredShiftEntity shift : shifts) {
                        if (shift.getId() == id) return shift;
                    }
                }
                return null;
            }
        });
    }

    private boolean skipWeekends(@NonNull final Shift.ShiftType shiftType) {
        @StringRes final int skipWeekendsKey;
        @BoolRes final int defaultSkipWeekends;
        if (shiftType == Shift.ShiftType.NORMAL_DAY) {
            skipWeekendsKey = R.string.key_skip_weekend_normal_day;
            defaultSkipWeekends = R.bool.default_skip_weekend_normal_day;
        } else if (shiftType == Shift.ShiftType.LONG_DAY) {
            skipWeekendsKey = R.string.key_skip_weekend_long_day;
            defaultSkipWeekends = R.bool.default_skip_weekend_long_day;
        } else if (shiftType == Shift.ShiftType.NIGHT_SHIFT) {
            skipWeekendsKey = R.string.key_skip_weekend_night_shift;
            defaultSkipWeekends = R.bool.default_skip_weekend_night_shift;
        } else throw new IllegalStateException();
        return PreferenceManager.getDefaultSharedPreferences(getApplication()).getBoolean(getApplication().getString(skipWeekendsKey), getApplication().getResources().getBoolean(defaultSkipWeekends));
    }

    @Override
    public void addNewShift(@NonNull final Shift.ShiftType shiftType) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
                LiveShiftConfig calculator = LiveShiftConfig.getInstance(getApplication());
                postSelectedId(getDao().insertSync(
                        calculator.getPair(shiftType, sharedPreferences),
                        calculator.getFreshZoneId(sharedPreferences),
                        skipWeekends(shiftType)
                ));
            }
        });
    }

    public void setLogged(final boolean logged) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                if (logged) {
                    try {
                        getDao().switchOnLogSync(getCurrentItemId());
                    } catch (SQLiteConstraintException e) {
                        postOverlappingShifts();
                    }
                }
                else getDao().switchOffLogSync(getCurrentItemId());
            }
        });
    }

    @MainThread
    private void saveNewShiftTimes(final long id, @NonNull final RawShift.RawShiftData rawShiftData, @Nullable final RawShift.RawShiftData loggedRawShiftData) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    getDao().setShiftTimesSync(id, rawShiftData.getStart(), rawShiftData.getEnd(), loggedRawShiftData == null ? null : loggedRawShiftData.getStart(), loggedRawShiftData == null ? null : loggedRawShiftData.getEnd());
                } catch (SQLiteConstraintException e) {
                    postOverlappingShifts();
                }
            }
        });
    }

    @Override
    public void saveNewDate(@NonNull LocalDate date) {
        RawRosteredShiftEntity shift = getCurrentItem().getValue();
        if (shift == null) throw new IllegalStateException();
        ZoneId zoneId = getZoneId();
        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewDate(date, zoneId), shift.getLoggedShiftData() == null ? null : shift.getLoggedShiftData().withNewDate(date, zoneId));
    }

    public void saveNewTime(@NonNull LocalTime time, boolean start, boolean logged) {
        RawRosteredShiftEntity shift = getCurrentItem().getValue();
        if (shift == null || (logged && shift.getLoggedShiftData() == null)) throw new IllegalStateException();
        ZoneId zoneId = getZoneId();
        saveNewShiftTimes(shift.getId(), logged ? shift.getShiftData() : shift.getShiftData().withNewTime(time, zoneId, start), logged ? shift.getLoggedShiftData().withNewTime(time, zoneId, start) : shift.getLoggedShiftData());
    }

}