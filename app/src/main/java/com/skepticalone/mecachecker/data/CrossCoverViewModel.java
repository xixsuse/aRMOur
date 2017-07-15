package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

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
    BaseItemDao<CrossCoverEntity> getDao() {
        return crossCoverDao;
    }

    @Override
    public void addNewItem() {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                LocalDate newDate = new LocalDate();
                LocalDate lastCrossCoverShiftDate = crossCoverDao.getLastCrossCoverDateSync();
                if (lastCrossCoverShiftDate != null) {
                    LocalDate earliestShiftDate = lastCrossCoverShiftDate.plusDays(1);
                    if (newDate.isBefore(earliestShiftDate)) newDate = earliestShiftDate;
                }
                crossCoverDao.insertItemSync(new CrossCoverEntity(
                        newDate,
                        new PaymentData(MoneyConverter.centsToMoney(newCrossCoverPayment), null, null),
                        null
                ));
            }
        });
    }

    @Override
    public void setPayment(final long id, @NonNull final BigDecimal payment) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                crossCoverDao.setPaymentSync(id, payment);
            }
        });
    }

    @Override
    public void setClaimed(final long id, final boolean claimed) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                crossCoverDao.setClaimedSync(id, claimed ? DateTime.now() : null);
            }
        });
    }

    @Override
    public void setPaid(final long id, final boolean paid) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                crossCoverDao.setPaidSync(id, paid ? DateTime.now() : null);
            }
        });
    }

    public void setDateSync(final long id, @NonNull final LocalDate date) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                try {
                    crossCoverDao.setDateSync(id, date);
                } catch (SQLiteConstraintException e) {
                    throw new ShiftOverlapException(errorMessage);
                }
            }
        });
    }

}
