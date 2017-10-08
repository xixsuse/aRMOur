package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import org.threeten.bp.ZoneId;

import java.math.BigDecimal;

public final class Expense extends Item implements Payment {

    @NonNull
    private final String title;

    @NonNull
    private final Data paymentData;

    public Expense(@NonNull ExpenseEntity rawExpense, @NonNull ZoneId timeZone) {
        super(rawExpense.getId(), rawExpense.getComment());
        title = rawExpense.getTitle();
        paymentData = new Data(rawExpense.getPaymentData(), timeZone);
    }

    @NonNull
    public final String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public final Data getPaymentData() {
        return paymentData;
    }

    @NonNull
    @Override
    public final BigDecimal getTotalPayment() {
        return getPaymentData().getPayment();
    }

}
