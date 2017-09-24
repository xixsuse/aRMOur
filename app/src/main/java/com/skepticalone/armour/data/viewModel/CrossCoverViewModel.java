package com.skepticalone.armour.data.viewModel;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.dao.CrossCoverDao;
import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.model.RawCrossCoverEntity;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

public final class CrossCoverViewModel extends ItemViewModel<RawCrossCoverEntity> implements DateViewModelContract<RawCrossCoverEntity>, PayableViewModelContract<RawCrossCoverEntity>, SingleAddItemViewModelContract<RawCrossCoverEntity> {

    @NonNull
    private final PayableViewModelHelper payableViewModelHelper;

    public CrossCoverViewModel(@NonNull Application application) {
        super(application);
        this.payableViewModelHelper = new PayableViewModelHelper(getDao().getPayableDaoHelper());
    }

    @NonNull
    @Override
    CrossCoverDao getDao() {
        return AppDatabase.getInstance(getApplication()).crossCoverDao();
    }

    @Override
    public void addNewItem() {
        runAsync(new Runnable() {
            @Override
            public void run() {
                postSelectedId(getDao().insertSync(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(getApplication().getString(R.string.key_default_cross_cover_payment), getApplication().getResources().getInteger(R.integer.default_cross_cover_payment))));
            }
        });
    }

    @Override
    public void setClaimed(boolean claimed) {
        payableViewModelHelper.setClaimed(getCurrentItemId(), claimed);
    }

    @Override
    public void setPaid(boolean paid) {
        payableViewModelHelper.setPaid(getCurrentItemId(), paid);
    }

    @Override
    public void saveNewPayment(@NonNull BigDecimal payment) {
        payableViewModelHelper.saveNewPayment(getCurrentItemId(), payment);
    }

    @Override
    public void saveNewDate(@NonNull final LocalDate date) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    getDao().setDateSync(getCurrentItemId(), date);
                } catch (SQLiteConstraintException e) {
                    postOverlappingShifts();
                }
            }
        });
    }

}
