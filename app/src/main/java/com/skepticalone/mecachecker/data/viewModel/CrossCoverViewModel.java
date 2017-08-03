package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.CrossCoverDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.util.PaymentData;

import org.joda.time.LocalDate;

public final class CrossCoverViewModel extends PayableViewModel<CrossCoverEntity, CrossCoverDao>
        implements SingleAddViewModelContract<CrossCoverEntity>, DateViewModelContract<CrossCoverEntity> {

    private final String newCrossCoverPaymentKey;
    private final int defaultNewCrossCoverPayment;

    public CrossCoverViewModel(Application application) {
        super(application);
        newCrossCoverPaymentKey = application.getString(R.string.key_cross_cover_payment);
        defaultNewCrossCoverPayment = application.getResources().getInteger(R.integer.default_cross_cover_payment);
    }

    @NonNull
    @Override
    CrossCoverDao onCreateDao(@NonNull AppDatabase database) {
        return database.crossCoverDao();
    }

    @Override
    public void saveNewDate(@NonNull final CrossCoverEntity item, @NonNull final LocalDate date) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    getDao().setDateSync(item.getId(), date);
                } catch (SQLiteConstraintException e) {
                    postOverlappingShifts();
                }
            }
        });
    }

    @Override
    public void addNewItem() {
        runAsync(new Runnable() {
            @Override
            public void run() {
                synchronized (CrossCoverViewModel.this) {
                    postSelectedId(getDao().insertItemSync(new CrossCoverEntity(
                            CrossCoverEntity.getNewDate(getDao().getLastCrossCoverDateSync()),
                            PaymentData.fromPayment(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(newCrossCoverPaymentKey, defaultNewCrossCoverPayment)),
                            null
                    )));
                }
            }
        });
    }
}
