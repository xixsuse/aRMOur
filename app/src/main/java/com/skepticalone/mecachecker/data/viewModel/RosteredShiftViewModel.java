package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
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
    void addNewShift(@NonNull LocalTime start, @NonNull LocalTime end) {
        runAsync(new InsertShiftTask(dao, start, end));
    }

    @MainThread
    public void setShiftTimes(long id, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end, @Nullable LocalTime loggedStart, @Nullable LocalTime loggedEnd) {
        runAsync(new SetShiftTimesTask(dao, id, errorMessage, date, start, end, loggedStart, loggedEnd));
    }

    static final class InsertShiftTask extends DaoRunnable<RosteredShiftDao> {
        @NonNull
        private final LocalTime start, end;
        InsertShiftTask(@NonNull RosteredShiftDao rosteredShiftDao, @NonNull LocalTime start, @NonNull LocalTime end) {
            super(rosteredShiftDao);
            this.start = start;
            this.end = end;
        }
        @Override
        void run(@NonNull RosteredShiftDao dao) {
            DateTime newStart = start.toDateTimeToday();
            final DateTime lastShiftEndTime = dao.getLastShiftEndTimeSync();
            if (lastShiftEndTime != null) {
                newStart = lastShiftEndTime.withTime(start);
                while (newStart.isBefore(lastShiftEndTime)) newStart = newStart.plusDays(1);
            }
            DateTime newEnd = newStart.withTime(end);
            while (newEnd.isBefore(newStart)) newEnd = newEnd.plusDays(1);
            ShiftData shiftData = new ShiftData(newStart, newEnd);
            dao.insertItemSync(new RosteredShiftEntity(shiftData, null, null));
        }
    }
    static final class SetShiftTimesTask extends OverlapItemRunnable<RosteredShiftDao> {
        @NonNull
        private final LocalDate date;
        @NonNull
        private final LocalTime start, end;
        @Nullable
        private final LocalTime loggedStart, loggedEnd;
        SetShiftTimesTask(@NonNull RosteredShiftDao rosteredShiftDao, long id, @NonNull ErrorMessageObservable errorMessage, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end, @Nullable LocalTime loggedStart, @Nullable LocalTime loggedEnd) {
            super(rosteredShiftDao, id, errorMessage);
            this.date = date;
            this.start = start;
            this.end = end;
            this.loggedStart = loggedStart;
            this.loggedEnd = loggedEnd;
        }
        @Override
        void runOrThrow(@NonNull RosteredShiftDao dao, long id) throws SQLiteConstraintException {
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
        }
    }

}