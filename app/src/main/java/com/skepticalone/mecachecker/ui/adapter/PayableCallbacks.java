package com.skepticalone.mecachecker.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;

public interface PayableCallbacks {
    void changePayment(long itemId, @NonNull BigDecimal payment);
    void changeComment(long itemId, @Nullable String currentComment);
    void setClaimed(long itemId, boolean claimed);
    void setPaid(long itemId, boolean paid);
}