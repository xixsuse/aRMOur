package com.skepticalone.mecachecker.data;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

public interface PayableViewModel {

    void setPayment(long id, @NonNull BigDecimal payment);

    void setClaimed(long id, boolean claimed);

    void setPaid(final long id, final boolean paid);

}
