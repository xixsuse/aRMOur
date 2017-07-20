package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.CrossCoverDao;
import com.skepticalone.mecachecker.data.dao.ItemDaoContract;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.util.PaymentData;

import org.joda.time.LocalDate;


public final class CrossCoverViewModel extends SingleAddItemViewModel<CrossCoverEntity> {

    @NonNull
    private final CrossCoverDao dao;
    @NonNull
    private final PayableModel payableModel;
    private final String newCrossCoverPaymentKey;
    private final int defaultNewCrossCoverPayment;

    CrossCoverViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).crossCoverDao();
        payableModel = new PayableModel(dao);
        newCrossCoverPaymentKey = application.getString(R.string.key_cross_cover_payment);
        defaultNewCrossCoverPayment = application.getResources().getInteger(R.integer.default_cross_cover_payment);
    }
    @NonNull
    @Override
    ItemDaoContract<CrossCoverEntity> getDao() {
        return dao;
    }
    @NonNull
    public PayableModel getPayableModel() {
        return payableModel;
    }
    @Override
    public void addNewItem() {
        int newCrossCoverPayment = PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(newCrossCoverPaymentKey, defaultNewCrossCoverPayment);
        runAsync(new InsertItemTask(dao, newCrossCoverPayment));
    }
    @MainThread
    public void setDate(long id, @NonNull LocalDate date) {
        runAsync(new SetDateTask(dao, id, errorMessage, date));
    }
    static final class InsertItemTask extends DaoRunnable<CrossCoverDao> {
        private final int newCrossCoverPayment;
        InsertItemTask(@NonNull CrossCoverDao crossCoverDao, int newCrossCoverPayment) {
            super(crossCoverDao);
            this.newCrossCoverPayment = newCrossCoverPayment;
        }
        @Override
        void run(@NonNull CrossCoverDao dao) {
            LocalDate newDate = new LocalDate();
            LocalDate lastCrossCoverShiftDate = dao.getLastCrossCoverDateSync();
            if (lastCrossCoverShiftDate != null) {
                LocalDate earliestShiftDate = lastCrossCoverShiftDate.plusDays(1);
                if (newDate.isBefore(earliestShiftDate)) newDate = earliestShiftDate;
            }
            dao.insertItemSync(new CrossCoverEntity(newDate, new PaymentData(newCrossCoverPayment), null));
        }
    }
    static final class SetDateTask extends OverlapItemRunnable<CrossCoverDao> {
        @NonNull
        private final LocalDate date;
        SetDateTask(@NonNull CrossCoverDao crossCoverDao, long id, @NonNull ErrorMessageObservable errorMessage, @NonNull LocalDate date) {
            super(crossCoverDao, id, errorMessage);
            this.date = date;
        }
        @Override
        void runOrThrow(@NonNull CrossCoverDao dao, long id) throws SQLiteConstraintException {
            dao.setDateSync(id, date);
        }
    }

}
