package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.RosteredShiftDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

public final class RosteredShiftViewModel extends ItemViewModel<RosteredShiftEntity, RosteredShiftDao> implements ShiftViewModelContract<RosteredShiftEntity> {

    private final LiveData<List<RosteredShiftEntity>> items;

    public RosteredShiftViewModel(@NonNull Application application) {
        super(application);
        items = Transformations.map(getDao().getItems(), new Function<List<RosteredShiftEntity>, List<RosteredShiftEntity>>() {
            @Override
            public List<RosteredShiftEntity> apply(List<RosteredShiftEntity> shifts) {
                for (int i = 0, count = shifts.size(); i < count; i++) {
                    shifts.get(i).setup(shifts, i);
                }
                return shifts;
            }
        });
    }

    @NonNull
    @Override
    RosteredShiftDao onCreateDao(@NonNull AppDatabase database) {
        return database.rosteredShiftDao();
    }

    @NonNull
    @Override
    public LiveData<List<RosteredShiftEntity>> getItems() {
        return items;
    }

    @NonNull
    @Override
    LiveData<RosteredShiftEntity> getItem(final long id) {
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

    @Override
    public void addNewShift(@NonNull final ShiftUtil.ShiftType shiftType) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                selectedId.postValue(getDao().insertItemSync(new RosteredShiftEntity(
                        ShiftData.withEarliestStartAfterMinimumDurationBetweenShifts(ShiftUtil.Calculator.getInstance(getApplication()).getStartTime(shiftType), ShiftUtil.Calculator.getInstance(getApplication()).getEndTime(shiftType), getDao().getLastShiftEndSync()),
                        null,
                        null
                )));

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
                    errorMessage.postValue(R.string.overlapping_shifts);
                }
            }
        });
    }

    @Override
    public void saveNewDate(@NonNull LocalDate date) {
        RosteredShiftEntity shift = getCurrentItemSync();
        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewDate(date), shift.getLoggedShiftData() == null ? null : shift.getLoggedShiftData().withNewDate(date));
    }

    @MainThread
    public void saveNewTime(@NonNull LocalTime time, boolean start, boolean logged) {
        RosteredShiftEntity shift = getCurrentItemSync();
        if (logged && shift.getLoggedShiftData() == null) throw new IllegalStateException();
        saveNewShiftTimes(shift.getId(), logged ? shift.getShiftData() : shift.getShiftData().withNewTime(time, start), logged ? shift.getLoggedShiftData().withNewTime(time, start) : shift.getLoggedShiftData());
    }

}