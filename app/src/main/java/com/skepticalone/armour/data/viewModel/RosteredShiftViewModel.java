package com.skepticalone.armour.data.viewModel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.dao.RosteredShiftDao;
import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.RosteredShiftEntity;
import com.skepticalone.armour.data.model.RosteredShiftList;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.data.model.ShiftData;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.List;

public final class RosteredShiftViewModel extends ItemViewModel<RosteredShiftEntity, RosteredShift> implements ShiftViewModelContract<RosteredShift> {

    @NonNull
    private final LiveData<List<RosteredShift>> rosteredShifts;

    public RosteredShiftViewModel(@NonNull Application application) {
        super(application);
        rosteredShifts = new RosteredShiftList(application, getDao().fetchItems());
    }

    @NonNull
    @Override
    RosteredShiftDao getDao() {
        return AppDatabase.getInstance(getApplication()).rosteredShiftDao();
    }

    @Override
    public void addNewShift(@NonNull final Shift.ShiftType shiftType) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                postCurrentItemId(getDao().insertSync(
                        shiftType.getTimes(getFreshShiftConfiguration()),
                        getFreshTimezone(),
                        shiftType.skipWeekends(getApplication())
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
        RosteredShift shift = getCurrentItem().getValue();
        if (shift == null) throw new IllegalStateException();
        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewDate(date), shift.getLoggedShiftData() == null ? null : shift.getLoggedShiftData().withNewDate(date));
    }

    public void saveNewTime(@NonNull LocalTime time, boolean isStart, boolean isLogged) {
        RosteredShift shift = getCurrentItem().getValue();
        if (shift == null || isLogged && shift.getLoggedShiftData() == null)
            throw new IllegalStateException();
        saveNewShiftTimes(shift.getId(), isLogged ? shift.getShiftData().toRawData() : shift.getShiftData().withNewTime(time, isStart), isLogged ? shift.getLoggedShiftData().withNewTime(time, isStart) : shift.getLoggedShiftData() == null ? null : shift.getLoggedShiftData().toRawData());
    }

    @NonNull
    @Override
    public LiveData<List<RosteredShift>> getItems() {
        return rosteredShifts;
    }

    @Override
    int getQuantityStringResource() {
        return R.plurals.rostered_shifts;
    }

}