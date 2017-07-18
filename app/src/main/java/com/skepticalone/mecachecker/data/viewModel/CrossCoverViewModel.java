package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.CrossCoverDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.util.PaymentData;

import org.joda.time.LocalDate;


public final class CrossCoverViewModel extends SingleAddItemViewModel<CrossCoverEntity, CrossCoverDao> {

    private final PayableModel payableModel;
    private final String newCrossCoverPaymentKey;
    private final int defaultNewCrossCoverPayment;

    CrossCoverViewModel(@NonNull Application application) {
        super(application, AppDatabase.getInstance(application).crossCoverDao());
        payableModel = new PayableModel(getDao());
        newCrossCoverPaymentKey = application.getString(R.string.key_cross_cover_payment);
        defaultNewCrossCoverPayment = application.getResources().getInteger(R.integer.default_cross_cover_payment);
    }

    public final PayableModel getPayableModel() {
        return payableModel;
    }

    @Override
    public void addNewItem() {
        final int newCrossCoverPayment = PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(newCrossCoverPaymentKey, defaultNewCrossCoverPayment);
        runAsync(new Runnable() {
            @Override
            public void run() {
                LocalDate newDate = new LocalDate();
                LocalDate lastCrossCoverShiftDate = getDao().getLastCrossCoverDateSync();
                if (lastCrossCoverShiftDate != null) {
                    LocalDate earliestShiftDate = lastCrossCoverShiftDate.plusDays(1);
                    if (newDate.isBefore(earliestShiftDate)) newDate = earliestShiftDate;
                }
                getDao().insertItemSync(new CrossCoverEntity(newDate, new PaymentData(newCrossCoverPayment), null));
            }
        });
    }

    @MainThread
    public void setDate(final long id, @NonNull final LocalDate date) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().setDateSync(id, date);
//
//                try {
//                    getDao().setDateSync(id, date);
//                } catch (SQLiteConstraintException e) {
//                    throw e;
////                    throw new ShiftOverlapException(getApplication().getString(R.string.overlapping_shifts));
//                }
            }
        });
    }

}
