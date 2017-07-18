package com.skepticalone.mecachecker.data.viewModel;


import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.dao.PayableDaoContract;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public final class PayableModel {

    @NonNull
    private final PayableDaoContract dao;

    PayableModel(@NonNull PayableDaoContract dao) {
        this.dao = dao;
    }

    @MainThread
    public void setPayment(final long id, @NonNull final BigDecimal payment) {
        ItemViewModel.runAsync(new Runnable() {
            @Override
            public void run() {
                dao.setPaymentSync(id, payment);
            }
        });
    }

    @MainThread
    public void setClaimed(final long id, final boolean claimed) {
        ItemViewModel.runAsync(new Runnable() {
            @Override
            public void run() {
                dao.setClaimedSync(id, claimed ? DateTime.now() : null);
            }
        });
    }

    @MainThread
    public void setPaid(final long id, final boolean paid) {
        ItemViewModel.runAsync(new Runnable() {
            @Override
            public void run() {
                dao.setPaidSync(id, paid ? DateTime.now() : null);
            }
        });
    }
}
