package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

public interface PayableDetailFragmentCallbacks {

    void changePayment(long id, @NonNull BigDecimal payment);

    void setClaimed(long id, boolean claimed);

    void setPaid(long id, boolean paid);

}
