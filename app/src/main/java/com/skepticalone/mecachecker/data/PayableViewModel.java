package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.skepticalone.mecachecker.model.PayableItem;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public abstract class PayableViewModel<Entity extends PayableItem> extends ItemViewModel<Entity> {
    PayableViewModel(Application application) {
        super(application);
    }

    @MainThread
    public final void setPayment(final long id, @NonNull final BigDecimal payment) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setPaymentSync(id, payment);
            }
        }).start();
    }

    @MainThread
    public final void setComment(final long id, @Nullable final String comment) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setCommentSync(id, comment);
            }
        }).start();
    }

    @MainThread
    public final void setClaimed(final long id, final boolean claimed) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setClaimedSync(id, claimed ? DateTime.now() : null);
            }
        }).start();
    }

    @MainThread
    public final void setPaid(final long id, final boolean paid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setPaidSync(id, paid ? DateTime.now() : null);
            }
        }).start();
    }

    @WorkerThread
    abstract void setPaymentSync(long id, @NonNull BigDecimal payment);

    @WorkerThread
    abstract void setCommentSync(long id, @Nullable String comment);

    @WorkerThread
    abstract void setClaimedSync(long id, @Nullable DateTime claimed);

    @WorkerThread
    abstract void setPaidSync(long id, @Nullable DateTime paid);

}
