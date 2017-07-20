package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.AdditionalShiftDao;
import com.skepticalone.mecachecker.data.dao.ItemDaoContract;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.util.PaymentData;
import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public final class AdditionalShiftViewModel extends ShiftAddItemViewModel<AdditionalShiftEntity> {

    @NonNull
    private final AdditionalShiftDao dao;
    @NonNull
    private final PayableModel payableModel;
    private final String hourlyRateKey;
    private final int defaultHourlyRate;

    AdditionalShiftViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).additionalShiftDao();
        payableModel = new PayableModel(dao);
        hourlyRateKey = application.getString(R.string.key_hourly_rate);
        defaultHourlyRate = application.getResources().getInteger(R.integer.default_hourly_rate);
    }

    @NonNull
    @Override
    ItemDaoContract<AdditionalShiftEntity> getDao() {
        return dao;
    }

    @NonNull
    public PayableModel getPayableModel() {
        return payableModel;
    }

    @Override
    void addNewShift(@NonNull LocalTime start, @NonNull LocalTime end) {
        int hourlyRate = PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(hourlyRateKey, defaultHourlyRate);
        runAsync(new InsertShiftTask(dao, start, end, hourlyRate));
    }

    @MainThread
    public void setShiftTimes(long id, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end) {
        runAsync(new SetShiftTimesTask(dao, id, errorMessage, date, start, end));
    }

    static final class InsertShiftTask extends DaoRunnable<AdditionalShiftDao> {
        @NonNull
        private final LocalTime start, end;
        private final int hourlyRate;
        InsertShiftTask(@NonNull AdditionalShiftDao additionalShiftDao, @NonNull LocalTime start, @NonNull LocalTime end, int hourlyRate) {
            super(additionalShiftDao);
            this.start = start;
            this.end = end;
            this.hourlyRate = hourlyRate;
        }
        @Override
        void run(@NonNull AdditionalShiftDao dao) {
            DateTime newStart = start.toDateTimeToday();
            final DateTime lastShiftEndTime = dao.getLastShiftEndTimeSync();
            if (lastShiftEndTime != null) {
                newStart = lastShiftEndTime.withTime(start);
                while (newStart.isBefore(lastShiftEndTime)) newStart = newStart.plusDays(1);
            }
            DateTime newEnd = newStart.withTime(end);
            while (newEnd.isBefore(newStart)) newEnd = newEnd.plusDays(1);
            ShiftData shiftData = new ShiftData(newStart, newEnd);
            dao.insertItemSync(new AdditionalShiftEntity(new PaymentData(hourlyRate), shiftData, null));
        }
    }
    static final class SetShiftTimesTask extends OverlapItemRunnable<AdditionalShiftDao> {
        @NonNull
        private final LocalDate date;
        @NonNull
        private final LocalTime start, end;
        SetShiftTimesTask(@NonNull AdditionalShiftDao additionalShiftDao, long id, @NonNull ErrorMessageObservable errorMessage, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end) {
            super(additionalShiftDao, id, errorMessage);
            this.date = date;
            this.start = start;
            this.end = end;
        }
        @Override
        void runOrThrow(@NonNull AdditionalShiftDao dao, long id) throws SQLiteConstraintException {
            final DateTime newStart = date.toDateTime(start);
            DateTime newEnd = date.toDateTime(end);
            while (newEnd.isBefore(newStart)) newEnd = newEnd.plusDays(1);
            dao.setShiftTimesSync(id, newStart, newEnd);
        }
    }

}