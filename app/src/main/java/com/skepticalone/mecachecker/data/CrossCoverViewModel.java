package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.List;

public final class CrossCoverViewModel extends ItemViewModel<CrossCoverEntity>
        implements PayableItemViewModel<CrossCoverEntity>, SingleAddItemViewModel<CrossCoverEntity> {
    private final CrossCoverDao crossCoverDao;
    private final String errorMessage;
    private final int newCrossCoverPayment;

    CrossCoverViewModel(Application application) {
        super(application);
        crossCoverDao = AppDatabase.getInstance(application).crossCoverDao();
        errorMessage = application.getString(R.string.cross_cover_already_exists_date);
        newCrossCoverPayment = PreferenceManager.getDefaultSharedPreferences(application).getInt(application.getString(R.string.key_cross_cover_payment), application.getResources().getInteger(R.integer.default_cross_cover_payment));
    }

    @Override
    public LiveData<List<CrossCoverEntity>> getItems() {
        return crossCoverDao.getCrossCoverShifts();
    }

    @Override
    public LiveData<CrossCoverEntity> getItem(long id) {
        return crossCoverDao.getCrossCoverShift(id);
    }

    @Override
    public void addNewItem() {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                LocalDate newDate = new LocalDate();
                LocalDate lastCrossCoverShiftDate = crossCoverDao.getLastCrossCoverShiftDate();
                if (lastCrossCoverShiftDate != null) {
                    LocalDate earliestShiftDate = lastCrossCoverShiftDate.plusDays(1);
                    if (newDate.isBefore(earliestShiftDate)) newDate = earliestShiftDate;
                }
                crossCoverDao.insertCrossCoverShift(new CrossCoverEntity(
                        newDate,
                        new PaymentData(MoneyConverter.centsToMoney(newCrossCoverPayment), null, null),
                        null
                ));
            }
        });
    }

    @Override
    public void deleteItem(final long id) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                crossCoverDao.deleteCrossCoverShift(id);
            }
        });
    }

    @Override
    public void setPayment(final long id, @NonNull final BigDecimal payment) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                crossCoverDao.setPayment(id, payment);
            }
        });
    }

    @Override
    public void setClaimed(final long id, final boolean claimed) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                crossCoverDao.setClaimed(id, claimed ? DateTime.now() : null);
            }
        });
    }

    @Override
    public void setPaid(final long id, final boolean paid) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                crossCoverDao.setPaid(id, paid ? DateTime.now() : null);
            }
        });
    }

    @Override
    public void setComment(final long id, @Nullable final String comment) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                crossCoverDao.setComment(id, comment);
            }
        });
    }

    public void setDate(final long id, @NonNull final LocalDate date) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                try {
                    crossCoverDao.setDate(id, date);
                } catch (SQLiteConstraintException e) {
                    throw new ShiftOverlapException(errorMessage);
                }
            }
        });
    }

}
