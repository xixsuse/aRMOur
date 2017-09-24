package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import org.threeten.bp.ZoneId;

import java.math.BigDecimal;

public final class Expense extends Item implements Payable {

    @NonNull
    private final String title;

    @NonNull
    private final PaymentData paymentData;

    public Expense(@NonNull RawExpenseEntity rawExpense, @NonNull ZoneId zoneId) {
        super(rawExpense.getId(), rawExpense.getComment());
        title = rawExpense.getTitle();
        paymentData = new PaymentData(rawExpense.getPaymentData(), zoneId);
    }

    @NonNull
    public final String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public final PaymentData getPaymentData() {
        return paymentData;
    }

    @NonNull
    @Override
    public final BigDecimal getTotalPayment() {
        return getPaymentData().getPayment();
    }
}
