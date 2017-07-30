package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.dao.PayableDaoContract;
import com.skepticalone.mecachecker.data.model.Item;

import org.joda.time.DateTime;

import java.math.BigDecimal;

abstract class PayableViewModel<Entity extends Item, Dao extends PayableDaoContract<Entity>> extends ItemViewModel<Entity, Dao> implements PayableViewModelContract<Entity> {

    PayableViewModel(Application application) {
        super(application);
    }

    @Override
    public final void saveNewPayment(final long id, @NonNull final BigDecimal payment) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().setPaymentSync(id, payment);
            }
        });
    }

    @Override
    public final void setClaimed(final long id, final boolean claimed) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().setClaimedSync(id, claimed ? DateTime.now() : null);
            }
        });
    }

    @Override
    public final void setPaid(final long id, final boolean paid) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().setPaidSync(id, paid ? DateTime.now() : null);
            }
        });
    }
}
