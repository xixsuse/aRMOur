package com.skepticalone.mecachecker.data;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import java.math.BigDecimal;

public interface PayableItemViewModel<T> extends BaseItemViewModel<T> {

    @MainThread
    void setPayment(long id, @NonNull BigDecimal payment);

    @MainThread
    void setClaimed(long id, boolean claimed);

    @MainThread
    void setPaid(final long id, final boolean paid);

}
