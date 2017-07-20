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
    public void setPayment(long id, @NonNull BigDecimal payment) {
        ItemViewModel.runAsync(new SetPaymentTask(dao, id, payment));
    }

    @MainThread
    public void setClaimed(long id, boolean claimed) {
        ItemViewModel.runAsync(new SetClaimedTask(dao, id, claimed));
    }

    @MainThread
    public void setPaid(long id, boolean paid) {
        ItemViewModel.runAsync(new SetPaidTask(dao, id, paid));
    }
    static final class SetPaymentTask extends ItemViewModel.ItemRunnable<PayableDaoContract> {
        @NonNull
        private final BigDecimal payment;
        SetPaymentTask(@NonNull PayableDaoContract dao, long id, @NonNull BigDecimal payment) {
            super(dao, id);
            this.payment = payment;
        }
        @Override
        void run(@NonNull PayableDaoContract dao, long id) {
            dao.setPaymentSync(id, payment);
        }
    }
    static final class SetClaimedTask extends ItemViewModel.ItemRunnable<PayableDaoContract> {
        private boolean claimed;
        SetClaimedTask(@NonNull PayableDaoContract dao, long id, boolean claimed) {
            super(dao, id);
            this.claimed = claimed;
        }
        @Override
        void run(@NonNull PayableDaoContract dao, long id) {
            dao.setClaimedSync(id, claimed ? DateTime.now() : null);
        }
    }
    static final class SetPaidTask extends ItemViewModel.ItemRunnable<PayableDaoContract> {
        private boolean paid;
        SetPaidTask(@NonNull PayableDaoContract dao, long id, boolean paid) {
            super(dao, id);
            this.paid = paid;
        }
        @Override
        void run(@NonNull PayableDaoContract dao, long id) {
            dao.setPaidSync(id, paid ? DateTime.now() : null);
        }
    }
}
