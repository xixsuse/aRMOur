package com.skepticalone.mecachecker.data;


import android.app.Application;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.math.BigDecimal;

final class PayableComposition extends Composition implements PayableViewModel {

    private final PayableDao dao;

    PayableComposition(Application application, PayableDao dao) {
        super(application);
        this.dao = dao;
    }

    @Override
    public void setPayment(final long id, @NonNull final BigDecimal payment) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                dao.setPaymentSync(id, payment);
            }
        });
    }

    @Override
    public void setClaimed(final long id, final boolean claimed) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                dao.setClaimedSync(id, claimed ? DateTime.now() : null);
            }
        });
    }

    @Override
    public void setPaid(final long id, final boolean paid) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                dao.setPaidSync(id, paid ? DateTime.now() : null);
            }
        });
    }
}
