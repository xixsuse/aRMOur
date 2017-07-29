package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.CrossCoverDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;
import com.skepticalone.mecachecker.data.util.PaymentData;

import org.joda.time.LocalDate;

import java.math.BigDecimal;


public final class CrossCoverViewModel extends ItemViewModel<CrossCoverEntity, CrossCoverDao>
        implements SingleAddViewModelContract<CrossCoverEntity>, PayableViewModelContract<CrossCoverEntity> {

    private final PayableHelper payableHelper;
    private final String newCrossCoverPaymentKey;
    private final int defaultNewCrossCoverPayment;

    public CrossCoverViewModel(Application application) {
        super(application);
        payableHelper = new PayableHelper(getDao());
        newCrossCoverPaymentKey = application.getString(R.string.key_cross_cover_payment);
        defaultNewCrossCoverPayment = application.getResources().getInteger(R.integer.default_cross_cover_payment);
    }

    @NonNull
    @Override
    CrossCoverDao onCreateDao(@NonNull AppDatabase database) {
        return database.crossCoverDao();
    }

    @Override
    public void saveNewPayment(@NonNull BigDecimal payment) {
        payableHelper.saveNewPayment(getCurrentItem(), payment);
    }

    @Override
    public void setClaimed(boolean claimed) {
        payableHelper.setClaimed(getCurrentItem(), claimed);
    }

    @Override
    public void setPaid(boolean paid) {
        payableHelper.setPaid(getCurrentItem(), paid);
    }

    public void saveNewDate(@NonNull LocalDate newDate) {
        CrossCoverEntity CrossCover = getCurrentItem().getValue();
        if (CrossCover != null) {
            getDao().setDateSync(CrossCover.getId(), newDate);
        }
    }

    @Override
    public void addNewItem() {
        LocalDate newDate = new LocalDate();
        LocalDate lastCrossCoverShiftDate = getDao().getLastCrossCoverDateSync();
        if (lastCrossCoverShiftDate != null) {
            LocalDate earliestShiftDate = lastCrossCoverShiftDate.plusDays(1);
            if (newDate.isBefore(earliestShiftDate)) newDate = earliestShiftDate;
        }
        int newCrossCoverPayment = PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(newCrossCoverPaymentKey, defaultNewCrossCoverPayment);
        insertItem(new CrossCoverEntity(newDate, new PaymentData(newCrossCoverPayment), null));
    }
}
