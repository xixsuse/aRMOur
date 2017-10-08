package com.skepticalone.armour.data.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.Instant;

import java.math.BigDecimal;

@SuppressWarnings("NullableProblems")
public interface PaymentDao {
    @SuppressWarnings("EmptyMethod")
    void setPaymentSync(@SuppressWarnings("unused") long id, @SuppressWarnings("unused") @NonNull BigDecimal payment);
    @SuppressWarnings("EmptyMethod")
    void setClaimedSync(@SuppressWarnings("unused") long id, @SuppressWarnings("unused") @Nullable Instant claimed);
    @SuppressWarnings("EmptyMethod")
    void setPaidSync(@SuppressWarnings("unused") long id, @SuppressWarnings("unused") @Nullable Instant paid);
}