package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.dao.PayableDaoHelper;

import java.math.BigDecimal;

public final class PayableViewModelHelper {

    @NonNull
    private final PayableDaoHelper dao;

    PayableViewModelHelper(@NonNull PayableDaoHelper dao) {
        this.dao = dao;
    }

    public final void saveNewPayment(final long id, @NonNull final BigDecimal payment) {
        ItemViewModel.runAsync(new Runnable() {
            @Override
            public void run() {
                dao.setPaymentSync(id, payment);
            }
        });
    }

    public final void setClaimed(final long id, final boolean claimed) {
        ItemViewModel.runAsync(new Runnable() {
            @Override
            public void run() {
                dao.setClaimedSync(id, claimed);
            }
        });
    }

    public final void setPaid(final long id, final boolean paid) {
        ItemViewModel.runAsync(new Runnable() {
            @Override
            public void run() {
                dao.setPaidSync(id, paid);
            }
        });
    }

}
