package com.skepticalone.armour.data.viewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.BoolRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.dao.RosteredShiftDao;
import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.entity.RosteredShiftEntity;
import com.skepticalone.armour.data.entity.ShiftData;
import com.skepticalone.armour.util.ShiftUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

public final class RosteredShiftViewModel extends ItemViewModel<RosteredShiftEntity> implements ShiftViewModelContract<RosteredShiftEntity>, RosteredShiftEntity.ComplianceChecker.Callbacks {

    @NonNull
    private final LiveData<List<RosteredShiftEntity>> items;

    public RosteredShiftViewModel(@NonNull Application application) {
        super(application);
        items = Transformations.map(getDao().getItems(), new RosteredShiftEntity.ComplianceChecker(this));
    }

    @NonNull
    @Override
    RosteredShiftDao getDao() {
        return AppDatabase.getInstance(getApplication()).rosteredShiftDao();
    }

    @NonNull
    @Override
    public LiveData<List<RosteredShiftEntity>> getItems() {
        return items;
    }

    @NonNull
    @Override
    LiveData<RosteredShiftEntity> fetchItem(final long id) {
        return Transformations.map(items, new Function<List<RosteredShiftEntity>, RosteredShiftEntity>() {
            @Override
            public RosteredShiftEntity apply(List<RosteredShiftEntity> shifts) {
                if (shifts != null) {
                    for (RosteredShiftEntity shift : shifts) {
                        if (shift.getId() == id) return shift;
                    }
                }
                return null;
            }
        });
    }

    private boolean skipWeekends(@NonNull final ShiftUtil.ShiftType shiftType) {
        @StringRes final int skipWeekendsKey;
        @BoolRes final int defaultSkipWeekends;
        if (shiftType == ShiftUtil.ShiftType.NORMAL_DAY) {
            skipWeekendsKey = R.string.key_skip_weekend_normal_day;
            defaultSkipWeekends = R.bool.default_skip_weekend_normal_day;
        } else if (shiftType == ShiftUtil.ShiftType.LONG_DAY) {
            skipWeekendsKey = R.string.key_skip_weekend_long_day;
            defaultSkipWeekends = R.bool.default_skip_weekend_long_day;
        } else if (shiftType == ShiftUtil.ShiftType.NIGHT_SHIFT) {
            skipWeekendsKey = R.string.key_skip_weekend_night_shift;
            defaultSkipWeekends = R.bool.default_skip_weekend_night_shift;
        } else throw new IllegalStateException();
        return getBooleanPreference(skipWeekendsKey, defaultSkipWeekends);
    }

    @Override
    public void addNewShift(@NonNull final ShiftUtil.ShiftType shiftType) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                ShiftUtil.Calculator calculator = ShiftUtil.Calculator.getInstance(getApplication());
                postSelectedId(getDao().insertSync(
                        calculator.getStartTime(shiftType),
                        calculator.getEndTime(shiftType),
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
    private void saveNewShiftTimes(final long id, @NonNull final ShiftData shiftData, @Nullable final ShiftData loggedShiftData) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    getDao().setShiftTimesSync(id, shiftData.getStart(), shiftData.getEnd(), loggedShiftData == null ? null : loggedShiftData.getStart(), loggedShiftData == null ? null : loggedShiftData.getEnd());
                } catch (SQLiteConstraintException e) {
                    postOverlappingShifts();
                }
            }
        });
    }

    @Override
    public void saveNewDate(@NonNull LocalDate date) {
        RosteredShiftEntity shift = getCurrentItem().getValue();
        if (shift == null) throw new IllegalStateException();
        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewDate(date), shift.getLoggedShiftData() == null ? null : shift.getLoggedShiftData().withNewDate(date));
    }

    public void saveNewTime(@NonNull LocalTime time, boolean start, boolean logged) {
        RosteredShiftEntity shift = getCurrentItem().getValue();
        if (shift == null || (logged && shift.getLoggedShiftData() == null)) throw new IllegalStateException();
        saveNewShiftTimes(shift.getId(), logged ? shift.getShiftData() : shift.getShiftData().withNewTime(time, start), logged ? shift.getLoggedShiftData().withNewTime(time, start) : shift.getLoggedShiftData());
    }

    private boolean getBooleanPreference(@StringRes int preferenceKey, @BoolRes int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(getApplication()).getBoolean(getApplication().getString(preferenceKey), getApplication().getResources().getBoolean(defaultValue));
    }

    @Override
    public boolean checkDurationOverDay() {
        return getBooleanPreference(R.string.key_check_duration_over_day, R.bool.default_check_duration_over_day);
    }

    @Override
    public boolean checkDurationOverWeek() {
        return getBooleanPreference(R.string.key_check_duration_over_week, R.bool.default_check_duration_over_week);
    }

    @Override
    public boolean checkDurationOverFortnight() {
        return getBooleanPreference(R.string.key_check_duration_over_fortnight, R.bool.default_check_duration_over_fortnight);
    }

    @Override
    public boolean checkDurationBetweenShifts() {
        return getBooleanPreference(R.string.key_check_duration_between_shifts, R.bool.default_check_duration_between_shifts);
    }

    @Override
    public boolean checkConsecutiveWeekends() {
        return getBooleanPreference(R.string.key_check_consecutive_weekends, R.bool.default_check_consecutive_weekends);
    }
}