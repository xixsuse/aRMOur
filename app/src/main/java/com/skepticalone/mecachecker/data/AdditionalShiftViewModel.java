package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.math.BigDecimal;
import java.util.List;

public final class AdditionalShiftViewModel extends AndroidViewModel
        implements AdditionalShiftModel, PayableModel {

    private final AdditionalShiftModel additionalShiftModel;
    private final PayableModel payableModel;

    AdditionalShiftViewModel(Application application) {
        super(application);
        AdditionalShiftDao additionalShiftDao = AppDatabase.getInstance(application).additionalShiftDao();
        additionalShiftModel = new AdditionalShiftComposition(application, additionalShiftDao);
        payableModel = new PayableComposition(application, additionalShiftDao);
    }

    @Override
    public void addNewShift(@NonNull LocalTime start, @NonNull LocalTime end) {
        additionalShiftModel.addNewShift(start, end);
    }

    @NonNull
    @Override
    public LiveData<List<AdditionalShiftEntity>> getItems() {
        return additionalShiftModel.getItems();
    }

    @NonNull
    @Override
    public LiveData<AdditionalShiftEntity> getItem(long id) {
        return additionalShiftModel.getItem(id);
    }

    @Override
    public void selectItem(long id) {
        additionalShiftModel.selectItem(id);
    }

    @NonNull
    @Override
    public LiveData<AdditionalShiftEntity> getSelectedItem() {
        return additionalShiftModel.getSelectedItem();
    }

    @Override
    public void deleteItem(long id) {
        additionalShiftModel.deleteItem(id);
    }

    @Override
    public void setShiftTimes(long id, @NonNull LocalDate date, @NonNull LocalTime start, @NonNull LocalTime end) {
        additionalShiftModel.setShiftTimes(id, date, start, end);
    }

    @Override
    public void setComment(long id, @Nullable String comment) {
        additionalShiftModel.setComment(id, comment);
    }

    @Override
    public void setPayment(long id, @NonNull BigDecimal payment) {
        payableModel.setPayment(id, payment);
    }

    @Override
    public void setClaimed(long id, boolean claimed) {
        payableModel.setClaimed(id, claimed);
    }

    @Override
    public void setPaid(long id, boolean paid) {
        payableModel.setPaid(id, paid);
    }

    static final class AdditionalShiftComposition extends ModelComposition<AdditionalShiftEntity> implements AdditionalShiftModel {

        private final AdditionalShiftDao dao;
        private final String hourlyRateKey;
        private final int defaultHourlyRate;

        AdditionalShiftComposition(Application application, AdditionalShiftDao dao) {
            super(application, dao);
            this.dao = dao;
            hourlyRateKey = application.getString(R.string.key_hourly_rate);
            defaultHourlyRate = application.getResources().getInteger(R.integer.default_hourly_rate);
        }

        @Override
        public void addNewShift(@NonNull final LocalTime start, @NonNull final LocalTime end) {
            final int hourlyRate = PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(hourlyRateKey, defaultHourlyRate);
            runAsync(new SQLiteTask() {
                @Override
                public void runSQLiteTask() throws ShiftOverlapException {
                    DateTime newStart = start.toDateTimeToday();
                    DateTime lastShiftEndTime = dao.getLastShiftEndTime();
                    if (lastShiftEndTime != null) {
                        newStart = lastShiftEndTime.withTime(start);
                        while (newStart.isBefore(lastShiftEndTime)) newStart = newStart.plusDays(1);
                    }
                    DateTime newEnd = newStart.withTime(end);
                    while (newEnd.isBefore(newStart)) newEnd = newEnd.plusDays(1);
                    ShiftData shiftData = new ShiftData(newStart, newEnd);
                    dao.insertItemSync(new AdditionalShiftEntity(new PaymentData(hourlyRate), shiftData, null));
                }
            });
        }

        @Override
        public void setShiftTimes(final long id, @NonNull final LocalDate date, @NonNull final LocalTime start, @NonNull final LocalTime end) {
            runAsync(new SQLiteTask() {
                @Override
                public void runSQLiteTask() throws ShiftOverlapException {
                    final DateTime newStart = date.toDateTime(start);
                    DateTime newEnd = date.toDateTime(end);
                    while (newEnd.isBefore(newStart)) newEnd = newEnd.plusDays(1);
                    try {
                        dao.setShiftTimesSync(id, newStart, newEnd);
                    } catch (SQLiteConstraintException e) {
                        throw new ShiftOverlapException(getApplication().getString(R.string.overlapping_shifts));
                    }
                }
            });
        }
    }
}