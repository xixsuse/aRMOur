package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.dao.PayableDaoContract;

import org.joda.time.DateTime;

import java.math.BigDecimal;

final class PayableHelper {

    @NonNull
    private final PayableDaoContract dao;

    PayableHelper(@NonNull PayableDaoContract dao) {
        this.dao = dao;
    }

    void saveNewPayment(final long id, @NonNull final BigDecimal payment) {
        ItemViewModel.runAsync(new Runnable() {
            @Override
            public void run() {
                dao.setPaymentSync(id, payment);
            }
        });
    }

    void setClaimed(final long id, final boolean claimed) {
        ItemViewModel.runAsync(new Runnable() {
            @Override
            public void run() {
                dao.setClaimedSync(id, claimed ? DateTime.now() : null);

            }
        });
    }

    void setPaid(final long id, final boolean paid) {
        ItemViewModel.runAsync(new Runnable() {
            @Override
            public void run() {
                dao.setPaidSync(id, paid ? DateTime.now() : null);

            }
        });
    }
}
