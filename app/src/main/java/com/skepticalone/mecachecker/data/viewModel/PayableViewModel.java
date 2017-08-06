package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.dao.PayableDaoHelper;
import com.skepticalone.mecachecker.data.model.Payable;

import org.joda.time.DateTime;

import java.math.BigDecimal;

abstract class PayableViewModelHelper<Entity extends Payable> {

    @NonNull
    private final PayableDaoHelper<Entity> dao;
    PayableViewModelHelper(@NonNull PayableDaoHelper<Entity> dao) {
        this.dao = dao;
    }

    public final void saveNewPayment(final long id, @NonNull final BigDecimal payment) {
        model.runAsync(new Runnable() {
            @Override
            public void run() {
                model.getDao().
            }
        });
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().setPaymentSync(id, payment);
            }
        });
    }

    public final void setClaimed(final long id, final boolean claimed) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().setClaimedSync(id, claimed ? DateTime.now() : null);
            }
        });
    }

    public final void setPaid(final long id, final boolean paid) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().setPaidSync(id, paid ? DateTime.now() : null);
            }
        });
    }

}
