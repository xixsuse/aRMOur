package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.dao.ItemDaoContract;
import com.skepticalone.mecachecker.data.dao.RosteredShiftDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public final class RosteredShiftViewModel extends ShiftAddItemViewModel<RosteredShiftEntity> {

    @NonNull
    private final RosteredShiftDao dao;

    RosteredShiftViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).rosteredShiftDao();
    }

    @NonNull
    @Override
    ItemDaoContract<RosteredShiftEntity> getDao() {
        return dao;
    }

    @Override
    void addNewShift(@NonNull final LocalTime start, @NonNull final LocalTime end) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                DateTime newStart = start.toDateTimeToday();
                final DateTime lastShiftEndTime = dao.getLastShiftEndTimeSync();
                if (lastShiftEndTime != null) {
                    newStart = lastShiftEndTime.withTime(start);
                    while (newStart.isBefore(lastShiftEndTime)) newStart = newStart.plusDays(1);
                }
                DateTime newEnd = newStart.withTime(end);
                while (newEnd.isBefore(newStart)) newEnd = newEnd.plusDays(1);
                ShiftData shiftData = new ShiftData(newStart, newEnd);
                getDao().insertItemSync(new RosteredShiftEntity(shiftData, null, null));
            }
        });
    }

    @MainThread
    public void setShiftTimes(final long id, @NonNull final LocalDate date, @NonNull final LocalTime start, @NonNull final LocalTime end, @Nullable final LocalTime loggedStart, @Nullable final LocalTime loggedEnd) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                final DateTime newStart = date.toDateTime(start);
                DateTime newEnd = date.toDateTime(end);
                while (newEnd.isBefore(newStart)) newEnd = newEnd.plusDays(1);
                DateTime newLoggedStart, newLoggedEnd;
                if (loggedStart == null || loggedEnd == null) {
                    newLoggedStart = newLoggedEnd = null;
                } else {
                    newLoggedStart = date.toDateTime(loggedStart);
                    newLoggedEnd = date.toDateTime(loggedEnd);
                    while (newLoggedEnd.isBefore(newLoggedStart)) newLoggedEnd = newLoggedEnd.plusDays(1);
                }
                dao.setShiftTimesSync(id, newStart, newEnd, newLoggedStart, newLoggedEnd);
//                try {
//                    getDao().setShiftTimesSync(id, newStart, newEnd);
//                } catch (SQLiteConstraintException e) {
//                    throw new ShiftOverlapException(getApplication().getString(R.string.overlapping_shifts));
//                }
            }
        });
    }

}