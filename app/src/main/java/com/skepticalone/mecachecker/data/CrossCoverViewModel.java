package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;

import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.List;

public final class CrossCoverViewModel extends AndroidViewModel
        implements CrossCoverModel, PayableModel, Model<CrossCoverEntity> {

    private final CrossCoverModel crossCoverModel;
    private final PayableModel payableModel;

    CrossCoverViewModel(Application application) {
        super(application);
        CrossCoverDao crossCoverDao = AppDatabase.getInstance(application).crossCoverDao();
        crossCoverModel = new CrossCoverComposition(application, crossCoverDao);
        payableModel = new PayableComposition(application, crossCoverDao);
    }

    @Override
    public void insertItem(@NonNull CrossCoverEntity item) {
        crossCoverModel.insertItem(item);
    }

    @NonNull
    @Override
    public MutableLiveData<CrossCoverEntity> getLastDeletedItem() {
        return crossCoverModel.getLastDeletedItem();
    }

    @Override
    public void addNewItem() {
        crossCoverModel.addNewItem();
    }

    @NonNull
    @Override
    public LiveData<List<CrossCoverEntity>> getItems() {
        return crossCoverModel.getItems();
    }

    @NonNull
    @Override
    public LiveData<CrossCoverEntity> getItem(long id) {
        return crossCoverModel.getItem(id);
    }

    @Override
    public void selectItem(long id) {
        crossCoverModel.selectItem(id);
    }

    @NonNull
    @Override
    public LiveData<CrossCoverEntity> getSelectedItem() {
        return crossCoverModel.getSelectedItem();
    }

    @Override
    public void deleteItem(long id) {
        crossCoverModel.deleteItem(id);
    }

    @Override
    public void setDate(long id, @NonNull LocalDate date) {
        crossCoverModel.setDate(id, date);
    }

    @Override
    public void setComment(long id, @Nullable String comment) {
        crossCoverModel.setComment(id, comment);
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

    static final class CrossCoverComposition extends ModelComposition<CrossCoverEntity> implements CrossCoverModel {

        private final CrossCoverDao dao;
        private final String newCrossCoverPaymentKey;
        private final int defaultNewCrossCoverPayment;

        CrossCoverComposition(Application application, CrossCoverDao dao) {
            super(application, dao);
            this.dao = dao;
            newCrossCoverPaymentKey = application.getString(R.string.key_cross_cover_payment);
            defaultNewCrossCoverPayment = application.getResources().getInteger(R.integer.default_cross_cover_payment);
        }

        @Override
        public void addNewItem() {
            final int newCrossCoverPayment = PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(newCrossCoverPaymentKey, defaultNewCrossCoverPayment);
            runAsync(new SQLiteTask() {
                @Override
                public void runSQLiteTask() throws ShiftOverlapException {
                    LocalDate newDate = new LocalDate();
                    LocalDate lastCrossCoverShiftDate = dao.getLastCrossCoverDateSync();
                    if (lastCrossCoverShiftDate != null) {
                        LocalDate earliestShiftDate = lastCrossCoverShiftDate.plusDays(1);
                        if (newDate.isBefore(earliestShiftDate)) newDate = earliestShiftDate;
                    }
                    dao.insertItemSync(new CrossCoverEntity(newDate, new PaymentData(newCrossCoverPayment), null));
                }
            });
        }

        @Override
        public void setDate(final long id, @NonNull final LocalDate date) {
            runAsync(new SQLiteTask() {
                @Override
                public void runSQLiteTask() throws ShiftOverlapException {
                    try {
                        dao.setDateSync(id, date);
                    } catch (SQLiteConstraintException e) {
                        throw new ShiftOverlapException(getApplication().getString(R.string.overlapping_shifts));
                    }
                }
            });
        }
    }

}
