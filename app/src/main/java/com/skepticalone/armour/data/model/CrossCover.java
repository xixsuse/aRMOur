package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;

import java.math.BigDecimal;

public final class CrossCover extends Item implements Payable {

    @NonNull
    private final LocalDate date;
    @NonNull
    private final PaymentData paymentData;

    public CrossCover(@NonNull RawCrossCoverEntity rawCrossCover, @NonNull ZoneId zoneId) {
        super(rawCrossCover.getId(), rawCrossCover.getComment());
        date = rawCrossCover.getDate();
        paymentData = new PaymentData(rawCrossCover.getPaymentData(), zoneId);
    }

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    @NonNull
    @Override
    public PaymentData getPaymentData() {
        return paymentData;
    }

    @NonNull
    @Override
    public BigDecimal getTotalPayment() {
        return getPaymentData().getPayment();
    }

}
