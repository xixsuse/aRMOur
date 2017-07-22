package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.dao.ItemDaoContract;
import com.skepticalone.mecachecker.data.dao.RosteredShiftDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

public final class RosteredShiftViewModel extends ShiftAddItemViewModel<RosteredShiftEntity> {

    @NonNull
    private final RosteredShiftDao dao;
    private final LiveData<List<RosteredShiftEntity>> items;

    RosteredShiftViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).rosteredShiftDao();
        items = Transformations.map(dao.getItems(), new Function<List<RosteredShiftEntity>, List<RosteredShiftEntity>>() {
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
    ItemDaoContract<RosteredShiftEntity> getDao() {
        return dao;
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
                if (shifts == null) return null;
                for (int i = 0; i < shifts.size(); i++) {
                    RosteredShiftEntity shift = shifts.get(i);
                    if (shift.getId() == id) return shift;
                }
                throw new IllegalStateException();
            }
        });
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
            dao.insertItemSync(new RosteredShiftEntity(createNewShiftData(start, end, dao.getLastShiftEndTimeSync()), null, null));
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
            final DateTime newStart = date.toDateTime(start), newEnd = getEnd(newStart, end), newLoggedStart, newLoggedEnd;
            if (loggedStart == null || loggedEnd == null) {
                newLoggedStart = newLoggedEnd = null;
            } else {
                newLoggedStart = date.toDateTime(loggedStart);
                newLoggedEnd = getEnd(newLoggedStart, loggedEnd);
            }
            dao.setShiftTimesSync(id, newStart, newEnd, newLoggedStart, newLoggedEnd);
        }
    }

}